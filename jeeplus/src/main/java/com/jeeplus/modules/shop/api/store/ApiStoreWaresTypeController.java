package com.jeeplus.modules.shop.api.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;

import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
import com.jeeplus.modules.shop.storemovie.entity.StoreMovie;
import com.jeeplus.modules.shop.wares.entity.Wares;
import com.jeeplus.modules.shop.wares.service.WaresService;
import com.jeeplus.modules.shop.waresspecs.entity.WaresSpecs;
import com.jeeplus.modules.shop.waresspecs.service.WaresSpecsService;
import com.jeeplus.modules.shop.warestype.entity.WaresType;
import com.jeeplus.modules.shop.warestype.service.WaresTypeService;
import com.jeeplus.modules.shop.warestypeattributekey.entity.WaresTypeAttributeKey;
import com.jeeplus.modules.shop.warestypeattributekey.service.WaresTypeAttributeKeyService;
import com.jeeplus.modules.shop.warestypeattributevalue.entity.WaresTypeAttributeValue;
import com.jeeplus.modules.shop.warestypeattributevalue.service.WaresTypeAttributeValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author lhh
 * @date 2019/12/2 0002
 */
@Api(value="ApiStoreWaresTypeController",description="App商家商品分类控制器")
@Controller
@CrossOrigin
@RequestMapping(value = "${adminPath}/api/storeWaresType")
public class ApiStoreWaresTypeController extends BaseController {
    @Autowired
    private WaresService waresService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private WaresTypeService waresTypeService;
    @Autowired
    private WaresSpecsService waresSpecsService;
    @Autowired
    private WaresTypeAttributeKeyService waresTypeAttributeKeyService;
    @Autowired
    private WaresTypeAttributeValueService waresTypeAttributeValueService;



    @ApiOperation(notes = "appGetWaresType", httpMethod = "GET", value = "获取商家分类/以及第一个分类下的商品集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name="store_id",value = "商家id",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appGetWaresType")
    public AjaxJson appGetWaresType(String store_id){

        AjaxJson j=new AjaxJson();
        Store store=storeService.get(store_id);
        WaresType waresType=new WaresType();
        waresType.setStore(store);
        List<WaresType> list=waresTypeService.findList(waresType);
        if (list.size() == 0){
            j.setSuccess(false);
            j.setMsg("暂无分类");
            j.setErrorCode("111");
        }else {
            Wares wares=new Wares();
            wares.setStore(store);
            wares.setWaresType(list.get(0));
            List<Wares> list1=waresService.findList(wares);
            list.get(0).setList(list1);
            j.setSuccess(true);
            j.setMsg("查询成功");
            j.setErrorCode("666");
            j.put("data",list);
        }
        return j;
    }


    @ApiOperation(notes = "appGetWaresByType", httpMethod = "GET", value = "根据分类获取商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name="wares_type_id",value = "分类id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="store_id",value = "商家id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="is_shang",value = "是否上架0否1是",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appGetWaresByType")
    public AjaxJson appGetWaresByType(String wares_type_id,String store_id,String is_shang){
        AjaxJson j=new AjaxJson();

        WaresType waresType=waresTypeService.get(wares_type_id);

        if(null == waresType){
            j.setSuccess(false);
            j.setMsg("此分类不存在");
            j.setErrorCode("111");
            return j;
        }
        Wares wares=new Wares();
        wares.setStore(storeService.get(store_id));
        wares.setWaresType(waresType);
        wares.setIsShang(is_shang);
        List<Wares> list1=waresService.findList(wares);
        waresType.setList(list1);

        j.setErrorCode("666");
        j.setMsg("查询成功");
        j.setSuccess(true);
        j.put("data",waresType);
        return j;
    }
    @ApiOperation(notes = "appSaveWaresType", httpMethod = "POST", value = "新建分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name="store_id",value = "商家id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="name",value = "分类名称",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appSaveWaresType")
    public AjaxJson appSaveWaresType(String store_id,String name){
        AjaxJson j=new AjaxJson();
        if(null == store_id || null == name){
            j.setErrorCode("111");
            j.setSuccess(false);
            j.setMsg("参数错误");
            return j;
        }



        Store store=storeService.get(store_id);
        WaresType waresType=new WaresType();
        waresType.setStore(store);
        waresType.setName(name);

        List<WaresType> list=waresTypeService.findList(waresType);
        if(list.size()>0){
            //说明已经添加过该分类
            j.setSuccess(false);
            j.setMsg("已经添加过该分类,请勿重新添加");
            j.setErrorCode("444");
        }else{
            waresTypeService.save(waresType);
            j.setSuccess(true);
            j.setErrorCode("666");
            j.put("data",waresType);
        }

        return j;
    }


    @ApiOperation(notes = "appDeleteWaresType", httpMethod = "DELETE", value = "删除分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name="wares_type_id",value = "分类id",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appDeleteWaresType")
    public AjaxJson appDeleteWaresType(String wares_type_id){

        AjaxJson j=new AjaxJson();
        WaresType waresType=waresTypeService.get(wares_type_id);
        waresTypeService.delete(waresTypeService.get(wares_type_id));

        Wares wares=new Wares();
        wares.setWaresType(waresType);
        List<Wares> list=waresService.findList(wares);

        //级联删除商品
        for (Wares s:
             list) {
            waresService.delete(s);
            WaresSpecs waresSpecs=new WaresSpecs();
            waresSpecs.setWares(s);
            //级联删除商品规格
            List<WaresSpecs> list1=waresSpecsService.findList(waresSpecs);
            for (WaresSpecs s1:
                 list1) {
                waresSpecsService.delete(s1);
            }


        }


        WaresTypeAttributeKey waresTypeAttributeKey=new WaresTypeAttributeKey();
        waresTypeAttributeKey.setWaresType(waresType);
        List<WaresTypeAttributeKey> list1=waresTypeAttributeKeyService.findList(waresTypeAttributeKey);

        //级联删除key
        for (WaresTypeAttributeKey w:
             list1) {
            waresTypeAttributeKeyService.delete(w);
            //级联删除value
            WaresTypeAttributeValue waresTypeAttributeValue=new WaresTypeAttributeValue();
            waresTypeAttributeValue.setAttributeKey(w);
            List<WaresTypeAttributeValue> list2=waresTypeAttributeValueService.findList(waresTypeAttributeValue);
            for (WaresTypeAttributeValue w1:
                 list2) {
                waresTypeAttributeValueService.delete(w1);
            }
        }
        j.setErrorCode("666");
        j.setSuccess(true);
        j.setMsg("删除成功");
        return j;
    }
}
