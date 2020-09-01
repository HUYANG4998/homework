package com.jeeplus.modules.shop.api.customer;

import com.alipay.api.domain.Article;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.shop.api.systemutil.LocationUtils;
import com.jeeplus.modules.shop.api.systemutil.MapUtils;
import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.customer.service.CustomerService;


import com.jeeplus.modules.shop.customerasses.entity.CustomerAsses;
import com.jeeplus.modules.shop.customerasses.service.CustomerAssesService;
import com.jeeplus.modules.shop.customerassit.entity.CustomerAssit;
import com.jeeplus.modules.shop.customerassit.service.CustomerAssitService;
import com.jeeplus.modules.shop.customerfollow.entity.CustomerFollow;
import com.jeeplus.modules.shop.customerfollow.service.CustomerFollowService;
import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
import com.jeeplus.modules.shop.storemovie.entity.StoreMovie;
import com.jeeplus.modules.shop.storemovie.service.StoreMovieService;
import com.jeeplus.modules.shop.systemtype.entity.SystemType;
import com.jeeplus.modules.shop.systemtype.service.SystemTypeService;
import com.jeeplus.modules.shop.util.UserUtils;
import com.jeeplus.modules.shop.wares.entity.Wares;
import com.jeeplus.modules.shop.wares.service.WaresService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author lhh
 * @date 2019/12/2 0002
 */
@Api(value="ApiCustomerController",description="App用户端控制器")
@Controller
@CrossOrigin
@RequestMapping(value = "${adminPath}/api/customer")
public class ApiCustomerController extends BaseController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private WaresService waresService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreMovieService storeMovieService;
    @Autowired
    private SystemTypeService systemTypeService;
    @Autowired
    private CustomerFollowService customerFollowService;
    @Autowired
    private CustomerAssitService customerAssitService;
    @Autowired
    private CustomerAssesService customerAssesService;
    @ModelAttribute
    public Customer get(@RequestParam(required=false) String id) {
        Customer entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = customerService.get(id);
        }
        if (entity == null){
            entity = new Customer();
        }
        return entity;
    }


    @ApiOperation(notes = "appGetCustomerById", httpMethod = "POST", value = "根据用户id获取个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "用户id",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appGetCustomerById")
    public AjaxJson appGetCustomerById(Customer customer){
        AjaxJson j=new AjaxJson();
        if(null == customer){
            j.setSuccess(false);
            j.setMsg("暂无此用户");
            j.setErrorCode("111");
        }else {
            j.put("data",customer);
            j.setMsg("查询成功");
            j.setSuccess(true);
            j.setErrorCode("666");
        }
        return  j;
    }

    @ApiOperation(notes = "appUpdateCustomer", httpMethod = "POST", value = "修改个人信息头像名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "用户id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="name",value = "用户名称",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="img",value = "头像",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appUpdateCustomer")
    public AjaxJson appUpdateCustomer(Customer customer){
        AjaxJson j=new AjaxJson();
        if(!StringUtils.isEmpty(customer.getImg())){
            if(!customer.getImg().contains(UserUtils.HTTPIP)){
                customer.setImg(UserUtils.HTTPIP+customer.getImg());
            }

        }

        customerService.save(customer);

        j.setMsg("修改成功");
        j.setSuccess(true);
        j.setErrorCode("666");
        return j;
    }

    @ApiOperation(notes = "appUpdateCustomerPhone", httpMethod = "POST", value = "修改手机号码")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "用户id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="mobile",value = "手机号",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appUpdateCustomerPhone")
    public AjaxJson appUpdateCustomerPhone(Customer customer,String mobile){
        AjaxJson j=new AjaxJson();

        Customer customer1=new Customer();
        customer1.setPhone(mobile);
        List<Customer> list=customerService.findList(customer1);
        if(list.size() == 0){
            customer.setPhone(mobile);
            customerService.save(customer);
            j.setMsg("修改成功");
            j.setSuccess(true);
        }else if(list.size()>=1){
            j.setSuccess(false);
            j.setMsg("此手机号已经存在,请重新输入");
            j.setErrorCode("777");
        }
        return j;
    }


    @ApiOperation(notes = "appCustomerLogin", httpMethod = "POST", value = "用户端手机号登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone",value = "手机号码",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="code",value = "验证码",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appCustomerLogin")
    public AjaxJson appCustomerLogin(String phone,String code){
        AjaxJson j=new AjaxJson();
        Customer customer=new Customer();
        customer.setPhone(phone);
        List<Customer> list=customerService.findList(customer);
        if(!StringUtils.isEmpty(phone)&&!StringUtils.isEmpty(code)){
            String storephone= CacheUtils.get("customer"+phone).toString();
            if(code.equals(storephone)){
                if(list.size()==0){

                    customer.setName(phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                    customer.setImg(" ");
                    customer.setIsUser("0");
                    customerService.save(customer);
                    j.setSuccess(true);
                    j.setMsg("注册成功");
                    j.put("data",customer);
                    /*String token = JwtHelper.createJWT(store.getPhone(), store.getId(), 365 * 24 * 60 * (60 * 1000), "base64security");
                    String tokenRedis = store.getId() + "_" + store.getPhone() + "_mhmm_User";
                    CacheUtils.put(tokenRedis, token);
                    j.put("token",token);*/
                }else if(list.size()==1){
                    //登陆
                    j.setMsg("登陆成功");
                    j.setSuccess(true);
                    j.put("data",list.get(0));

                    /*String token = JwtHelper.createJWT(store1.getPhone(), store1.getId(), 365 * 24 * 60 * (60 * 1000), "base64security");
                    String tokenRedis = store1.getId() + "_" + store1.getPhone() + "_mhmm_User";
                    CacheUtils.put(tokenRedis, token);
                    j.put("token",token);*/
                }else {
                    j.setMsg("登陆失败,账号异常");
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





    @ApiOperation(notes = "appCustomerSanLogin", httpMethod = "POST", value = "用户端第三方登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name="type",value = "1:微信2:qq",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="wei_openid",value = "微信openid",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="qq_id",value = "qqopenid",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="name",value = "名称",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="img",value = "头像",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appCustomerSanLogin")
    public AjaxJson appCustomerSanLogin(String type,String wei_openid,String qq_id,String name,String img){
        AjaxJson j=new AjaxJson();
        Customer customer=new Customer();
        if(type.equals("1")){
            //微信
            customer.setWeiId(wei_openid);
        }else if(type.equals("2")){
            //qq
            customer.setQqId(qq_id);
        }
        List<Customer> list=customerService.findList(customer);
        if(list.size() == 0){
            //注册
            customer.setName(name);
            customer.setImg(img);
            customer.setIsUser("0");
            customerService.save(customer);
            j.setSuccess(true);
            j.setMsg("注册成功");
            j.put("data",customer);
        }else {
            //登陆
            j.setMsg("登陆成功");
            j.setSuccess(true);
            j.put("data",list.get(0));
            /*Store store1 = list.get(0);
            String token = JwtHelper.createJWT(store1.getPhone(), store1.getId(), 365 * 24 * 60 * (60 * 1000), "base64security");
            String tokenRedis = store1.getId() + "_" + store1.getPhone() + "_mhmm_User";
            CacheUtils.put(tokenRedis, token);
            j.put("token",token);*/
        }
        return j;
    }




    @ApiOperation(notes = "appCustomerBangPhone", httpMethod = "POST", value = "用户端手机号绑定")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone",value = "手机号码",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="id",value = "用户id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="code",value = "验证码",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appCustomerBangPhone")
    public AjaxJson appCustomerBangPhone(String phone, String id,String code){
        AjaxJson j=new AjaxJson();
        String cacheCode = (String)CacheUtils.get("customer" + phone);
        if(code.equals(cacheCode)){
            Customer customer=new Customer();
            customer.setPhone(phone);
            List<Customer> list = customerService.findList(customer);
            if(list.size()==0){
                Customer customer1=customerService.get(id);
                customer1.setPhone(phone);
                customerService.save(customer1);
                j.setMsg("绑定成功");
                j.setSuccess(true);
                j.put("data",customer1);
            }else{
                j.setMsg("手机号已被绑定");
                j.setSuccess(true);
            }

        }else{
            j.setMsg("验证码错误");
            j.setSuccess(false);
        }

        return j;
    }



    @ApiOperation(notes = "appGetSystemType", httpMethod = "POST", value = "获取系统分类")
    @ApiImplicitParams({

    })
    @ResponseBody
    @RequestMapping(value = "/appGetSystemType")
    public AjaxJson appGetSystemType(){
        AjaxJson j=new AjaxJson();
        List<SystemType> list=systemTypeService.findList(new SystemType());
        SystemType systemType=list.get(0);
        Wares wares=new Wares();
        wares.setByOne(systemType.getId());
        wares.setIsShang("1");
        List<Wares> list1=waresService.findList(wares);


        systemType.setWaresList(list1);
        if(list1.size()==0){
            j.setMsg("此分类下暂无商品");
            j.setSuccess(true);
            j.setErrorCode("111");
        }else{
            j.put("one",systemType);
            j.setSuccess(true);
            j.setErrorCode("666");
            j.setMsg("查询成功");
        }
        j.put("all",list);
        return j;
    }

    @ApiOperation(notes = "appGetWaresBySystemType", httpMethod = "POST", value = "根据系统分类获取商品/根据商品名称搜索商品(复合)")
    @ApiImplicitParams({
            @ApiImplicitParam(name="address",value = "地址",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="type_id",value = "系统分类id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="type",value = "0:仅仅根据分类查询商品1:根据商品名称查询",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="name",value = "根据商品名称查询的时候传",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="pageNum",value = "第几页",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="pageSize",value = "每页条数",required = true, paramType = "query",dataType = "string")

    })
    @ResponseBody
    @RequestMapping(value = "/appGetWaresBySystemType")
    public AjaxJson appGetWaresBySystemType(String type_id,String type,String name){
        AjaxJson j=new AjaxJson();
        if("".equals(type_id) || null == type_id){
            j.setMsg("参数错误");
            j.setSuccess(false);
            return j;
        }
        Wares wares=new Wares();
        wares.setByOne(type_id);
        wares.setIsShang("1");
        if(type.equals("1")){
            wares.setName(name);
        }
        List<Wares> list1=waresService.findList(wares);
        Iterator<Wares> iterator = list1.iterator();
        while(iterator.hasNext()) {
            Wares ware = iterator.next();

            if("0".equals(ware.getStore().getIsShen())){
                iterator.remove();
            }
        }
        j.setSuccess(true);
        j.put("data",list1);
      /*  if(list1.size()==0){
            j.setMsg("此分类下暂无商品");
            j.setSuccess(false);
            j.setErrorCode("111");
        }else{
            j.setSuccess(true);
            j.put("data",list1);
            j.setErrorCode("666");
            j.setMsg("查询成功");
        }*/
        return j;
    }


    @ApiOperation(notes = "appCustomerFollw", httpMethod = "POST", value = "关注/取消关注(复合)")
    @ApiImplicitParams({
            @ApiImplicitParam(name="customer_id",value = "用户id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="store_id",value = "店铺id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="type",value = "0:关注1:取消关注",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appCustomerFollw")
    public AjaxJson appCustomerFollw(String customer_id,String store_id,String type){
        AjaxJson j=new AjaxJson();
        if(type.equals("") || null == type){
            j.setSuccess(false);
            j.setMsg("参数错误");
            j.setErrorCode("111");
            return j;
        }
        Customer customer=customerService.get(customer_id);
        Store store=storeService.get(store_id);
        CustomerFollow customerFollow=new CustomerFollow();
        customerFollow.setCustomer(customer);
        customerFollow.setStore(store);
        if(type.equals("0")){
            //关注
            customerFollowService.save(customerFollow);
            String fenNum=store.getFenNum();
            int i = Integer.parseInt(fenNum)+1;
            store.setFenNum(String.valueOf(i));
            storeService.save(store);
            j.setMsg("关注成功");
            j.setSuccess(true);
            j.setErrorCode("666");
            j.put("data",store.getFenNum());
        }else if(type.equals("1")){
            //取消关注
            List<CustomerFollow> list=customerFollowService.findList(customerFollow);
            CustomerFollow customerFollow1=list.get(0);
            customerFollowService.delete(customerFollow1);
            String fenNum=store.getFenNum();
            int i = Integer.parseInt(fenNum)-1;
            store.setFenNum(String.valueOf(i));
            storeService.save(store);
            j.put("data",store.getFenNum());
            j.setSuccess(true);
            j.setMsg("取消关注成功");
            j.setErrorCode("666");
        }
        return j;
    }
    @ApiOperation(notes = "appCustomerAssit", httpMethod = "POST", value = "点赞/取消点赞(复合)")
    @ApiImplicitParams({
            @ApiImplicitParam(name="customer_id",value = "用户id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="movie_id",value = "商家动态id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="type",value = "0:点赞1:取消点赞",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appCustomerAssit")
    public AjaxJson appCustomerAssit(String customer_id,String movie_id,String type){
        AjaxJson j=new AjaxJson();
        if(type.equals("") || null == type){
            j.setSuccess(false);
            j.setMsg("参数错误");
            j.setErrorCode("111");
            return j;
        }
        Customer customer=customerService.get(customer_id);
        StoreMovie storeMovie=storeMovieService.get(movie_id);

        CustomerAssit customerAssit=new CustomerAssit();
        customerAssit.setCustomer(customer);
        customerAssit.setMovie(storeMovie);
        if(type.equals("0")){
            //点赞
            customerAssitService.save(customerAssit);

            storeMovie.setDianNum(String.valueOf(Integer.parseInt(storeMovie.getDianNum())+1));
            storeMovieService.save(storeMovie);
            j.setMsg("点赞成功");
            j.setSuccess(true);
            j.setErrorCode("666");
            j.put("data",storeMovie.getDianNum());
        }else if(type.equals("1")){
            //取消点赞
            List<CustomerAssit> list=customerAssitService.findList(customerAssit);
            CustomerAssit customerAssit1=list.get(0);
            customerAssitService.delete(customerAssit1);

            storeMovie.setDianNum(String.valueOf(Integer.parseInt(storeMovie.getDianNum())-1));
            storeMovieService.save(storeMovie);
            j.setSuccess(true);
            j.setMsg("取消点赞成功");
            j.setErrorCode("666");
            j.put("data",storeMovie.getDianNum());
        }
        return j;
    }


    @ApiOperation(notes = "appGetCustomerFollowAssit", httpMethod = "POST", value = "获取我的关注/获取我的点赞(复合)")
    @ApiImplicitParams({
            @ApiImplicitParam(name="customer_id",value = "用户id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="type",value = "0:获取我的关注:获取我的点赞",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appGetCustomerFollowAssit")
    public AjaxJson appGetCustomerFollowAssit(String customer_id,String type){
        AjaxJson j=new AjaxJson();
        if(type.equals("") || null == type){
            j.setSuccess(false);
            j.setMsg("参数错误");
            j.setErrorCode("111");
            return j;
        }
        Customer customer=customerService.get(customer_id);
        if(type.equals("0")){
            //获取我的关注
            CustomerFollow customerFollow=new CustomerFollow();
            customerFollow.setCustomer(customer);
            List<CustomerFollow> list=customerFollowService.findList(customerFollow);
            j.setMsg("查询我的关注成功");
            j.setSuccess(true);
            j.put("data",list);
        }else if(type.equals("1")){
            //获取我的点赞
            CustomerAssit customerAssit=new CustomerAssit();
            customerAssit.setCustomer(customer);
            List<CustomerAssit> list=customerAssitService.findList(customerAssit);
            j.setMsg("查询我的点赞成功");
            j.setSuccess(true);
            j.put("data",list);

        }
        return j;
    }

    @ApiOperation(notes = "getStoreAndWaresOrVideo", httpMethod = "POST", value = "搜索店铺及旗下商品和视频")
    @ApiImplicitParams({
            @ApiImplicitParam(name="address",value = "地址",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="type",value = "(0:查商品1:查视频)",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="name",value = "名称",required = false, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="pageNo",value = "页数",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="pageSize",value = "每页数据数",required = true, paramType = "query",dataType = "string")

    })
    @ResponseBody
    @RequestMapping(value = "/getStoreAndWaresOrVideo")
    public AjaxJson getStoreAndWaresOrVideo(@RequestBody Map<String,Object> map){
        AjaxJson j=new AjaxJson();

        Integer pageNo=map.get("pageNo")==null?null:(Integer)map.get("pageNo");
        Integer pageSize=map.get("pageSize")==null?null:(Integer)map.get("pageSize");
        String name=map.get("name")==null?null:(String)map.get("name");
        String type=map.get("type")==null?null:(String)map.get("type");
        String address=map.get("address")==null?null:(String)map.get("address");
        if(type.equals("0")){
            //查商家和商品
            Store store=new Store();
            //先查询出所有  已经审核过的 店铺
            if(!StringUtils.isEmpty(address)){
                store.setAddress(address);
            }
            if(!StringUtils.isEmpty(name)){
                store.setName(name);
            }
            store.setIsShen("1");
            Page<Store> list=storeService.getStoreAndWares(new Page<Store>(pageNo,pageSize),store);
            List<Map<String,Object>> listMap=new ArrayList<>();
            if(list.getList().size()>0){
                for (Store s:list.getList()) {
                    Map<String,Object> result=new HashMap<>();
                    Store store1=storeService.get(s.getId());
                    result.put("store",store1);
                    List<Wares> listWares=waresService.getWaresByStoreIdAndName(store1.getId(),name);
                    for (Wares wares:listWares){
                        String img = wares.getImg();
                        wares.setImg(img.split(",")[0]);;
                    }
                    result.put("wares",listWares);
                    listMap.add(result);
                }
            }

            j.put("data",listMap);
            j.put("total",list.getTotalPage());
            j.setSuccess(true);
        } else if(type.equals("1")){
            //查视频
            StoreMovie storeMovie=new StoreMovie();
            if(!StringUtils.isEmpty(name)){
                storeMovie.setTitle(name);
            }
            if(!StringUtils.isEmpty(address)){
                Store store =new Store();
                store.setAddress(address);
                storeMovie.setStore(store);
            }
            Page<StoreMovie> page=storeMovieService.findPage(new Page<StoreMovie>(pageNo, pageSize), storeMovie);
            j.put("data",page.getList());
            j.put("total",page.getTotalPage());
            j.setSuccess(true);

        }


        return j;
    }



    @ApiOperation(notes = "appGetWaresByLngLat", httpMethod = "POST", value = "用户端首页查询方法（复合型接口）")
    @ApiImplicitParams({
            @ApiImplicitParam(name="address",value = "地址",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="type",value = "(0:查商品1:查视频2:查关注)",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="customer_id",value = "用户id(查关注的时候传)",required = false, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="name",value = "商品名称或者店铺名称（查询的时候传 不是查询的时候传空值）",required = false, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="pageNo",value = "页数",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="pageSize",value = "每页数据数",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="sysType",value = "平台分类",required = true, paramType = "query",dataType = "string")

    })
    @ResponseBody
    @RequestMapping(value = "/appGetWaresByLngLat")
    public AjaxJson appGetWaresByLngLat(String address, String type, String customer_id,String name, HttpServletRequest request, HttpServletResponse response){
        AjaxJson j=new AjaxJson();
        if("".equals(type) || null == type){
            j.setMsg("参数错误");
            j.setSuccess(false);
            j.setErrorCode("111");
            return  j;
        }
        Integer pageNo=request.getParameter("pageNo")==null?1:Integer.valueOf(request.getParameter("pageNo"));
        Integer pageSize=request.getParameter("pageSize")==null?10:Integer.valueOf(request.getParameter("pageSize"));

        String sysType=request.getParameter("sysType")==null?null:String.valueOf(request.getParameter("sysType"));
        if(type.equals("0") || type.equals("1")){
            Store store1=new Store();
            //先查询出所有  已经审核过的 店铺
            if(!StringUtils.isEmpty(address)){
                store1.setAddress(address);
            }
            store1.setIsShen("1");
            List<Store> list=storeService.findList(store1);
            List<String> idList=new ArrayList<>();
            for (Store s:list) {
                    idList.add(s.getId());
            }
            if(idList.size()>0){
                //查商品
                Wares wares=new Wares();
                wares.setIsShang("1");
                wares.setIdList(idList);
                if(!StringUtils.isEmpty(name)){
                    wares.setName(name);
                }
                if(!StringUtils.isEmpty(sysType)){
                    wares.setByOne(sysType);
                }
                Page<Wares> page=waresService.findPages(new Page<Wares>(pageNo, pageSize), wares);
                Map<String, Object> bootstrapData=getBootstrapData(page);
                j.put("data",bootstrapData);
                j.setSuccess(true);
            }else {
                j.setSuccess(true);
                j.setMsg("暂无店铺信息");
                j.put("data",new ArrayList<>());
            }

        } else if(type.equals("2")){
            //查视频
            StoreMovie storeMovie=new StoreMovie();
            if(!StringUtils.isEmpty(name)){
                Store store=new Store();
                store.setName(name);
                storeMovie.setStore(store);
            }
            Page<StoreMovie> page=storeMovieService.findPage(new Page<StoreMovie>(pageNo, pageSize), storeMovie);
            Map<String, Object> bootstrapData=getBootstrapData(page);
            j.put("data",bootstrapData);
            j.setSuccess(true);
            j.setErrorCode("666");

        }else if(type.equals("3")){
            //查关注
            Customer customer=customerService.get(customer_id);
            CustomerFollow customerFollow=new CustomerFollow();
            customerFollow.setCustomer(customer);
            Page<CustomerFollow> page = customerFollowService.findPage(new Page<CustomerFollow>(pageNo, pageSize), customerFollow);
            List<String> idList=new ArrayList<>();
            for (CustomerFollow c:page.getList()) {
                idList.add(c.getStore().getId());
            }
            if(idList.size()>0){
                StoreMovie storeMovie=new StoreMovie();
                storeMovie.setIdList(idList);

                Page<StoreMovie> list=storeMovieService.findPageFocus(new Page<StoreMovie>(pageNo,pageSize),storeMovie);
                j.put("data",list);
            }else{
                j.put("data",new ArrayList<>());
            }
            j.setSuccess(true);
            j.setMsg("查询成功");

        }


        return j;
    }


    @ApiOperation(notes = "appGetCustomerAsses", httpMethod = "POST", value = "获取商品评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name="wares_id",value = "商品id",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appGetCustomerAsses")
    public AjaxJson appGetCustomerAsses(String wares_id){

        AjaxJson j=new AjaxJson();

        if(null == wares_id ||"".equals(wares_id)){
            j.setMsg("参数错误");
            j.setSuccess(false);
            j.setErrorCode("111");
            return j;
        }

        Wares wares=waresService.get(wares_id);
        CustomerAsses customerAsses=new CustomerAsses();
        customerAsses.setWares(wares);
        List<CustomerAsses> list=customerAssesService.findList(customerAsses);
        if(list.size() ==0){
            j.setErrorCode("777");
            j.setSuccess(false);
            j.setMsg("暂无数据");
        }else {
            j.setSuccess(true);
            j.setErrorCode("666");
            j.put("data",list);
        }
        return j;
    }


    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }


}
