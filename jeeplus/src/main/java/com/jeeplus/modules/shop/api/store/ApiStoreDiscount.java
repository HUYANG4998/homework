package com.jeeplus.modules.shop.api.store;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
import com.jeeplus.modules.shop.storediscount.entity.StoreDiscount;
import com.jeeplus.modules.shop.storediscount.service.StoreDiscountService;
import com.jeeplus.modules.shop.storemovie.entity.StoreMovie;
import com.jeeplus.modules.shop.util.Merge;
import com.jeeplus.modules.shop.wares.entity.Wares;
import com.jeeplus.modules.shop.wares.service.WaresService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.runtime.StoredScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuan
 * @date 2019/11/29 0029
 */

@Api(value = "ApiStoreController", description = "App商家端优惠控制器")
@Controller
@CrossOrigin
@RequestMapping(value = "${adminPath}/api/storeDiscount")
public class ApiStoreDiscount extends BaseController {

    @Autowired
    private StoreDiscountService storeDiscountService;

    @Autowired
    private StoreService storeService;
    @Autowired
    private WaresService waresService;

    @ModelAttribute
    public StoreDiscount get(@RequestParam(required = false) String id) {
        StoreDiscount entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = storeDiscountService.get(id);
        }
        if (entity == null) {
            entity = new StoreDiscount();
        }
        return entity;
    }

    @ApiOperation(notes = "appGetStoreDiscount", httpMethod = "GET", value = "获取商家/商品/优惠详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "store_id", value = "商家id(查商家优惠传)", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "wares_id", value = "商品id（查商品优惠传）", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "discount_type", value = "优惠所属（1：商家 2：商品）", required = true, paramType = "query", dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appGetStoreDiscount")
    public AjaxJson appGetStoreDiscount(HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String discount_type = request.getParameter("discount_type");

        if (null == discount_type || "".equals(discount_type)) {
            j.setMsg("优惠所属参数错误");
            j.setSuccess(false);
            j.setErrorCode("111");
            return j;
        }
        StoreDiscount storeDiscount = new StoreDiscount();
        if (discount_type.equals("1")) {
            //商家优惠
            String store_id = request.getParameter("store_id");
            Store store = storeService.get(store_id);
            if (null == store || store.getId().equals("")) {
                j.setErrorCode("222");
                j.setSuccess(false);
                j.setMsg("此商家不存在");
                return j;
            } else {
                storeDiscount.setStore(store);
            }
        } else if (discount_type.equals("2")) {
            //商品优惠
            String wares_id = request.getParameter("wares_id");
            Wares wares = waresService.get(wares_id);
            if (null == wares || wares.getId().equals("")) {
                j.setErrorCode("222");
                j.setSuccess(false);
                j.setMsg("此商品不存在");
                return j;
            } else {
                storeDiscount.setWares(wares);
            }
        }
        storeDiscount.setDiscountType(discount_type);

        List<StoreDiscount> list = storeDiscountService.findList(storeDiscount);
        if (list.size() > 0) {

            j.put("data", list.get(0));
        } else {
            j.put("data", null);
        }
        j.setSuccess(true);
        j.setMsg("查询成功");
        j.setErrorCode("666");

        return j;
    }


    @ApiOperation(notes = "appAddStoreDiscount", httpMethod = "POST", value = "添加商家/商品/优惠信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "优惠id", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "store_id", value = "商家id", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "wares_id", value = "商品id(添加商家优惠时不需要传)", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "discountType", value = "优惠所属（1：商家 2：商品）", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "manMoney", value = "满多少金额", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "msg", value = "减/送/折扣", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "discountState", value = "满减 1 /满送 2/折扣 3", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "startDate", value = "开启时间", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "status", value = "0新增1修改", required = true, paramType = "query", dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appAddStoreDiscount")
    public AjaxJson appAddStoreDiscount(StoreDiscount storeDiscount, HttpServletRequest request) throws Exception{
        request.setCharacterEncoding("utf-8");

        AjaxJson j = new AjaxJson();
        String discount_type = request.getParameter("discountType");
        String status = request.getParameter("status");

        if (null == discount_type || "".equals(discount_type)) {
            j.setMsg("优惠所属参数错误");
            j.setSuccess(false);
            j.setErrorCode("111");
            return j;
        }
        String store_id = request.getParameter("store_id");
        Store store = null;
        if (!StringUtils.isEmpty(store_id)) {
            store = storeService.get(store_id);
        } else {
            j.setMsg("商家id为空");
            j.setSuccess(false);
            return j;
        }
        Wares wares = null;
        String wares_id = request.getParameter("wares_id");


        if ("0".equals(status)) {
            if ("2".equals(discount_type)) {

                if (!StringUtils.isEmpty(wares_id)) {
                    wares = waresService.get(wares_id);
                } else {
                    j.setMsg("商品id为空");
                    j.setSuccess(false);
                    return j;
                }
                if (wares != null) {

                    StoreDiscount storeD1 = new StoreDiscount();
                    storeD1.setWares(wares);
                    storeD1.setStore(store);
                    List<StoreDiscount> storeDiscountList1 = storeDiscountService.findList(storeD1);
                    if (storeDiscountList1.size() > 0) {
                        j.setMsg("优惠已存在");
                        j.setSuccess(false);
                        return j;
                    }

                    wares.setIsYou("1");
                    waresService.save(wares);
                }
            }else{
                StoreDiscount storeD1 = new StoreDiscount();

                storeD1.setStore(store);
                List<StoreDiscount> storeDiscountList1 = storeDiscountService.findList(storeD1);
                if (storeDiscountList1.size() > 0) {
                    j.setMsg("优惠已存在");
                    j.setSuccess(false);
                    return j;
                }
            }

            storeDiscount.setYesNo("1");

        }
        if (null == store || store.getId().equals("")) {
            j.setErrorCode("222");
            j.setSuccess(false);
            j.setMsg("此商家不存在");
            return j;
        }
        if (discount_type.equals("1")) {
            //商家
            storeDiscount.setStore(store);
        } else if (discount_type.equals("2")) {
            //商品
            storeDiscount.setWares(waresService.get(wares_id));
        }
        StoreDiscount s1=storeDiscountService.get(storeDiscount.getId());
        if(s1!=null){
            storeDiscount = (StoreDiscount)Merge.combineSydwCore(storeDiscount, s1);
        }

        storeDiscountService.save(storeDiscount);
        j.setMsg("添加成功");
        j.setSuccess(true);
        j.setErrorCode("666");
        j.put("data", storeDiscount);
        return j;
    }

    @ApiOperation(notes = "appUpdateStoreDiscount", httpMethod = "POST", value = "开启-关闭商家/商品/优惠详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "优惠id", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "yesNo", value = "是否开启（0：停止1：开启）", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "（0：商品1:商家）", required = true, paramType = "query", dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appUpdateStoreDiscount")
    public AjaxJson appUpdateStoreDiscount(StoreDiscount storeDiscount, String type) {
        AjaxJson j = new AjaxJson();
        if (storeDiscount.getYesNo().equals("0")) {

            if (type.equals("0")) {
                //商品停止
                /*Wares wares=waresService.get(storeDiscount.getWares().getId());
                wares.setIsYou("0");
                waresService.save(wares);*/
                storeDiscountService.save(storeDiscount);
                j.setMsg("商品停止优惠成功");
            } else if (type.equals("1")) {
                //商家停止
                storeDiscountService.save(storeDiscount);
                j.setMsg("商家停止优惠成功");
            }

        } else if (storeDiscount.getYesNo().equals("1")) {
            if (type.equals("0")) {
                //商品开启
                Wares wares = waresService.get(storeDiscount.getWares().getId());
                wares.setIsYou("1");
                waresService.save(wares);
                storeDiscountService.save(storeDiscount);
                j.setMsg("商品开启成功");
            } else if (type.equals("1")) {
                //商家开启
                storeDiscountService.save(storeDiscount);
                j.setMsg("商家开启优惠成功");
            }
        }

        j.setSuccess(true);
        j.setErrorCode("666");
        j.put("data", storeDiscount);
        return j;
    }

    @ApiOperation(notes = "selectCurrentDiscounts", httpMethod = "POST", value = "获取当前优惠活动")

    @ResponseBody
    @RequestMapping(value = "/selectCurrentDiscounts")
    public AjaxJson selectCurrentDiscounts() {
        AjaxJson j = new AjaxJson();
        List<Map<String, Object>> listMap = storeDiscountService.selectCurrentDiscounts();
        Integer discounts = storeDiscountService.selectDiscountsNumber();
        Map<String, Object> map = new HashMap<>();
        map.put("discount", discounts);
        map.put("list", listMap);
        j.setSuccess(true);
        j.setErrorCode("666");
        j.put("data", map);
        return j;
    }


}
