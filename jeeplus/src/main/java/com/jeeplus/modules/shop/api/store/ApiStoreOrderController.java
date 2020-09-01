package com.jeeplus.modules.shop.api.store;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.customer.service.CustomerService;
import com.jeeplus.modules.shop.customerfollow.entity.CustomerFollow;
import com.jeeplus.modules.shop.customerfollow.service.CustomerFollowService;
import com.jeeplus.modules.shop.express.entity.Express;
import com.jeeplus.modules.shop.express.service.ExpressService;
import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.order.service.OrderService;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.bytedeco.javacpp.presets.opencv_core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lhh
 * @date 2019/11/26 0026
 */

@Api(value="ApiStoreOrderController",description="App商家端订单控制器")
@Controller
@CrossOrigin
@RequestMapping(value = "${adminPath}/api/store")
public class ApiStoreOrderController extends BaseController {

    @Autowired
    private StoreService storeService;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private ExpressService expressService;

    @ModelAttribute
    public Store get(@RequestParam(required=false) String id) {
        Store entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = storeService.get(id);
        }
        if (entity == null){
            entity = new Store();
        }
        return entity;
    }

    @ApiOperation(notes = "appGetStoreOrderByType", httpMethod = "GET", value = "获取商家端订单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="store_id",value = "商家id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="pageNo",value = "页数",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="pageSize",value = "每页数据数",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="type",value = "0:待付款1:待发货2:已发货3:退款售后4:已完成5:已关闭",required = true, paramType = "query",dataType = "string"),

    })
    @ResponseBody
    @RequestMapping(value = "/appGetStoreOrderByType")
    public AjaxJson appGetStoreOrderByType(String store_id,String type, HttpServletRequest request, HttpServletResponse response){
        AjaxJson j=new AjaxJson();
        if (!StringUtils.isNotBlank(store_id)){
            j.setSuccess(false);
            j.setMsg("参数错误");
            j.setErrorCode("111");
            return j;
        }


        Order order=new Order();
        Store store=storeService.get(store_id);
        order.setStore(store);
        switch(type){
            case "0" :
                //待付款
                order.setOrderState("0");
                break; //可选
            case "1" :
                //待发货
                order.setOrderState("1");
                order.setStoreState("0");
                break; //可选
            case "2" :
                //已发货
                order.setOrderState("1");
                order.setStoreState("1");
                break; //可选
            case "3" :
                //退款
                order.setOrderState("1");
                order.setStoreState("2");
                break; //可选
            case "4" :
                //已完成
                order.setOrderState("2");
                order.setStoreState("1");
                break; //可选
            case "5" :
                //已关闭
                order.setOrderState("3");
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
            orders.add(order1);
        }
        bootstrapData.put("rows",orders);

            j.setSuccess(true);
            j.setMsg("查询成功");
            j.put("data",bootstrapData);

        return j;
    }



    @ApiOperation(notes = "appDeliverGoods", httpMethod = "POST", value = "商家端发货（复合）")
    @ApiImplicitParams({
            @ApiImplicitParam(name="express_id",value = "商家id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="order_id",value = "订单id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="numbers",value = "单号",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="type",value = "0:配送发货1:快递发货",required = true, paramType = "query",dataType = "string"),

    })
    @ResponseBody
    @RequestMapping(value = "/appDeliverGoods")
    public AjaxJson appDeliverGoods(String express_id,String type,String numbers,String order_id){
        AjaxJson j=new AjaxJson();
        if(null == express_id ||"".equals(express_id)
                ||null == type ||"".equals(type) ||
                null == numbers ||"".equals(numbers)
                || null==order_id ||"".equals(order_id)){
            j.setErrorCode("111");
            j.setSuccess(false);
            j.setMsg("参数错误");
            return j;
        }

        Express express=expressService.get(express_id);

        Order order=orderService.get(order_id);

        if(type.equals("0")){
            //配送发货

            //骑手状态变为待接单
            order.setRiderState("0");
        }else if(type.equals("1")){
            //快递发货
            order.setExpress(express);
            order.setExpressNumbers(numbers);

        }
        order.setOrderState("2");
        order.setStoreState("1");
        orderService.save(order);
        j.setMsg("发货成功");
        j.setSuccess(true);
        j.setErrorCode("666");
        return j;
    }

}
