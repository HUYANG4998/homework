package com.jeeplus.modules.shop.api.rider;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.monitor.utils.Common;
import com.jeeplus.modules.shop.api.systemutil.LocationUtils;
import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.customeraddress.entity.CustomerAddress;
import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.order.service.OrderService;
import com.jeeplus.modules.shop.rider.entity.Rider;
import com.jeeplus.modules.shop.rider.service.RiderService;
import com.jeeplus.modules.shop.riderrefuse.entity.RiderRefuse;
import com.jeeplus.modules.shop.riderrefuse.service.RiderRefuseService;
import com.jeeplus.modules.shop.store.entity.Store;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lhh
 * @date 2020/1/15 0015
 */
@Api(value="ApiRiderOrderController",description="App骑手端订单控制器")
@Controller
@CrossOrigin
@RequestMapping(value = "${adminPath}/api/rider")
public class ApiRiderOrderController  extends BaseController {
    @Autowired
    private RiderService riderService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RiderRefuseService riderRefuseService;


    @ApiOperation(notes = "appGetRiderOrderByType", httpMethod = "GET", value = "获取骑手端订单信息（新任务,待取货,配送中）")
    @ApiImplicitParams({
            @ApiImplicitParam(name="rider_id",value = "骑手id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="pageNo",value = "页数",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="pageSize",value = "每页数据数",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="type",value = "0:新任务1:待取货2:配送中3配送完成4配送取消",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="lng",value = "骑手当前经度",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="lat",value = "骑手当前纬度",required = true, paramType = "query",dataType = "string"),

    })
    @ResponseBody
    @RequestMapping(value = "/appGetRiderOrderByType")
    public AjaxJson appGetRiderOrderByType(String rider_id, String type,String lng,String lat, HttpServletRequest request, HttpServletResponse response){
        AjaxJson j=new AjaxJson();
        if (!StringUtils.isNotBlank(rider_id)){
            j.setSuccess(false);
            j.setMsg("参数错误");
            j.setErrorCode("111");
            return j;
        }
        //先查询所有 用户已经付款的单子

        Order order=new Order();
        Rider rider=riderService.get(rider_id);
        String yesNO=rider.getYesNO();
        if(yesNO.equals("0")){
            j.setMsg("暂未开启接单");
            j.setSuccess(false);
            j.setErrorCode("111");
            return j;
        }
        order.setRider(rider);
        switch(type){
            case "0" :
                //新任务
                order.setOrderState("1");
                order.setRiderState("0");
                break; //可选
            case "1" :
                //待取货
                order.setRiderState("1");
                order.setOrderState("1");
                break; //可选
            case "2" :
                //配送中
                order.setRiderState("2");
                break; //可选
            case "3" :
                //配送完成
                order.setRiderState("3");
                break; //可选
            case "4" :
                //配送取消
                order.setOrderState("3");
                order.setRiderState("0");
                break; //可选
            default : //可选
                //语句
        }
        Page<Order> page = orderService.findPage(new Page<Order>(request, response), order);
        Map<String, Object> bootstrapData=getBootstrapData(page);
        List<Order> list =(ArrayList<Order>)bootstrapData.get("rows");
        List<Order> orders=new ArrayList<>();
        for (Order o:
                list) {
            Order order1=orderService.get(o.getId());
            //计算骑手当前位置 到 商家的距离
            Store store=order1.getStore();
            double distance=LocationUtils.getDistance(store.getLng(), store.getLat(), lng, lat);
            order1.setShangJu(distance);
            //计算骑手当前位置到用户距离
            CustomerAddress customerAddress=order1.getCustomerAddress();
            double distance1=LocationUtils.getDistance(customerAddress.getLng(), customerAddress.getLat(), lng, lat);
            order1.setYongJu(distance1);
            orders.add(order1);
        }
        bootstrapData.put("rows",orders);
        if(list.size() == 0){
            j.setSuccess(false);
            j.setMsg("暂无数据");
            j.setErrorCode("111");
        }else {
            j.setSuccess(true);
            j.setErrorCode("666");
            j.setMsg("查询成功");
            j.put("data",bootstrapData);
        }
        return j;
    }



    @ApiOperation(notes = "appRefuseOrder", httpMethod = "POST", value = "骑手拒绝订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name="rider_id",value = "骑手id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="order_id",value = "订单id",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appRefuseOrder")
    public AjaxJson appRefuseOrder(String rider_id,String order_id){
        AjaxJson j=new AjaxJson();

        if(null == rider_id){
            j.setSuccess(false);
            j.setMsg("参数错误");
            return j;
        }
        Order order=orderService.get(order_id);

        order.setRiderState("0");
        order.setRider(new Rider());
        orderService.save(order);

        //骑手有无单子状态修改成无
        Rider rider=riderService.get(rider_id);
        rider.setIsYou("0");
        riderService.save(rider);

        //插入骑手拒绝订单表
        RiderRefuse riderRefuse=new RiderRefuse();
        riderRefuse.setOrder(order);
        riderRefuse.setRider(rider);
        riderRefuseService.save(riderRefuse);

        //查询今天内有几次拒绝订单记录 给前台提示
        int todayCount=riderRefuseService.findTodayCount(rider.getId());
        int sheng = 3-todayCount;
        j.setSuccess(true);
        j.setMsg("今天还有"+sheng+"次机会拒绝订单");
        j.setSuccess(true);
        j.put("data",sheng);
        return j;
    }


}
