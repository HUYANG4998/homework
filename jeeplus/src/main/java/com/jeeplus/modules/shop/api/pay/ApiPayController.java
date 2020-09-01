package com.jeeplus.modules.shop.api.pay;

import com.alibaba.druid.util.StringUtils;
import com.jeeplus.common.json.AjaxJson;

import com.jeeplus.modules.shop.api.payutils.alipay.ALiPayController;
import com.jeeplus.modules.shop.api.payutils.wxpay.WXpayController;
import com.jeeplus.modules.shop.api.payutils.wxpay.entity.WxNotifyParam;
import com.jeeplus.modules.shop.api.systemutil.OrderNumbersUtil;
import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.order.service.OrderService;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lhh
 * @date 2019/7/22 0022
 */
@Api(value="ApiAliPayController",description="App支付控制器")
@Controller
@CrossOrigin
@RequestMapping(value = "${adminPath}/api/customer/")
public class ApiPayController {


    @Autowired
    private  ALiPayController aLiPayController;


    @Autowired
    private WXpayController wXpayController;
    @Autowired
    private OrderService orderService;



    @ApiOperation(notes = "initPay", httpMethod = "POST", value = "初始化支付相关")
    @ApiImplicitParams({
            @ApiImplicitParam(name="price",value = "金额",required = true, paramType = "query",dataType = "String"),
            @ApiImplicitParam(name="order_id",value = "订单id",required = true, paramType = "query",dataType = "String"),
            @ApiImplicitParam(name="type",value = "操作种类类型（0:支付订单 1:支付保证金）",required = true, paramType = "query",dataType = "String"),
            @ApiImplicitParam(name="state",value = "支付类型（0:支付宝 1:微信）",required = true, paramType = "query",dataType = "String"),
            @ApiImplicitParam(name="pay_id",value = "用户id",required = true, paramType = "query",dataType = "String")
    })
    @ResponseBody
    @RequestMapping(value = "/initPay")
    public AjaxJson initAliPay(HttpServletRequest request, HttpServletResponse response){
        AjaxJson j = new AjaxJson();
        String type=request.getParameter("type");
        String state=request.getParameter("state");
        String pay_id=request.getParameter("pay_id");
        String order_id=request.getParameter("order_id");

        if(null == state || "".equals(state) ||
                null == type || "".equals(type)||"".equals(order_id)||null==order_id){
            j.setErrorCode("500");
            j.setSuccess(false);
            j.setMsg("参数错误");
            return  j;
        }
        /*if(!StringUtils.isEmpty(order_id)){
            Order order=new Order();
            order.setNumbers(order_id);
            List<Order> list = orderService.findList(order);

        }*/


        if(!StringUtils.isEmpty(order_id)){
            Order order = orderService.get(order_id);

            if(order!=null){
                order.setNumbers(OrderNumbersUtil.getD(UUID.randomUUID().toString()));
                orderService.save(order);
                String price=order.getTotalPrice();
                Map<String,Object> map=new HashMap<>();
                map.put("price",price);
                map.put("type",type);
                map.put("state",state);
                map.put("pay_id",pay_id);
                map.put("order_id",order.getNumbers());
                AjaxJson pay = pay(request, response, map);
                return pay;
            }
        }
        return j;

    }
    public  AjaxJson pay(HttpServletRequest request,HttpServletResponse response,Map<String,Object> map){
        AjaxJson j = new AjaxJson();
        String price=map.get("price")==null?null:(String)map.get("price");
        String type=map.get("type")==null?null:(String)map.get("type");
        String state=map.get("state")==null?null:(String)map.get("state");
        String pay_id=map.get("pay_id")==null?null:(String)map.get("pay_id");
        String order_id=map.get("order_id")==null?null:(String)map.get("order_id");
        if("1".equals(type)){
            order_id=pay_id;
        }
        if(state.equals("0")){

            //初始化支付宝
            /*String order_id = oid+"/"+getUniqueOrder();
            System.out.println("xxxxx"+order_id);*/

            String s=aLiPayController.payIn(order_id, Double.valueOf(price), type);
            if(s.equals("error")){
                j.setErrorCode("501");
                j.setSuccess(false);
                j.setMsg("支付宝调取错误!");
                return  j;
            }
            j.setMsg("支付宝调取成功");
            j.put("canshu",s);
        }else if(state.equals("1")){
            //初始化微信
            /*String order_id=getUniqueOrder();*/
            /*order_id=pay_id;*/

            WxNotifyParam wxNotifyParam=wXpayController.initWx(request, response, Double.valueOf(price), order_id, type);
            if(null == wxNotifyParam){
                j.setErrorCode("501");
                j.setSuccess(false);
                j.setMsg("微信调取错误!");
                return  j;
            }
            j.setMsg("微信调取成功");
            j.put("canshu",wxNotifyParam);
        }
        return j;
    }


    public static String getUniqueOrder() {
        SimpleDateFormat format = new SimpleDateFormat("YYYYMMddHHmmss");
        String format2 = format.format(new Date());
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {
            //有可能是负数
            hashCodeV = - hashCodeV;
        }
        return "pk"+format2+String.format("%012d", hashCodeV);
    }

    public String doOrderNum() {
        Random random = new Random();
        SimpleDateFormat allTime = new SimpleDateFormat("YYYYMMddHHmmSSS");
        String subjectno = allTime.format(new Date())+random.nextInt(10);
        return subjectno+random.nextInt(10);
    }


}
