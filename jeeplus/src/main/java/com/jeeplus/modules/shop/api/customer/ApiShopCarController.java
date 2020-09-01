package com.jeeplus.modules.shop.api.customer;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.monitor.utils.Common;
import com.jeeplus.modules.shop.api.systemutil.ShopCarUtil;
import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.customer.service.CustomerService;

import com.jeeplus.modules.shop.shopcar.entity.ShopCar;
import com.jeeplus.modules.shop.shopcar.service.ShopCarService;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
import com.jeeplus.modules.shop.storediscount.entity.StoreDiscount;
import com.jeeplus.modules.shop.storediscount.service.StoreDiscountService;
import com.jeeplus.modules.shop.util.DiscountUtils;
import com.jeeplus.modules.shop.wares.entity.Wares;
import com.jeeplus.modules.shop.wares.service.WaresService;
import com.jeeplus.modules.shop.waresspecs.entity.WaresSpecs;
import com.jeeplus.modules.shop.waresspecs.service.WaresSpecsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lhh
 * @date 2019/12/2 0002
 */
@Api(value="ApiCustomerController",description="App用户端购物车控制器")
@Controller
@CrossOrigin
@RequestMapping(value = "${adminPath}/api/customer")
public class ApiShopCarController extends BaseController {
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
    private StoreDiscountService storeDiscountService;

//    @ModelAttribute
//    public Customer get(@RequestParam(required=false) String id) {
//        Customer entity = null;
//        if (StringUtils.isNotBlank(id)){
//            entity = customerService.get(id);
//        }
//        if (entity == null){
//            entity = new Customer();
//        }
//        return entity;
//    }


    @ApiOperation(notes = "appAddShopCar", httpMethod = "POST", value = "用户添加购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(name="customer_id",value = "用户id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="store_id",value = "商家id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="wares_id",value = "商品id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="price",value = "总价",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="waresPrice",value = "单价",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="num",value = "数量",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="guige",value = "规格id",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appAddShopCar")
    public AjaxJson appAddShopCar(ShopCar shopCar, String customer_id, String wares_id, String store_id,String guige){
        AjaxJson j=new AjaxJson();
        Customer customer=customerService.get(customer_id);
        Store store=storeService.get(store_id);
        Wares wares=waresService.get(wares_id);
        List<ShopCar> list  = new ArrayList<>();
        if(!StringUtils.isEmpty(guige)){
            WaresSpecs waresSpecs = waresSpecsService.get(guige);
            if(waresSpecs!=null){
                shopCar.setWaresPrice(waresSpecs.getWaresPrice());
                shopCar.setPrice(String.valueOf(Double.valueOf(waresSpecs.getWaresPrice())*Integer.valueOf(shopCar.getNum())));
            }
            ShopCar shopCar1=new ShopCar();
            shopCar1.setWares(wares);
            shopCar1.setGuige(shopCar.getGuige());
            shopCar1.setCustomer(customer);
            list=shopCarService.findList(shopCar1);
        }else{
            shopCar.setWaresPrice(wares.getPrice());
            shopCar.setPrice(String.valueOf(Double.valueOf(wares.getPrice())*Integer.valueOf(shopCar.getNum())));
            ShopCar shopCar1=new ShopCar();
            shopCar1.setWares(wares);
            shopCar1.setCustomer(customer);
            list=shopCarService.findList(shopCar1);
        }

        /*guige = StringEscapeUtils.unescapeHtml4(guige);
        shopCar.setGuige(guige);*/

        /*if(!"".equals(shopCar.getGuige())){
            //根据商品id和规格查询所有购物车记录 如果有 则添加数量没有 就新增一条购物车记录

        }else{
            //根据商品id查询所有购物车记录 如果有 则添加数量没有 就新增一条购物车记录

        }*/
        if(list.size()==0){
            //说明此商品没有添加过
            shopCar.setStore(store);
            shopCar.setWares(wares);
            shopCar.setName(wares.getName());
            shopCar.setTitle(wares.getTitle());
            shopCar.setCustomer(customer);
            shopCar.setStore(store);
            shopCarService.save(shopCar);
            j.setSuccess(true);
            j.setMsg("添加成功");
            j.setErrorCode("666");
        }else {
            //商品添加过 那么数量就追加前端传过来的数量  价格同
            ShopCar shopCar2=list.get(0);
            String num=shopCar2.getNum();
            int s = Integer.parseInt(num)+ Integer.parseInt(shopCar.getNum());
            shopCar2.setNum(String.valueOf(s));
            String price=shopCar2.getPrice();
            double add=Common.add(Double.valueOf(price), Double.valueOf(shopCar.getPrice()));
            shopCar2.setPrice(String.valueOf(add));
            shopCarService.save(shopCar2);
            j.setSuccess(true);
            j.setMsg("添加成功");
            j.setErrorCode("666");
        }
        return j;
    }

    @ApiOperation(notes = "appGetCustomerShopCar", httpMethod = "POST", value = "获取用户购物车记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="customer_id",value = "用户id",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appGetCustomerShopCar")
    public AjaxJson appGetCustomerShopCar( String customer_id){
        AjaxJson j=new AjaxJson();

        if(customer_id.equals("")|| null == customer_id){
            j.setErrorCode("111");
            j.setMsg("参数错误");
            j.setSuccess(false);
            return j;
        }

        Customer customer=customerService.get(customer_id);
        if(customer == null){
            j.setErrorCode("111");
            j.setMsg("用户不存在");
            j.setSuccess(false);
            return j;
        }

        ShopCar shopCar=new ShopCar();
        shopCar.setCustomer(customer);
        List<ShopCar> list=shopCarService.findList(shopCar);
        Iterator<ShopCar> iterator = list.iterator();
        while (iterator.hasNext()) {
            ShopCar s = iterator.next();
            s.setStoreId(s.getStore().getId());
            Wares wares = waresService.get(s.getWares());
            WaresSpecs waresSpecs = waresSpecsService.get(s.getGuige());
            if(waresSpecs!=null){
                s.setGuige(waresSpecs.getWaresSpecs());
                s.setStock(Integer.valueOf(waresSpecs.getWaresStock()));
            }else if(wares!=null){
                s.setStock(Integer.valueOf(wares.getStock()));
            }
            if(wares!=null){
                String img=s.getWares().getImg();
                String[] split=img.split("[,]");
                s.setWaresImg(split[0]);
            }else{
                iterator.remove();
            }
        }


        //java8根据商家id进行分组
        Map<String, List<ShopCar>> map =  list.stream()
                .collect(Collectors.groupingBy(ShopCar::getStoreId));
        ArrayList<ShopCarUtil> systems=new ArrayList<>();
        //遍历map集合
        for (Map.Entry<String, List<ShopCar>> entry : map.entrySet()) {
            Store store=storeService.get(entry.getKey());
            ShopCarUtil shopCarUtil=new ShopCarUtil();
            shopCarUtil.setImg(store.getImg());
            shopCarUtil.setName(store.getName());
            shopCarUtil.setStoreId(store.getId());
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
                    if("1".equals(storeDiscount1.getDiscountState())){
                        storeDiscountPrice=DiscountUtils.getDiscount(storeDiscount1, Double.valueOf(shopCar1.getPrice()));
                        discount=shopCar1.getWaresPrice();
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
                    if("1".equals(storeDiscount2.getDiscountState())){
                        waresDiscountPrice=DiscountUtils.getDiscount(storeDiscount2, Double.valueOf(shopCar1.getPrice()));
                    }else{
                        discount = DiscountUtils.getDiscount(storeDiscount2, Double.valueOf(discount));
                    }

                }
                Double price=Double.valueOf(storeDiscountPrice)+Double.valueOf(waresDiscountPrice);
                shopCar1.setPresentPrice(String.valueOf(Double.valueOf(discount)*Integer.valueOf(shopCar1.getNum())-price));

            }
            shopCarUtil.setList(value);

            systems.add(shopCarUtil);
        }

        j.setSuccess(true);
        j.put("data",systems);
        return j;
    }



    @ApiOperation(notes = "appGetPriceBySpecs", httpMethod = "GET", value = "根据用户选择的规格获取某一个规格的价格/库存")
    @ApiImplicitParams({
            @ApiImplicitParam(name="wares_id",value = "商品id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="guige",value = "{12g,红色}",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appGetPriceBySpecs")
    public AjaxJson appGetPriceBySpecs( String wares_id,String guige){
        AjaxJson j=new AjaxJson();
        WaresSpecs waresSpecs=new WaresSpecs();
        waresSpecs.setWares(waresService.get(wares_id));
        /*guige=StringEscapeUtils.unescapeHtml4(guige);*/
        waresSpecs.setWaresSpecs(guige);
        List<WaresSpecs> list=waresSpecsService.findList(waresSpecs);
        if(list.size() == 0){
            j.setMsg("暂无数据");
        }else if(list.size()>1) {
            j.setMsg("数据异常");
        }else {
            j.put("data",list.get(0));
            j.setMsg("查询成功");
        }

        return j;
    }


    @ApiOperation(notes = "appJiaOrJianShopCar", httpMethod = "DELETE", value = "加减用户购物车记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="shopcar_id",value = "购物车id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="number",value = "数字",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appJiaOrJianShopCar")
    public AjaxJson appJiaOrJianShopCar( String shopcar_id,Integer number){
        AjaxJson j=new AjaxJson();

        ShopCar shopCar=shopCarService.get(shopcar_id);
        if(null == shopCar){
            j.setSuccess(false);
            j.setMsg("参数错误");
            j.setErrorCode("111");
            return j;
        }
        Integer num=Integer.valueOf(shopCar.getNum());
        Double price=Double.valueOf(shopCar.getPrice());
        Double unitprice=price/num;
        shopCar.setPrice(String.valueOf(unitprice * number));
        shopCar.setNum(String.valueOf(number));
        /*shopCar.setGuige(null);*/
        shopCarService.save(shopCar);
        j.setMsg("修改成功");
        j.setSuccess(true);

        return j;
    }


    @ApiOperation(notes = "appDeletCustomerShopCar", httpMethod = "DELETE", value = "删除用户购物车记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="shopcarIdList",value = "购物车id集合",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appDeletCustomerShopCar")
    public AjaxJson appDeletCustomerShopCar( String[] shopcarIdList){

        AjaxJson j=new AjaxJson();
        for (String shopcarId:shopcarIdList){
            ShopCar shopCar=shopCarService.get(shopcarId);
            shopCarService.delete(shopCar);
        }

        j.setMsg("删除成功");
        j.setSuccess(true);
        return j;
    }

    @ApiOperation(notes = "appQingKongCustomerShopCar", httpMethod = "DELETE", value = "清空用户购物车记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="customer_id",value = "用户id",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appQingKongCustomerShopCar")
    public AjaxJson appQingKongCustomerShopCar( String customer_id){
        AjaxJson j=new AjaxJson();

        Customer customer=customerService.get(customer_id);
        ShopCar shopCar=new ShopCar();
        shopCar.setCustomer(customer);
        List<ShopCar> list=shopCarService.findList(shopCar);
        for (ShopCar s:
             list) {
            shopCarService.delete(s);
        }
        j.setSuccess(true);
        j.setMsg("清空成功");
        j.setErrorCode("666");
        j.put("data",new ArrayList<ShopCar>());
        return j;
    }
}
