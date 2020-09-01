package com.jeeplus.modules.shop.api.store;

import cn.hutool.core.date.DateUtil;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;

import com.jeeplus.modules.shop.api.earn.entity.Earn;
import com.jeeplus.modules.shop.api.earn.service.EarnService;
import com.jeeplus.modules.shop.api.pay.ApiPayController;
import com.jeeplus.modules.shop.api.payaccount.entity.PayAccount;
import com.jeeplus.modules.shop.api.payaccount.service.PayAccountService;
import com.jeeplus.modules.shop.api.payutils.alipay.AlipayTranfer;
import com.jeeplus.modules.shop.api.payutils.wxpay.TransferController;
import com.jeeplus.modules.shop.config.MyStaticMap;
import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.customer.service.CustomerService;
import com.jeeplus.modules.shop.customerfollow.entity.CustomerFollow;
import com.jeeplus.modules.shop.customerfollow.service.CustomerFollowService;
import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.order.service.OrderService;
import com.jeeplus.modules.shop.rider.entity.Rider;
import com.jeeplus.modules.shop.rider.service.RiderService;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
import com.jeeplus.modules.shop.util.JwtHelper;
import com.jeeplus.modules.shop.util.UserUtils;
import com.jeeplus.modules.sys.entity.Log;
import com.jeeplus.modules.sys.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuan
 * @date 2019/11/26 0026
 */

@Api(value="ApiStoreController",description="App商家端控制器")
@Controller
@CrossOrigin
@RequestMapping(value = "${adminPath}/api/store")
public class ApiStoreController extends BaseController {

    @Autowired
    private StoreService storeService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerFollowService customerFollowService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private LogService logService;
    @Autowired
    private ApiPayController apiPayController;
    @Autowired
    private PayAccountService payAccountService;
    @Autowired
    private EarnService earnService;
    @Autowired
    private TransferController transferController;
    @Autowired
    private RiderService riderService;

    private MyStaticMap redisUtils = new MyStaticMap();

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

    @ApiOperation(notes = "appGetStoreDetail", httpMethod = "GET", value = "获取商家端信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "商家id",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appGetStoreDetail")
    public AjaxJson appGetStoreDetail(Store store){
        AjaxJson j=new AjaxJson();

        List<Order> list = orderService.findListOrder(DateUtil.today(),store.getId());
        Map<String,Object> map=new HashMap<>();
        map.put("ordersize",list.size());
        Double d=0.0;
        for (Order orders:list){
            d+=Double.valueOf(orders.getByOne());
        }

        map.put("ordermoney",d);
        /*Log log=new Log();
        log.setParams(store.getId());
        String today=DateUtil.today();
        log.setBeginDate(DateUtil.parse(today+" 00:00:00"));
        log.setEndDate(DateUtil.parse(today+" 23:59:59"));*/
        /*log.setCreateDate(DateUtil.parse("2020-04-24","yyyy-MM-dd"));*/
        Integer count = logService.selectByParamsAndCreateDate(store.getId(), DateUtil.today());
        map.put("visitor",count);
        if (store.getPhone() == null){
            j.setSuccess(false);
            j.setMsg("暂无此商户数据");
            j.setErrorCode("111");
        }else {
            j.setSuccess(true);
            j.setMsg("查询成功");
            j.setErrorCode("666");
            map.put("store",store);


        }
        j.put("data",map);
        return j;
    }

    @ApiOperation(notes = "appGetStoreByCustomerId", httpMethod = "GET", value = "获取商家端是否关注信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "商家id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="customer_id",value = "用户id",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appGetStoreByCustomerId")
    public AjaxJson appGetStoreByCustomerId(Store store,String customer_id){
        AjaxJson j=new AjaxJson();
        if(null == customer_id ||"".equals(customer_id)){
            j.setErrorCode("111");
            j.setSuccess(false);
            j.setMsg("缺少用户id参数错误");
            return j;
        }
        //判断用户是否已经关注过此店铺
        Customer customer=customerService.get(customer_id);
        CustomerFollow customerFollow=new CustomerFollow();
        customerFollow.setCustomer(customer);
        customerFollow.setStore(store);
        List<CustomerFollow> list1=customerFollowService.findList(customerFollow);
        if(list1.size() == 0){
            store.setIsFllow("0");
        }else {
            store.setIsFllow("1");
        }
        if (store.getPhone() == null){
            j.setSuccess(false);
            j.setMsg("暂无此商户数据");
            j.setErrorCode("111");
        }else {
            j.setSuccess(true);
            j.setMsg("查询成功");
            j.setErrorCode("666");
            j.put("data",store);
        }
        return j;
    }

    @ApiOperation(notes = "appStoreLogin", httpMethod = "POST", value = "商家端手机号登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone",value = "手机号码",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="code",value = "验证码",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appStoreLogin")
    public AjaxJson appStoreLogin(String phone,String code){
        AjaxJson j=new AjaxJson();
        Store store=new Store();
        store.setPhone(phone);
        List<Store> list=storeService.findList(store);
        if(!StringUtils.isEmpty(phone)&&!StringUtils.isEmpty(code)){
            String storephone=CacheUtils.get("store"+phone).toString();
            if(code.equals(storephone)){
                if(list.size()==0){
                    //注册
                    store.setImg("");
                    store.setIsRen("0");
                    store.setIsShen("0");
                    store.setMoney("0");
                    store.setBaoMoney("0");
                    store.setNum(0);
                    store.setStarNum("5");
                    store.setName(phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                    store.setIsUser("1");
                    storeService.save(store);
                    j.setSuccess(true);
                    j.setErrorCode("888");
                    j.setMsg("注册成功");

                    /*String token = JwtHelper.createJWT(store.getPhone(), store.getId(), 365 * 24 * 60 * (60 * 1000), "base64security");
                    String tokenRedis = store.getId() + "_" + store.getPhone() + "_mhmm_User";
                    CacheUtils.put(tokenRedis, token);
                    j.put("token",token);*/
                    j.put("data",store);


                }else if(list.size()==1){
                    Store store1 = list.get(0);
                    //登陆
                    j.setMsg("登陆成功");
                    j.setErrorCode("666");
                    j.setSuccess(true);


                    /*String token = JwtHelper.createJWT(store1.getPhone(), store1.getId(), 365 * 24 * 60 * (60 * 1000), "base64security");
                    String tokenRedis = store1.getId() + "_" + store1.getPhone() + "_mhmm_User";
                    CacheUtils.put(tokenRedis, token);
                    j.put("token",token);*/
                    j.put("data",store1);
                }else {
                    j.setMsg("登陆失败,账号异常");
                    j.setErrorCode("777");
                    j.setSuccess(false);
                }
            }else{
                j.setMsg("验证码错误");
                j.setErrorCode("777");
                j.setSuccess(false);
            }

        }else{
            j.setMsg("登陆失败,账号异常");
            j.setErrorCode("777");
            j.setSuccess(false);
        }

        return j;
    }

    @ApiOperation(notes = "appStoreSanLogin", httpMethod = "POST", value = "商家端第三方登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name="type",value = "1:微信2:qq",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="wei_openid",value = "微信openid",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="qq_id",value = "qqopenid",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="name",value = "名称",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="img",value = "头像",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appStoreSanLogin")
    public AjaxJson appStoreSanLogin(String type,String wei_openid,String qq_id,String name,String img){
        AjaxJson j=new AjaxJson();
        Store store=new Store();
        if(type.equals("1")){
            //微信
            store.setWeiOpenId(wei_openid);
        }else if(type.equals("2")){
            //qq
            store.setQqId(qq_id);
        }
        List<Store> list=storeService.findList(store);
        if(list.size() == 0){
            //注册
            store.setName(name);//昵称
            store.setImg(img);//头像
            store.setIsShen("0");//是否审核
            store.setIsRen("0");//是否认证
            store.setMoney("0");//余额
            store.setBaoMoney("0");//保证金
            store.setPhone("");//手机号
            store.setFenNum("0");//粉丝数
            store.setStarNum("5");//星级数
            store.setIsUser("1");//身份
            storeService.save(store);
            j.setSuccess(true);
            j.setMsg("注册成功");
            j.put("data",store);
        }else {
            //登陆
            j.setMsg("登陆成功");
            j.setSuccess(true);
            /*Store store1 = list.get(0);
            String token = JwtHelper.createJWT(store1.getPhone(), store1.getId(), 365 * 24 * 60 * (60 * 1000), "base64security");
            String tokenRedis = store1.getId() + "_" + store1.getPhone() + "_mhmm_User";
            CacheUtils.put(tokenRedis, token);
            j.put("token",token);*/
            j.put("data",list.get(0));

        }
        return j;
    }



    @ApiOperation(notes = "appStoreBangPhone", httpMethod = "POST", value = "商家端手机号绑定")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone",value = "手机号码",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="id",value = "商家id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="code",value = "验证码",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appStoreBangPhone")
    public AjaxJson appStoreBangPhone(String phone,String id,String code){
        AjaxJson j=new AjaxJson();
        int cacheCode = (int)CacheUtils.get("store" + phone);
        if(Integer.valueOf(code)==cacheCode){
            Store store=new Store();
            store.setPhone(phone);
            List<Store> list = storeService.findList(store);
            if(list.size()==0){
                Store store1 = storeService.get(id);
                store1.setPhone(phone);
                storeService.save(store1);
                j.setMsg("绑定成功");
                j.setSuccess(true);
                j.put("data",store1);
            }else{
                j.setMsg("手机号已被绑定");
                j.setSuccess(false);
            }

        }else{
            j.setMsg("验证码错误");
            j.setSuccess(false);
        }
        return j;
    }




    @ApiOperation(notes = "appStoreRen", httpMethod = "POST", value = "商家端认证资料")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "商家id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="name",value = "商家名称",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="realName",value = "真实姓名",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="card",value = "身份证号",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="cardZ",value = "身份证正面",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="cardF",value = "身份证反面",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="cardS",value = "身份证手持",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="address",value = "地址",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="addressDetail",value = "详细地址",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="lng",value = "商家经度",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="lat",value = "商家纬度",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="menImg",value = "门头照片",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="neiImg",value = "内部照片",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="yingImg",value = "营业执照",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="pinImg",value = "品牌授权书",required = false, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="hangImg",value = "行业执照",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="img",value = "头像（修改资料才传）",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="phone",value = "手机号码（修改资料才传）",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="type",value = "1:认证资料2:修改资料",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appStoreRen")
    public AjaxJson appStoreRen(Store store, HttpServletRequest request){

        AjaxJson j=new AjaxJson();

        String type = request.getParameter("type");
        if(type.equals("1")){

            Store s=storeService.get(store.getId());


                if("1".equals(s.getIsShen())){
                    store.setIsShen("0");
                }
                store.setIsRen("1");
                if(!StringUtils.isEmpty(store.getImg())){
                    if(!store.getImg().contains(UserUtils.HTTPIP)){
                        store.setImg(UserUtils.HTTPIP+store.getImg());
                    }
                }

                storeService.save(store);

                j.setMsg("认证成功");

                j.setSuccess(true);
                j.put("data",store);

        }else  if (type.equals("2")){

            String phone=request.getParameter("phone");
            String id=request.getParameter("id");
            Store store2=storeService.get(id);
            //判断传过来的手机是否和原手机号相同
            if(!store2.getPhone().equals(phone)){
                Store store1=new Store();
                store1.setPhone(phone);
                List<Store> list=storeService.findList(store1);
                if(list.size()>1){
                    j.setErrorCode("777");
                    j.setSuccess(false);
                    j.setMsg("此手机号已经存在!");

                }
            }
            store.setIsShen("0");
            storeService.save(store);
            j.setMsg("修改成功");
            j.setSuccess(true);
            j.put("data",store);
        }
        return j;
    }

    @ApiOperation(notes = "payStoreBaoMoney", httpMethod = "POST", value = "商家支付保证金")
    @ApiImplicitParams({
            @ApiImplicitParam(name="money",value = "保证金额",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="status",value = "0支付宝1微信",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="userId",value = "商家id",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/payStoreBaoMoney")
    public AjaxJson payStoreBaoMoney(String money, String status, String userId, HttpServletRequest request, HttpServletResponse response){
        AjaxJson j=new AjaxJson();
        if(!StringUtils.isEmpty(money)&&!StringUtils.isEmpty(status)&&!StringUtils.isEmpty(userId)){
            Map<String, Object> result = new HashMap<>();
            result.put("price", money);
            result.put("type", "1");
            result.put("state", status);//支付方式
            result.put("pay_id", userId);
            j = apiPayController.pay(request, response, result);
        }else{
            j.setSuccess(false);
            j.setMsg("参数错误");
        }
        j.setSuccess(true);
        return j;
    }
    @ApiOperation(notes = "withDrawal", httpMethod = "POST", value = "商家提现")
    @ApiImplicitParams({
            @ApiImplicitParam(name="money",value = "提现金额",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="status",value = "0支付宝1微信",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="userId",value = "商家id",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/withDrawal")
    public AjaxJson withDrawal(String money, String status, String userId, HttpServletRequest request, HttpServletResponse response){
        AjaxJson j=new AjaxJson();

        if(!StringUtils.isEmpty(money)&&!StringUtils.isEmpty(status)&&!StringUtils.isEmpty(userId)){
            Store store=storeService.get(userId);

            PayAccount payAccount=payAccountService.selectPayAccountByStatus(userId,status);
            boolean success=false;
            if("1".equals(status)){
                //微信提现
                if(payAccount!=null&&Double.valueOf(store.getMoney())>=Double.valueOf(money)){
                    j = transferController.transferPay(request,response,payAccount.getOpenId(), Double.valueOf(money),userId);

                }else{
                    j.setSuccess(false);
                    j.setMsg("提现失败");
                }

            }else if("0".equals(status)){

                //支付宝提现
                if(payAccount!=null&&Double.valueOf(store.getMoney())>=Double.valueOf(money)){
                    success = AlipayTranfer.AlipayTransfer("", Double.valueOf(money), payAccount.getAccount());
                    if(success){
                        //提现成功
                        //加提现明细
                        Double mon = Double.valueOf(money);
                        Double smoney = Double.valueOf(store.getMoney());
                        if(mon<=smoney){
                            //减余额
                            store.setMoney(String.valueOf(smoney-mon));
                            storeService.save(store);
                            //收益记录
                            Earn earn=new Earn("提现",money,userId,"0",DateUtil.now());
                            earnService.insertEarn(earn);

                            j.setSuccess(true);
                            j.setMsg("提现成功");
                        }else{
                            j.setSuccess(false);
                            j.setMsg("余额不足");
                        }

                    }else{
                        j.setSuccess(false);
                        j.setMsg("提现失败");
                    }
                }else{
                    j.setSuccess(false);
                    j.setMsg("提现失败");
                }
            }

        }else{
            j.setSuccess(false);
            j.setMsg("参数错误");
        }

        return j;
    }

    @ApiOperation(notes = "riderWithDrawal", httpMethod = "POST", value = "骑手提现")
    @ApiImplicitParams({
            @ApiImplicitParam(name="money",value = "提现金额",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="status",value = "0支付宝1微信",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="userId",value = "骑手id",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/riderWithDrawal")
    public AjaxJson riderWithDrawal(String money, String status, String userId, HttpServletRequest request, HttpServletResponse response){
        AjaxJson j=new AjaxJson();

        if(!StringUtils.isEmpty(money)&&!StringUtils.isEmpty(status)&&!StringUtils.isEmpty(userId)){
            Rider rider=riderService.get(userId);

            PayAccount payAccount=payAccountService.selectPayAccountByStatus(userId,status);
            boolean success=false;
            if("1".equals(status)){
                //微信提现
                if(payAccount!=null&&Double.valueOf(rider.getMoney())>=Double.valueOf(money)){
                    j = transferController.transferPay(request,response,payAccount.getOpenId(), Double.valueOf(money),userId);

                }else{
                    j.setSuccess(false);
                    j.setMsg("提现失败");
                }

            }else if("0".equals(status)){

                //支付宝提现
                if(payAccount!=null&&Double.valueOf(rider.getMoney())>=Double.valueOf(money)){
                    success = AlipayTranfer.AlipayTransfer("", Double.valueOf(money), payAccount.getAccount());
                    if(success){
                        //提现成功
                        //加提现明细
                        Double mon = Double.valueOf(money);
                        Double smoney = Double.valueOf(rider.getMoney());
                        if(mon>=smoney){
                            //减余额
                            rider.setMoney(String.valueOf(Double.valueOf(rider.getMoney())-Double.valueOf(money)));
                            riderService.save(rider);
                            //收益记录
                            Earn earn=new Earn("提现",money,userId,"0",DateUtil.now());
                            earnService.insertEarn(earn);

                            j.setSuccess(true);
                            j.setMsg("提现成功");
                        }else{
                            j.setSuccess(false);
                            j.setMsg("余额不足");
                        }

                    }else{
                        j.setSuccess(false);
                        j.setMsg("提现失败");
                    }
                }else{
                    j.setSuccess(false);
                    j.setMsg("提现失败");
                }
            }

        }else{
            j.setSuccess(false);
            j.setMsg("参数错误");
        }

        return j;
    }






}
