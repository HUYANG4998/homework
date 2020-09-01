package com.jeeplus.modules.shop.api.order;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.monitor.utils.Common;
import com.jeeplus.modules.shop.api.deal.entity.Deal;
import com.jeeplus.modules.shop.api.deal.service.DealService;
import com.jeeplus.modules.shop.api.pay.ApiPayController;

import com.jeeplus.modules.shop.api.ridereval.entity.RiderEval;
import com.jeeplus.modules.shop.api.ridereval.service.RiderEvalService;
import com.jeeplus.modules.shop.api.systemutil.LocationUtils;
import com.jeeplus.modules.shop.api.systemutil.OrderNumbersUtil;
import com.jeeplus.modules.shop.api.systemutil.ShopCarUtil;
import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.customer.service.CustomerService;
import com.jeeplus.modules.shop.customeraddress.entity.CustomerAddress;
import com.jeeplus.modules.shop.customeraddress.service.CustomerAddressService;
import com.jeeplus.modules.shop.customerasses.entity.CustomerAsses;
import com.jeeplus.modules.shop.customerasses.service.CustomerAssesService;
import com.jeeplus.modules.shop.express.entity.Express;
import com.jeeplus.modules.shop.express.service.ExpressService;
import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.order.entity.OrderDetail;
import com.jeeplus.modules.shop.order.service.OrderService;
import com.jeeplus.modules.shop.ordertotal.entity.OrderTotal;
import com.jeeplus.modules.shop.ordertotal.service.OrderTotalService;
import com.jeeplus.modules.shop.otherprice.entity.OtherPrice;
import com.jeeplus.modules.shop.otherprice.service.OtherPriceService;
import com.jeeplus.modules.shop.shopcar.entity.ShopCar;
import com.jeeplus.modules.shop.shopcar.service.ShopCarService;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
import com.jeeplus.modules.shop.storediscount.entity.StoreDiscount;
import com.jeeplus.modules.shop.storediscount.service.StoreDiscountService;
import com.jeeplus.modules.shop.util.*;
import com.jeeplus.modules.shop.util.orderchange.OrderHandleChain;
import com.jeeplus.modules.shop.wares.entity.Wares;
import com.jeeplus.modules.shop.wares.service.WaresService;
import com.jeeplus.modules.shop.waresspecs.entity.WaresSpecs;
import com.jeeplus.modules.shop.waresspecs.service.WaresSpecsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/*import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.monitor.utils.Common;
import com.jeeplus.modules.shop.api.pay.ApiPayController;
import com.jeeplus.modules.shop.api.systemutil.LocationUtils;
import com.jeeplus.modules.shop.api.systemutil.OrderNumbersUtil;
import com.jeeplus.modules.shop.api.systemutil.ShopCarUtil;
import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.customer.service.CustomerService;
import com.jeeplus.modules.shop.customeraddress.entity.CustomerAddress;
import com.jeeplus.modules.shop.customeraddress.service.CustomerAddressService;
import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.order.entity.OrderDetail;
import com.jeeplus.modules.shop.order.mapper.OrderDetailMapper;
import com.jeeplus.modules.shop.order.service.OrderService;
import com.jeeplus.modules.shop.ordertotal.entity.OrderTotal;
import com.jeeplus.modules.shop.ordertotal.service.OrderTotalService;
import com.jeeplus.modules.shop.otherprice.entity.OtherPrice;
import com.jeeplus.modules.shop.otherprice.service.OtherPriceService;
import com.jeeplus.modules.shop.shopcar.entity.ShopCar;
import com.jeeplus.modules.shop.shopcar.service.ShopCarService;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
import com.jeeplus.modules.shop.wares.entity.Wares;
import com.jeeplus.modules.shop.wares.service.WaresService;
import com.jeeplus.modules.shop.waresspecs.entity.WaresSpecs;
import com.jeeplus.modules.shop.waresspecs.service.WaresSpecsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringEscapeUtils;
import org.bytedeco.javacpp.presets.opencv_core;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.faces.view.facelets.FaceletsAttachedObjectHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;*/
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lhh
 * @date 2019/12/27 0002
 */
@Api(value = "ApiOrderController", description = "App订单控制器")
@Controller
@CrossOrigin
@RequestMapping(value = "${adminPath}/api/order")
public class ApiOrderController extends BaseController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private WaresService waresService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private ShopCarService shopCarService;
    @Autowired
    private WaresSpecsService waresSpecsService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerAddressService customerAddressService;
    @Autowired
    private OtherPriceService otherPriceService;
    @Autowired
    private OrderTotalService orderTotalService;
    @Autowired
    private ApiPayController apiPayController;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private CustomerAssesService customerAssesService;
    @Autowired
    private RiderEvalService riderEvalService;
    @Autowired
    private DealService dealService;
    @Autowired
    private StoreDiscountService storeDiscountService;

    @ApiOperation(notes = "deleteOrder", httpMethod = "POST", value = "根据订单id删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderid", value = "订单id", required = true, paramType = "query", dataType = "string"),

    })
    @ResponseBody
    @RequestMapping(value = "/deleteOrder")
    public AjaxJson deleteOrder(String orderid) {

        AjaxJson j = new AjaxJson();
        if (!StringUtils.isEmpty(orderid)) {
            Order order = orderService.get(orderid);
            System.out.println("234");
            if (order != null) {
                orderService.delete(order);
                j.setSuccess(true);
                j.put("data", "删除成功");
            } else {
                j.setSuccess(false);
                j.put("data", "订单已删除");
            }
        } else {
            j.setMsg("删除失败");
            j.setSuccess(false);
        }
        return j;
    }
    @ApiOperation(notes = "selectShopCar", httpMethod = "POST", value = "根据购物车集合id查询购物车信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopCarList", value = "购物车集合", required = true, paramType = "query", dataType = "string"),

    })
    @ResponseBody
    @RequestMapping(value = "/selectShopCar")
    public AjaxJson selectShopCar(String[] shopCarList) {

        AjaxJson j = new AjaxJson();
        List<String> listId=new ArrayList<>();
        if(shopCarList.length>0){
            List<ShopCar> listShop=new ArrayList<>();
            for (String shopCarId:shopCarList){
                if(!listId.contains(shopCarId)){
                    listId.add(shopCarId);
                }
                ShopCar shopCar = shopCarService.get(shopCarId);
                if(shopCar!=null){
                    shopCar.setStoreId(shopCar.getStore().getId());
                    String img=shopCar.getWares().getImg();
                    String[] split=img.split("[,]");
                    shopCar.setWaresImg(split[0]);
                    listShop.add(shopCar);
                }
            }
            Map<String, List<ShopCar>> map =  listShop.stream()
                    .collect(Collectors.groupingBy(ShopCar::getStoreId));
            ArrayList<ShopCarUtil> systems=new ArrayList<>();
            //遍历map集合
            for (Map.Entry<String, List<ShopCar>> entry : map.entrySet()) {
                Store store=storeService.get(entry.getKey());
                ShopCarUtil shopCarUtil=new ShopCarUtil();
                shopCarUtil.setImg(store.getImg());
                shopCarUtil.setName(store.getName());
                shopCarUtil.setStoreId(store.getId());
                shopCarUtil.setList(entry.getValue());
                List<ShopCar> value = entry.getValue();
                for (ShopCar shopCar1:value){
                    StoreDiscount storeDiscount=new StoreDiscount();
                    storeDiscount.setStore(store);
                    storeDiscount.setDiscountType("1");
                    storeDiscount.setYesNo("1");
                    List<StoreDiscount> storeDiscountList=storeDiscountService.findList(storeDiscount);
                    String discount="0";
                    //商家满减金额
                    String storeDiscountPrice="0";
                    //商品满减金额
                    String waresDiscountPrice="0";
                    if(storeDiscountList.size()>0){
                        StoreDiscount storeDiscount1 = storeDiscountList.get(0);
                        if(!"2".equals(storeDiscount1.getDiscountState())){
                            storeDiscountPrice=DiscountUtils.getDiscount(storeDiscount1, Double.valueOf(shopCar1.getPrice()));
                        }else{
                            discount = DiscountUtils.getDiscount(storeDiscount1, Double.valueOf(shopCar1.getWaresPrice()));
                        }


                    }else{
                        discount=shopCar1.getWaresPrice();
                    }
                    StoreDiscount storeDiscount1=new StoreDiscount();
                    storeDiscount1.setWares(shopCar1.getWares());
                    storeDiscount1.setDiscountType("2");
                    storeDiscount1.setYesNo("1");
                    List<StoreDiscount> storeDiscountList1=storeDiscountService.findList(storeDiscount1);
                    if(storeDiscountList1.size()>0){
                        StoreDiscount storeDiscount2 = storeDiscountList1.get(0);
                        if(!"2".equals(storeDiscount2.getDiscountState())){
                            waresDiscountPrice=DiscountUtils.getDiscount(storeDiscount2, Double.valueOf(shopCar1.getPrice()));
                        }else{
                            discount = DiscountUtils.getDiscount(storeDiscount2, Double.valueOf(discount));
                        }

                    }
                    Double price=Double.valueOf(storeDiscountPrice)+Double.valueOf(waresDiscountPrice);
                    shopCar1.setPresentPrice(String.valueOf(Double.valueOf(shopCar1.getWaresPrice())*Integer.valueOf(shopCar1.getNum())-price));

                }
                shopCarUtil.setList(value);
                systems.add(shopCarUtil);
            }
            j.setSuccess(true);
            j.put("data", systems);
        }
        return j;
    }

    @ApiOperation(notes = "selectGuigePrice", httpMethod = "POST", value = "根据商品id和规格")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "waresid", value = "商品id", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "guige", value = "规格", required = true, paramType = "query", dataType = "string")

    })
    @ResponseBody
    @RequestMapping(value = "/selectGuigePrice")
    public AjaxJson selectGuigePrice(String waresid,String guige) {

        AjaxJson j = new AjaxJson();
        if (!StringUtils.isEmpty(waresid)&&!StringUtils.isEmpty(guige)) {
            WaresSpecs waresSpecs=new WaresSpecs();
            Wares wares=waresService.get(waresid);
            waresSpecs.setWares(wares);
            waresSpecs.setWaresSpecs(guige);
            List<WaresSpecs> list = waresSpecsService.findList(waresSpecs);
            if(list.size()==0){
                j.setSuccess(false);
                j.setMsg("规格已失效");
            }else if(list.size()==1){
                j.setSuccess(true);
                j.put("data", list.get(0));
            }else{
                j.setSuccess(false);
                j.setMsg("规格重复");
            }

        } else {
            j.setMsg("参数异常");
            j.setSuccess(false);
        }
        return j;
    }

    @ApiOperation(notes = "orderEavl", httpMethod = "POST", value = "发起评价--转移EOLINKER")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customer_id", value = "订单id", required = true, paramType = "query", dataType = "string"),

    })
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/orderEavl")
    public AjaxJson orderEavl(@RequestBody Map<String, Object> map){
        AjaxJson j = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInfo = objectMapper.writeValueAsString(map.get("riderEval"));
            RiderEval riderEval = objectMapper.readValue(jsonInfo, RiderEval.class);
            List<Map<String, Object>> customerAssesList = map.get("customerAssesList") == null ? null : (List<Map<String, Object>>) map.get("customerAssesList");

            /*String waresId = map.get("waresId") == null ? null : (String) map.get("waresId");*/
            String customerId = map.get("customerId") == null ? null : (String) map.get("customerId");
            String orderId = map.get("orderId") == null ? null : (String) map.get("orderId");
            String yesNo = map.get("yesNo") == null ? null : (String) map.get("yesNo");

            Customer customer = customerService.get(customerId);
            j = new AjaxJson();
            CustomerAsses customerAsses1=new CustomerAsses();
            customerAsses1.setOrderId(orderId);
            List<CustomerAsses> list = customerAssesService.findList(customerAsses1);
            /*if(list.size()==0){*/
                if (customerAssesList.size() > 0) {
                    for (Map<String, Object> m : customerAssesList) {
                        String waresId = m.get("waresId") == null ? null : (String) m.get("waresId");
                        String contents = m.get("contents") == null ? null : (String) m.get("contents");
                        String img = m.get("img") == null ? null : (String) m.get("img");
                        /*String yesNo = map.get("yesNo") == null ? null : (String) m.get("yesNo");*/
                        Integer star = m.get("star") == null ? null : (Integer) m.get("star");
                        Wares wares = waresService.get(waresId);
                        CustomerAsses customerAsses = new CustomerAsses();
                        customerAsses.setWares(wares);
                        customerAsses.setCustomer(customer);
                        customerAsses.setContents(contents);
                        customerAsses.setImg(img);
                        customerAsses.setYesNo(yesNo);
                        customerAsses.setOrderId(orderId);
                        customerAsses.setStar(star);

                        if (!IsNull.objCheckIsNull(customerAsses, Arrays.asList("delFlag"), null)) {
                            customerAssesService.save(customerAsses);
                        }

                    }
                }
            /*}else{
                j.setMsg("您已评价");
                j.setSuccess(false);
                return j;
            }*/

            riderEval.setCustomerId(customerId);
            riderEval.setOrderId(orderId);
            if (!IsNull.objCheckIsNull(riderEval, Arrays.asList("delFlag", "riderReviewId"), null)) {

                riderEvalService.insertRiderView(riderEval);
            }
            Order order = orderService.get(orderId);
            order.setOrderState("4");
            orderService.save(order);
            j.setMsg("评价完成");
            j.setSuccess(true);
        } catch (IOException e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return j;
    }

    @ApiOperation(notes = "selectOrderEavl", httpMethod = "POST", value = "查看订单评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", required = true, paramType = "query", dataType = "string"),

    })
    @ResponseBody
    @RequestMapping(value = "/selectOrderEavl")
    public AjaxJson selectOrderEavl(String orderId,String pagesize,String pagenum) {

        AjaxJson j = new AjaxJson();
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(orderId)) {
            CustomerAsses customerAsses = new CustomerAsses();
            customerAsses.setOrderId(orderId);

            List<CustomerAsses> customerAssesList = customerAssesService.findList(customerAsses);
            if(customerAssesList.size()>0){
                map.put("customer", customerAssesList);
                CustomerAsses customerAsses1 = customerAssesList.get(0);
                if(customerAsses1!=null){
                    map.put("yesNo",customerAsses1.getYesNo());
                }

            }
            RiderEval riderEval = riderEvalService.selectByOrderId(orderId);
            if(riderEval!=null){
                map.put("review", riderEval);
            }
            j.setMsg("查询成功");
            j.setSuccess(true);
            j.put("customerAsses", map);
        } else {
            j.setMsg("参数错误");
            j.setSuccess(false);
        }
        return j;
    }

    @ApiOperation(notes = "selectWaresEavl", httpMethod = "POST", value = "查看商品评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "waresId", value = "商品id", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/selectWaresEavl")
    public AjaxJson selectWaresEavl(String waresId,String pageSize,String pageNum) {
        pageSize=pageSize==null?"1":pageSize;
        pageNum=pageNum==null?"0":pageNum;
        AjaxJson j = new AjaxJson();
        if (!StringUtils.isEmpty(waresId)) {
            CustomerAsses customerAsses = new CustomerAsses();
            customerAsses.setWares(waresService.get(waresId));
            Page page = customerAssesService.findPage(new Page(Integer.valueOf(pageNum), Integer.valueOf(pageSize)), customerAsses);
            List<CustomerAsses> list = page.getList();
            Long totalPage = page.getCount();
            j.put("num",totalPage);
            j.put("customerAsses", list);
            j.setMsg("查询成功");
            j.setSuccess(true);
        } else {
            j.setMsg("参数错误");
            j.setSuccess(false);
        }
        return j;
    }

    @ApiOperation(notes = "getShippingInfo", httpMethod = "POST", value = "查看订单物流")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", required = true, paramType = "query", dataType = "string"),


    })
    @ResponseBody
    @RequestMapping(value = "/getShippingInfo")
    public AjaxJson getShippingInfo(String orderId) {

        AjaxJson j = new AjaxJson();

        if (!StringUtils.isEmpty(orderId)) {

            Order order = orderService.get(orderId);
            Map<String, Object> result = (Map) CacheUtils.get(orderId);

            if (result != null && !result.isEmpty()) {
                String s = (String) result.get("log");
                Map<String, Object> m1 = JSONUtils.jsonToMap(s);
                Map<String, Object> m2 = (Map) m1.get("result");
                String issign = (String) m2.get("issign");
                if (!"1".equals(issign)) {
                    String time = (String) result.get("time");
                    long between = DateUtil.between(DateUtil.parse(DateUtil.now()), DateUtil.parse(time), DateUnit.HOUR);
                    if (between > 12) {
                        if(!StringUtils.isEmpty(order.getExpressNumbers())){
                            result = getLogistics(order.getExpressNumbers(),orderId);
                        }
                    }
                }
            } else {
                System.out.println("内存里是空的");
                if(!StringUtils.isEmpty(order.getExpressNumbers())){
                    result = getLogistics(order.getExpressNumbers(),orderId);
                }
            }

            j.setMsg("查询成功");
            j.setSuccess(true);
            j.put("data", result);
        } else {
            j.setMsg("参数错误");
            j.setSuccess(false);
        }
        return j;
    }


    private Map<String, Object> getLogistics(String number,String orderId) {
        Map<String, Object> map = new HashMap<>();
        String logistics = Logistics.getLogistics(number);
        map.put("log", logistics);
        map.put("time", DateUtil.now());
        CacheUtils.put(orderId, map);
        return map;
    }

    @ApiOperation(notes = "updateOrder", httpMethod = "POST", value = "修改订单---转移EOLINKER ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order", value = "订单信息", required = true, paramType = "query", dataType = "order"),
            @ApiImplicitParam(name = "id", value = "订单id", required = true, paramType = "query", dataType = "string"),
            /*@ApiImplicitParam(name = "orderState", value = "用户交易状态0待付款1待收货2待评价3取消", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "storeState", value = "商家交易状态0待发货1已发货2退款售后", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "riderState", value = "骑手交易状态0待接单1已接单2待取货3待配送4配送完成", required = true, paramType = "query", dataType = "string"),*/
            @ApiImplicitParam(name = "byThree", value = "取消原因", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "expressNumbers", value = "快递单号", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "status", value = "状态0发货1发起退款2给予退款3拒绝退款4确认收货5去配送6配送完成7取消订单", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "expressId", value = "快递方式id", required = true, paramType = "query", dataType = "string")

    })
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/updateOrder")
    public AjaxJson updateOrder(@RequestBody Map<String, Object> map) {
        AjaxJson j = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInfo = objectMapper.writeValueAsString(map.get("order"));
            Order order = objectMapper.readValue(jsonInfo, Order.class);
            String expressId = map.get("expressId") == null ? null : (String) map.get("expressId");
            if (!StringUtils.isEmpty(expressId)) {
                Express express = expressService.get(expressId);
                if (express != null) {
                    order.setExpress(express);
                }
            }
            j = new AjaxJson();
            if (order != null) {
                String id = order.getId();
                if (!StringUtils.isEmpty(id)) {
                    Order orderData = orderService.get(id);
                    if (orderData != null) {
                        orderData = (Order) Merge.combineSydwCore(order, orderData);
                        OrderHandleChain orderHandleChain = new OrderHandleChain();
                        String success = orderHandleChain.handleOrder(orderData);
                        orderHandleChain.orderSave(orderData);
                        j.setSuccess(true);
                        j.put("data", success);
                    } else {
                        j.setMsg("订单不存在");
                        j.setSuccess(false);
                    }
                } else {
                    j.setMsg("订单id未传");
                    j.setSuccess(false);
                }
            } else {
                j.setMsg("订单参数错误");
                j.setSuccess(false);
            }
            /*System.out.println(10/0);*/
        } catch (IOException e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return j;
    }

    private String dealStatus(Order order) {
        String success = null;
        //商家状态
        String storeState = order.getStoreState();
        //骑手状态
        String riderState = order.getRiderState();
        //用户状态
        String orderState = order.getOrderState();
        if ("0".equals(orderState)) {
            success = "订单待付款";
        } else if ("1".equals(orderState) && "0".equals(storeState)) {
            success = "订单待发货";
        } else if ("1".equals(orderState) && "0".equals(riderState)) {
            success = "等待骑手接单";
        } else if ("1".equals(storeState)) {
            success = "订单已发货";
        } else if ("1".equals(riderState)) {
            success = "骑手已接单";
        } else if ("2".equals(riderState)) {
            success = "骑手已取货，正在配送中";
        } else if ("3".equals(riderState)) {
            success = "骑手已送达，配送完成";
        }
        return success;
    }


    @ApiOperation(notes = "selectPeiType", httpMethod = "POST", value = "根据用户地址查看配送类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeList", value = "商家id集合", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "addressid", value = "用户地址id", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customerid", value = "用户id", required = true, paramType = "query", dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/selectPeiType")
    public AjaxJson selectPeiType(String[] storeList,String addressid, String customerid) {

        AjaxJson j = new AjaxJson();
        CustomerAddress customerAddress = new CustomerAddress();
        if (!StringUtils.isEmpty(addressid)) {
            customerAddress = customerAddressService.get(addressid);
        } else {
            Customer customer = customerService.get(customerid);
            customerAddress.setYesNo("1");
            customerAddress.setCustomer(customer);
            List<CustomerAddress> list = customerAddressService.findList(customerAddress);
            if (list.size() > 0) {
                customerAddress = list.get(0);
            }
        }
        Map<String, Object> map = new HashMap<>();
        if (customerAddress != null) {
            if(!StringUtils.isEmpty(customerAddress.getLat())){
                Double totalPeiPrice=0.0;
                for (String storeid : storeList) {
                    Order order = new Order();
                    Store store = storeService.get(storeid);
                    /*order.setCustomerAddress(list.get(0));*/
                    //计算配送方式
                    int peiType = getPeiType(customerAddress, store);
                    if (peiType == 0) {
                        //配送方式为骑手配送 计算配送费
                        Double total_pei = getPei(customerAddress, store);
                        totalPeiPrice+=total_pei;
                        order.setPeiType("0");
                        //计算总配送距离
                        double distance = LocationUtils.getDistance(customerAddress.getLng(), customerAddress.getLat(), store.getLng(), store.getLat());
                        double div = Common.div(distance, 1000, 2);
                        System.out.println("xxxxxxx" + div);
                        order.setTotalDistance(String.valueOf(div));
                        order.setTotalPei(String.valueOf(total_pei));
                    } else if (peiType == 1) {
                        //物流配送
                        order.setPeiType("1");
                        order.setTotalPei("0");
                    }
                    /*order.setCustomerAddress(customerAddress);*/
                    map.put(storeid, order);

                }
                j.setSuccess(true);
                j.setMsg("查询成功");
                j.put("orders", map);
                j.put("customerAddress",customerAddress);
                j.put("peiPrice",new DecimalFormat("0.00").format(totalPeiPrice));
            }else{
                j.setSuccess(true);
                j.setMsg("暂无默认地址");

            }
        } else {
            j.setSuccess(false);
            j.setMsg("地址id错误");
        }

        return j;
    }

    @ApiOperation(notes = "appAddOrder", httpMethod = "POST", value = "用户从购物车创建订单 转移EOLINKER ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopcar_id", value = "组合数据集合形式", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customer_id", value = "用户id", required = true, paramType = "query", dataType = "string"),
    })
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    @RequestMapping(value = "/appAddOrder")
    public AjaxJson appAddOrder(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> map1) throws Exception {
        AjaxJson j = new AjaxJson();

        try {
            ObjectMapper objectMapper1 = new ObjectMapper();

            List<Map<String, Object>> listOrder = (List<Map<String, Object>>) map1.get("orderList");
            /*String addressid=(String)map1.get("adressid");*/
            String customerid = (String) map1.get("customerid");
            String status = (String) map1.get("status");
            String customerAddressId = (String) map1.get("customerAddressId");
            CustomerAddress customerAddress = customerAddressService.get(customerAddressId);
            Customer customer = customerService.get(customerid);
            /*CustomerAddress customeraddress=customerAddressService.get(addressid);*/
            Double alipayprice = 0.0;
            String number = OrderNumbersUtil.getD(UUID.randomUUID().toString());
            for (Map<String, Object> m : listOrder) {
                String jsonInfo1 = objectMapper1.writeValueAsString(m.get("order"));
                Order order = objectMapper1.readValue(jsonInfo1, Order.class);
                /*Order order=(Order)m.get("order");*/
                List<String> shopcarList = (List<String>) m.get("shopcarList");
                String storeid = (String) m.get("storeid");
                Store store = storeService.get(storeid);
                if(customerAddress!=null){
                    order.setCustomerAddress(customerAddress);
                }

                int count = 0;
                double totalprice = 0.0;
                List<OrderDetail> orderDetails = new ArrayList<>();
                for (String shopcarid : shopcarList) {
                    OrderDetail orderDetail = new OrderDetail();
                    ShopCar shopCar = shopCarService.get(shopcarid);
                    orderDetail.setWares(shopCar.getWares());
                    if (shopCar != null) {
                        Wares wares = waresService.get(shopCar.getWares().getId());
                        if (wares != null) {
                            //计算商品总价格
                            Integer num = Integer.valueOf(shopCar.getNum());
                            orderDetail.setNum(shopCar.getNum());
                            if (!StringUtils.isEmpty(shopCar.getGuige())) {
                                WaresSpecs waresSpecs = waresSpecsService.get(shopCar.getGuige());
                                if (waresSpecs != null) {
                                    //订单详情设置商品规格价格
                                    orderDetail.setWaresPrice(waresSpecs.getWaresPrice());
                                    //订单详情设置商品规格类型  红色,14斤
                                    orderDetail.setByOne(waresSpecs.getWaresSpecs());
                                    orderDetail.setByTwo(waresSpecs.getId());
                                    orderDetail.setTotalPrice(String.valueOf(Double.valueOf(waresSpecs.getWaresPrice()) * num));
                                    Integer stock = Integer.valueOf(waresSpecs.getWaresStock());
                                    if (stock < num) {
                                        j.setMsg(shopCar.getName() + "库存不足");
                                        j.setSuccess(false);
                                        throw new Exception();
                                    } else {
                                        waresSpecs.setWaresStock(String.valueOf(stock - num));
                                        waresSpecsService.save(waresSpecs);
                                    }
                                } else {
                                    j.setMsg(shopCar.getName() + "规格已失效");
                                    j.setSuccess(false);
                                    throw new Exception();
                                }
                            } else {
                                //订单详情设置商品价格
                                orderDetail.setWaresPrice(wares.getPrice());
                                orderDetail.setTotalPrice(String.valueOf(Double.valueOf(wares.getPrice()) * num));
                                Integer stock = Integer.valueOf(wares.getStock());
                                if (stock < num) {
                                    j.setMsg(shopCar.getName() + "库存不足");
                                    j.setSuccess(false);
                                    throw new Exception();
                                } else {
                                    wares.setStock(String.valueOf(stock - num));
                                    waresService.save(wares);
                                }
                            }
                            //订单详情设置商品总价
                            StoreDiscount storeDiscount=new StoreDiscount();
                            storeDiscount.setStore(store);
                            storeDiscount.setDiscountType("1");
                            storeDiscount.setYesNo("1");
                            List<StoreDiscount> storeDiscountList=storeDiscountService.findList(storeDiscount);
                            String discount="0";
                            //商家满减金额
                            String storeDiscountPrice="0";
                            //商品满减金额
                            String waresDiscountPrice="0";
                            if(storeDiscountList.size()>0){
                                StoreDiscount storeDiscount1 = storeDiscountList.get(0);
                                if(!"2".equals(storeDiscount1.getDiscountState())){
                                    storeDiscountPrice=DiscountUtils.getDiscount(storeDiscount1, Double.valueOf(orderDetail.getTotalPrice()));
                                }else{
                                    discount = DiscountUtils.getDiscount(storeDiscount1, Double.valueOf(orderDetail.getWaresPrice()));
                                }


                            }else{
                                discount=orderDetail.getWaresPrice();
                            }
                            StoreDiscount storeDiscount1=new StoreDiscount();
                            storeDiscount1.setWares(orderDetail.getWares());
                            storeDiscount1.setDiscountType("2");
                            storeDiscount1.setYesNo("1");
                            List<StoreDiscount> storeDiscountList1=storeDiscountService.findList(storeDiscount1);
                            if(storeDiscountList1.size()>0){
                                StoreDiscount storeDiscount2 = storeDiscountList1.get(0);
                                if(!"2".equals(storeDiscount2.getDiscountState())){
                                    waresDiscountPrice=DiscountUtils.getDiscount(storeDiscount2, Double.valueOf(orderDetail.getTotalPrice()));
                                }else{
                                    discount = DiscountUtils.getDiscount(storeDiscount2, Double.valueOf(discount));
                                }

                            }
                            Double price=Double.valueOf(storeDiscountPrice)+Double.valueOf(waresDiscountPrice);

                            /*orderDetail.setWaresPrice(discount);
                            orderDetail.setTotalPrice(String.valueOf(Double.valueOf(discount)*num));*/
                            totalprice += Double.valueOf(String.valueOf(Double.valueOf(orderDetail.getWaresPrice())*num-price));
                        } else {
                            j.setMsg(shopCar.getName() + "已失效");
                            j.setSuccess(false);
                            throw new Exception();
                        }
                    } else {
                        j.setMsg(shopCar.getName() + "购物车已失效");
                        j.setSuccess(false);
                        throw new Exception();

                    }

                    count += 1;
                    orderDetail.setWares(shopCar.getWares());
                    String img = shopCar.getWares().getImg();
                    String[] split = img.split("[,]");
                    orderDetail.setByThree(split[0]);
                    orderDetail.setId("");
                    orderDetails.add(orderDetail);
                    //删除购物车
                    shopCarService.delete(shopCar);
                }
                order.setOrderDetailList(orderDetails);
                order.setCustomer(customer);
                order.setCount(String.valueOf(count));
                order.setNumbers(number);
                order.setOrderState("0");
                order.setStore(store);
                order.setByOne(String.valueOf(totalprice));
                /*order.setTotalPei(String.valueOf(total_pei));*/
                long currentTime = System.currentTimeMillis() + 30 * 60 * 1000;
                Date date = new Date(currentTime);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String nowTime = "";
                nowTime = df.format(date);
                System.out.println(nowTime);
                order.setByTwo(nowTime);
                double add = Common.add(totalprice, Double.valueOf(order.getTotalPei()));
                order.setTotalPrice(String.valueOf(add));
                alipayprice += add;
                //计算总应付价格
                /*total_ying=Common.add(total_ying, add);*/
                orderService.save(order);
            }
            /*System.out.println(1/0);*/
            Map<String, Object> map = new HashMap<>();
            map.put("price", String.valueOf(alipayprice));
            map.put("type", "0");
            map.put("state", status);//支付方式
            map.put("pay_id", customer.getId());
            map.put("order_id", number);
            j = apiPayController.pay(request, response, map);
            j.setSuccess(true);
            j.setMsg("创建订单成功");
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return j;
        }

        return j;
    }



        /*if ("".equals(shopcar_id) || null == shopcar_id) {
            j.setMsg("参数错误");
            j.setSuccess(false);
        }
        *//*Customer customer=customerService.get(customer_id);*//*
        //获取用户的默认地址
        CustomerAddress customerAddress=new CustomerAddress();
        customerAddress.setYesNo("1");
        customerAddress.setCustomer(customer);
        List<CustomerAddress> list=customerAddressService.findList(customerAddress);
        //处理前端传过来的字符串
        shopcar_id=StringEscapeUtils.unescapeHtml4(shopcar_id);
        //转换成json数据此处顺序会变 不过没影响
        JSONArray json=JSON.parseArray(shopcar_id);
        System.out.println(json);
        //1、循环遍历这个数组
        //总订单id
        String orderIds="";
        //应付价格
        double total_ying = 0.0;
        for (int i=0; i < json.size(); i++) {
            //2、把里面的对象转化为JSONObject
            JSONObject job=json.getJSONObject(i);
            //3、把里面想要的参数一个个用.属性名的方式获取到
            System.out.println(job.get("storeId")+":"+job.get("id"));
            String storeId=job.get("storeId").toString();
            //获取商家
            Store store1=storeService.get(storeId);
           String ids =  job.get("id").toString();
            String[] shopCarIds=ids.split(",");
            //创建附表记录
            //总商品价格
            double totalprice=0.0;
            //总商品个数
            int count=0;
            //总配送费
            double total_pei=0.0;

            //附表集合
            List<OrderDetail> orderDetails=new ArrayList<>();
            for (int k=0; k < shopCarIds.length; k++) {
                String s=shopCarIds[k];
                ShopCar shopCar=shopCarService.get(s);

                Double price=Double.valueOf(shopCar.getPrice());
                count+=1;
                totalprice=Common.add(totalprice, price);
                OrderDetail orderDetail=new OrderDetail();
                orderDetail.setNum(shopCar.getNum());
                orderDetail.setTotalPrice(shopCar.getPrice());
                orderDetail.setWares(shopCar.getWares());
                orderDetail.setByOne(shopCar.getGuige());
                String img=shopCar.getWares().getImg();
                String[] split=img.split("[|]");
                orderDetail.setByThree(split[0]);

                orderDetail.setWaresPrice(shopCar.getWaresPrice());
                orderDetail.setId("");
//                if (list.size() == 0) {
//                    //说明此用户没有默认地址
//                    orderDetail.setPeiPrice("0.0");
//                } else {
//                    CustomerAddress customerAddress1=list.get(0);
//                    String id=shopCar.getStore().getId();
//                    Store store=storeService.get(id);
//                    //获取配送费
//                    double pei=getPei(customerAddress1, store);
//                    orderDetail.setPeiPrice(String.valueOf(pei));
//                    total_pei=Common.add(total_pei, pei);
//                }
                orderDetails.add(orderDetail);
            }

            //添加订单主表记录
            Order order=new Order();


            order.setOrderDetailList(orderDetails);
            order.setCustomer(customer);
            order.setCount(String.valueOf(count));
            order.setNumbers(OrderNumbersUtil.getD(UUID.randomUUID().toString()));
            order.setOrderState("0");
            order.setStore(store1);
            order.setByOne(String.valueOf(totalprice));
            order.setTotalPei(String.valueOf(total_pei));
            long currentTime = System.currentTimeMillis() + 30 * 60 * 1000;
            Date date = new Date(currentTime);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTime="";
            nowTime= df.format(date);
            System.out.println(nowTime);
            order.setByTwo(nowTime);
            double add=Common.add(totalprice, total_pei);
            order.setTotalPrice(String.valueOf(add));
            //计算总应付价格
            total_ying=Common.add(total_ying, add);


            orderService.save(order);


            orderIds+=order.getId()+",";

            *//*for (int k=0; k <shopCarIds.length ; k++) {
                String id = shopCarIds[k];
                ShopCar shopCar=shopCarService.get(id);
                shopCarService.delete(shopCar);
            }*//*

        }*/


        /*orderIds=orderIds.substring(0, orderIds.length()-1);
        OrderTotal orderTotal=new OrderTotal();
        orderTotal.setOrderIds(orderIds);
        orderTotal.setTotalPrice( String.valueOf(total_ying));
        orderTotalService.save(orderTotal);*/
    /*j.put("orderIds", orderTotal.getId());*/


    @ApiOperation(notes = "appAddOrderByWares", httpMethod = "POST", value = "用户从商品页面创建订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "wares_id", value = "商品id", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customer_id", value = "用户id", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "store_id", value = "店铺id", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "totalPrice", value = "总商品价格", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "price", value = "单价", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "num", value = "数量", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "guige", value = "规格", required = true, paramType = "query", dataType = "string"),
    })
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    @RequestMapping(value = "/appAddOrderByWares")
    public AjaxJson appAddOrderByWares(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> map) throws Exception {
        AjaxJson j = null;
        try {
            String wares_id = map.get("wares_id") == null ? null : (String) map.get("wares_id");
            String customer_id = map.get("customer_id") == null ? null : (String) map.get("customer_id");
            String store_id = map.get("store_id") == null ? null : (String) map.get("store_id");
            String status = map.get("status") == null ? null : (String) map.get("status");

            ObjectMapper objectMapper1 = new ObjectMapper();
            String jsonInfo = objectMapper1.writeValueAsString(map.get("order"));
            String jsonInfo1 = objectMapper1.writeValueAsString(map.get("orderDetail"));
            Order order = objectMapper1.readValue(jsonInfo, Order.class);
            OrderDetail orderDetail = objectMapper1.readValue(jsonInfo1, OrderDetail.class);
            j = new AjaxJson();

            if (StringUtils.isEmpty(wares_id) || StringUtils.isEmpty(customer_id) || StringUtils.isEmpty(store_id) || StringUtils.isEmpty(status) || order == null || orderDetail == null) {
                j.setMsg("参数缺失");
                j.setSuccess(false);
                return j;
            }
            Wares wares = waresService.get(wares_id);
            Customer customer = customerService.get(customer_id);
            Store store = storeService.get(store_id);

            List<OrderDetail> orderDetails = new ArrayList<>();

            orderDetail.setId("");
        /*orderDetail.setNum(num);
        orderDetail.setWaresPrice(price);*/
            orderDetail.setWares(wares);



        /*orderDetail.setTotalPrice(totalPrice);
        orderDetail.setByOne(guige);*/
            Integer count = Integer.valueOf(orderDetail.getNum());
            if (!StringUtils.isEmpty(orderDetail.getByTwo())) {

                WaresSpecs waresSpecs = waresSpecsService.get(orderDetail.getByTwo());

                /*List<WaresSpecs> waresSpecsList=waresSpecsService.findList(waresSpecs);*/
                if (waresSpecs != null) {
                    //设置订单详情规格信息  红色,14斤
                    orderDetail.setByOne(waresSpecs.getWaresSpecs());
                    //设置订单详情商品规格单价
                    orderDetail.setWaresPrice(waresSpecs.getWaresPrice());
                    //设置订单详情商品规格总价
                    orderDetail.setTotalPrice(String.valueOf(Double.valueOf(waresSpecs.getWaresPrice()) * count));
                    Integer stock = Integer.valueOf(waresSpecs.getWaresStock());


                    if (stock < count) {
                        j.setMsg(wares.getName() + "库存不足");
                        j.setSuccess(false);
                        return j;
                    } else {
                        waresSpecs.setWaresStock(String.valueOf(stock - count));
                        waresSpecsService.save(waresSpecs);
                    }
                } else {
                    j.setMsg(wares.getName() + "规格已失效");
                    j.setSuccess(false);
                    return j;
                }
            } else {
                //商品
                //设置订单详情商品单价
                orderDetail.setWaresPrice(wares.getPrice());
                //设置订单详情商品总价
                orderDetail.setTotalPrice(String.valueOf(Double.valueOf(wares.getPrice()) * count));
                Integer stock = Integer.valueOf(wares.getStock());


                if (stock < count) {
                    j.setMsg(wares.getName() + "库存不足");
                    j.setSuccess(false);
                    return j;
                } else {
                    wares.setStock(String.valueOf(stock - count));
                    waresService.save(wares);
                }
            }
            StoreDiscount storeDiscount=new StoreDiscount();
            storeDiscount.setStore(store);
            storeDiscount.setDiscountType("1");
            storeDiscount.setYesNo("1");
            List<StoreDiscount> storeDiscountList=storeDiscountService.findList(storeDiscount);
            String discount="0";
            if(storeDiscountList.size()>0){
                discount = DiscountUtils.getDiscount(storeDiscountList.get(0), Double.valueOf(orderDetail.getWaresPrice()));

            }else{
                discount=orderDetail.getWaresPrice();
            }
            StoreDiscount storeDiscount1=new StoreDiscount();
            storeDiscount1.setWares(wares);
            storeDiscount1.setDiscountType("2");
            storeDiscount1.setYesNo("1");
            List<StoreDiscount> storeDiscountList1=storeDiscountService.findList(storeDiscount1);
            if(storeDiscountList1.size()>0){
                discount = DiscountUtils.getDiscount(storeDiscountList1.get(0), Double.valueOf(discount));
            }
            orderDetail.setWaresPrice(discount);
            orderDetail.setTotalPrice(String.valueOf(Double.valueOf(discount)*count));

            String img = wares.getImg();
            String[] split = img.split("[,]");
            orderDetail.setByThree(split[0]);

            double pei = 0.0;
            orderDetails.add(orderDetail);

            order.setOrderState("0");
            order.setCustomer(customer);
            order.setStore(store);
            order.setNumbers(OrderNumbersUtil.getD(UUID.randomUUID().toString()));
            order.setCount("1");
            //总商品价格+总配送价格
            order.setByOne(orderDetail.getTotalPrice());
            order.setTotalPei(String.valueOf(pei));
            long currentTime = System.currentTimeMillis() + 30 * 60 * 1000;
            Date date = new Date(currentTime);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTime = "";
            nowTime = df.format(date);
            System.out.println(nowTime);
            order.setByTwo(nowTime);
            double add = Common.add(pei, Double.valueOf(orderDetail.getTotalPrice()));
            order.setTotalPrice(String.valueOf(add));
            order.setOrderDetailList(orderDetails);
            orderService.save(order);

        /*OrderTotal orderTotal=new OrderTotal();
        orderTotal.setOrderIds(order.getId());
        orderTotal.setTotalPrice(String.valueOf(add));
        orderTotalService.save(orderTotal);*/
            Map<String, Object> result = new HashMap<>();
            result.put("price", order.getTotalPrice());
            result.put("type", "0");
            result.put("state", status);//支付方式
            result.put("pay_id", customer.getId());
            result.put("order_id", order.getNumbers());
            j = apiPayController.pay(request, response, result);

            j.setSuccess(true);
            j.setMsg("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return j;
    }
    //获取用户的默认地址
       /* CustomerAddress customerAddress=new CustomerAddress();
        customerAddress.setYesNo("1");
        customerAddress.setCustomer(customer);
        List<CustomerAddress> list=customerAddressService.findList(customerAddress);*/
        /*if(list.size()!=0){
            CustomerAddress customerAddress1=list.get(0);
            order.setCustomerAddress(customerAddress1);
            //计算配送方式
            int peiType=getPeiType(customerAddress1, store);
            if(peiType == 0){
                //配送方式为骑手配送 计算配送费
                pei=getPei(customerAddress1, store);
                order.setPeiType("0");
            }else if(peiType == 1){
                //物流配送
                order.setPeiType("1");
            }
        }else {
            order.setPeiType("1");
        }*/


    @ApiOperation(notes = "appUpdateOrderAddress", httpMethod = "POST", value = "修改订单默认地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_id", value = "订单id", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "address_id", value = "地址id", required = true, paramType = "query", dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appUpdateOrderAddress")
    public AjaxJson appUpdateOrderAddress(String order_id, String address_id) {

        AjaxJson j = new AjaxJson();

        OrderTotal orderTotal = orderTotalService.get(order_id);
        if (null == orderTotal) {
            j.setMsg("参数错误");
            j.setSuccess(false);
            return j;
        }
        String orderIds = orderTotal.getOrderIds();
        String[] split = orderIds.split(",");

        //总价格
        double totalprice = 0.0;
        for (int i = 0; i < split.length; i++) {
            String orderId = split[i];
            Order order = orderService.get(orderId);
            CustomerAddress customerAddress = customerAddressService.get(address_id);
            order.setCustomerAddress(customerAddress);

            Store store = storeService.get(order.getStore().getId());
            double pei = 0.0;
            //计算配送方式
            int peiType = getPeiType(customerAddress, store);
            if (peiType == 0) {
                //配送方式为骑手配送 计算配送费
                pei = getPei(customerAddress, store);
                order.setPeiType("0");
            } else if (peiType == 1) {
                //物流配送
                order.setPeiType("1");
            }
            order.setTotalPei(String.valueOf(pei));
            //获取总订单的总商品价格
            Double byOne = Double.valueOf(order.getByOne());
            //总价格 = 总商品价格+总配送价格
            double add = Common.add(byOne, pei);
            order.setTotalPrice(String.valueOf(add));
            orderService.save(order);
            totalprice = Common.add(totalprice, add);
        }
        orderTotal.setTotalPrice(String.valueOf(totalprice));
        orderTotalService.save(orderTotal);
        j.setErrorCode("666");
        j.setSuccess(true);
        j.setMsg("修改成功");
        return j;
    }


    @ApiOperation(notes = "appGetOrderById", httpMethod = "POST", value = "获取订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_id", value = "订单id", required = true, paramType = "query", dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appGetOrderById")
    public AjaxJson appGetOrderById(String order_id) {
        AjaxJson j = new AjaxJson();
        Order order=orderService.get(order_id);


        j.put("data", order);
        j.setMsg("查询成功");
        j.setSuccess(true);
        return j;
    }


    @ApiOperation(notes = "appUpdateOrderRemarks", httpMethod = "POST", value = "修改订单备注信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_id", value = "订单id", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "remarks", value = "备注信息", required = true, paramType = "query", dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appUpdateOrderRemarks")
    public AjaxJson appUpdateOrderRemarks(String order_id, String remarks) {
        AjaxJson j = new AjaxJson();

        Order order = orderService.get(order_id);
        order.setRemarks(remarks);
        orderService.save(order);
        j.setErrorCode("666");
        j.setSuccess(true);
        j.setMsg("修改成功");
        return j;
    }


    @ApiOperation(notes = "appGetAddressByCustomerId", httpMethod = "GET", value = "获取用户的默认地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customer_id", value = "用户id", required = true, paramType = "query", dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appGetAddressByCustomerId")
    public AjaxJson appGetAddressByCustomerId(String customer_id) {
        AjaxJson j = new AjaxJson();

        if (null == customer_id || "".equals(customer_id)) {
            j.setErrorCode("111");
            j.setSuccess(false);
            j.setMsg("参数错误");
            return j;
        }
        Customer customer = customerService.get(customer_id);
        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setCustomer(customer);
        customerAddress.setYesNo("1");
        List<CustomerAddress> list = customerAddressService.findList(customerAddress);
        if (list.size() > 1) {
            j.setMsg("数据异常,出现两条默认数据");
            j.setSuccess(false);
            j.setErrorCode("777");
        } else if (list.size() == 0) {
            j.setMsg("暂无数据");
            j.setSuccess(false);
            j.setErrorCode("111");
        } else {
            j.setMsg("查询成功");
            j.setSuccess(true);
            j.setErrorCode("666");
            j.put("data", list.get(0));
        }
        return j;
    }


    @ApiOperation(notes = "appDeleteOrder", httpMethod = "POST", value = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_id", value = "订单id", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "0:手动1:系统", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "remarks", value = "取消订单原因", required = true, paramType = "query", dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appDeleteOrder")
    public AjaxJson appDeleteOrder(String order_id, String type, String remarks) {
        AjaxJson j = new AjaxJson();
        Order order = orderService.get(order_id);
        if (type.equals("0")) {
            order.setRemarks(remarks);
        } else if (type.equals("1")) {
            order.setRemarks("超时,系统取消订单");
        }
        //订单状态变为取消
        order.setOrderState("6");
        orderService.save(order);

        j.setMsg("取消订单成功");
        j.setSuccess(true);
        j.setErrorCode("666");

        return j;
    }
    @ApiOperation(notes = "selectExpress", httpMethod = "POST", value = "获取物流信息")

    @ResponseBody
    @RequestMapping(value = "/selectExpress")
    public AjaxJson selectExpress() {
        AjaxJson j = new AjaxJson();
        Express express=new Express();
        List<Express> list = expressService.findList(express);
        j.setMsg("获取物流信息成功");
        j.setSuccess(true);
        j.put("data",list);

        return j;
    }


    @ApiOperation(notes = "appGetCustomerOrder", httpMethod = "POST", value = "获取用户订单记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customer_id", value = "用户id", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "666:全部0:待付款1:待收货2:待评价3:退款售后", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "name", value = "单号或商品名称", required = true, paramType = "query", dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appGetCustomerOrder")
    public AjaxJson appGetCustomerOrder(String customer_id, String type,String name,HttpServletRequest request,HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Order order = new Order();
        Customer customer = customerService.get(customer_id);
        order.setCustomer(customer);
        if (!type.equals("666")) {
            order.setOrderState(type);

        }
        if("1".equals(type)){
            order.setStoreState("0");
        }
        if("3".equals(type)){
            order.setOrderState("1");
            order.setStoreState("2");
        }
        if(!StringUtils.isEmpty(name)){
            order.setRemarks(name);
        }
        Page<Order> list = orderService.findPageList(new Page(1,100),order);
        List<Order> orderList=new ArrayList<>();
        for (Order orders:list.getList()){
            Order o=orderService.get(orders.getId());
            orderList.add(o);
        }
       /* List<Order> list = orderService.findList(order);
        List<Order> orderList=new ArrayList<>();
        for (Order orders:list){
            Order o=orderService.get(orders.getId());
            orderList.add(o);
        }

        if("1".equals(type)){
            List<Order> ordersList=new ArrayList<>();

            for (Order orderss:orderList){

                if("0".equals(orderss.getStoreState())||"1".equals(orderss.getStoreState())){
                    ordersList.add(orderss);
                }
            }
            orderList=ordersList;
        }*/
       /* if(!StringUtils.isEmpty(name)){
            List<Order> orderLists=new ArrayList<>();
            for (Order orderss:orderList){
                if(!StringUtils.isEmpty(orderss.getExpressNumbers())){
                    if(orderss.getExpressNumbers().contains(name)){
                        orderLists.add(orderss);
                    }
                }else{
                    for (OrderDetail orderDetail:orderss.getOrderDetailList()){
                        if(orderDetail.getWares().getName().contains(name)){
                            orderLists.add(orderss);
                        }
                    }
                }
            }
            orderList=orderLists;
        }*/

        j.setSuccess(true);
        j.put("data", orderList);

        return j;
    }


    /**
     * 计算配送费  根据用户到商家之间的距离
     *
     * @param customerAddress
     * @param store
     * @return
     */
    private double getPei(CustomerAddress customerAddress, Store store) {

        double pei = 0.0;
        double distance = LocationUtils.getDistance(customerAddress.getLng(), customerAddress.getLat(), store.getLng(), store.getLat());
        double div = Common.div(distance, 1000, 0);
        //先获取初始距离所对应的配送费
        OtherPrice otherPrice = otherPriceService.get("f19f6c40fa4c4666b49a6c3e0fa3c39d");
        String distance1 = otherPrice.getDistance();
        String price = otherPrice.getPrice();
        double mul = Common.mul(Double.valueOf(distance1), 1000);
        BigDecimal bigDecimal = new BigDecimal(distance);
        BigDecimal bigDecimal1 = new BigDecimal(mul);
        int i = bigDecimal.compareTo(bigDecimal1);
        if (i > 0) {
            System.out.println("超出配送范围");
            //先计算超出多少公里
            double sub = Common.sub(div, Double.valueOf(distance1));
            //获取追加配送费
            OtherPrice otherPrice1 = otherPriceService.get("a108eef10a824ee884498a8c63582f8d");
            Double aDouble = Double.valueOf(otherPrice1.getDistance());
            Double aDouble1 = Double.valueOf(otherPrice1.getPrice());
            double div1 = Common.div(sub, aDouble, 0);
            //应追加的配送费金额
            double mul1 = Common.mul(div1, aDouble1);
            //加上初始配送费
            double add = Common.add(Double.valueOf(price), mul1);
            pei = add;
        }
        if (i == 0 || i < 0) {
            System.out.println("在配送范围之内");
            pei = Double.valueOf(price);
        }
        return pei;
    }

    /**
     * 计算配送方式 根据用户到商家之间的距离
     *
     * @param customerAddress
     * @param store
     * @return
     */
    private int getPeiType(CustomerAddress customerAddress, Store store) {

        int pei = 0;
        double distance = LocationUtils.getDistance(customerAddress.getLng(), customerAddress.getLat(), store.getLng(), store.getLat());
        double div = Common.div(distance, 1000, 0);
        //先获取初始距离所对应的配送费
        OtherPrice otherPrice = otherPriceService.get("fa8a1d93565746599e766d88406531f1");
        String distance1 = otherPrice.getDistance();
        double mul = Common.mul(Double.valueOf(distance1), 1000);
        BigDecimal bigDecimal = new BigDecimal(distance);
        BigDecimal bigDecimal1 = new BigDecimal(mul);
        int i = bigDecimal.compareTo(bigDecimal1);
        if (i > 0) {
            System.out.println("超出系统设定公里数");
            pei = 1;
        }
        if (i == 0 || i < 0) {
            System.out.println("在系统设定范围之内");
            pei = 0;
        }
        return pei;
    }

}
