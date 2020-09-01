package com.jeeplus.modules.shop.api.store;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
import com.jeeplus.modules.shop.storediscount.entity.StoreDiscount;
import com.jeeplus.modules.shop.storediscount.service.StoreDiscountService;
import com.jeeplus.modules.shop.storemovie.entity.StoreMovie;
import com.jeeplus.modules.shop.util.ExtendsArrayList;
import com.jeeplus.modules.shop.util.JSONUtils;
import com.jeeplus.modules.shop.util.Merge;
import com.jeeplus.modules.shop.util.Utils;
import com.jeeplus.modules.shop.wares.entity.Wares;
import com.jeeplus.modules.shop.wares.service.WaresService;
import com.jeeplus.modules.shop.waresspecs.entity.WaresSpecs;
import com.jeeplus.modules.shop.waresspecs.service.WaresSpecsService;
import com.jeeplus.modules.shop.warestype.entity.WaresType;
import com.jeeplus.modules.shop.warestype.service.WaresTypeService;
import com.jeeplus.modules.shop.warestypeattributekey.entity.WaresTypeAttributeKey;
import com.jeeplus.modules.shop.warestypeattributekey.mapper.WaresTypeAttributeKeyMapper;
import com.jeeplus.modules.shop.warestypeattributekey.service.WaresTypeAttributeKeyService;
import com.jeeplus.modules.shop.warestypeattributevalue.entity.WaresTypeAttributeValue;
import com.jeeplus.modules.shop.warestypeattributevalue.service.WaresTypeAttributeValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.formula.functions.T;
import org.bytedeco.javacpp.presets.opencv_core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @author lhh
 * @date 2019/12/2 0002
 */

@Api(value="ApiStoreWaresController",description="App商家商品控制器")
@Controller
@CrossOrigin
@RequestMapping(value = "${adminPath}/api/storeWares")
public class ApiStoreWaresController  extends BaseController {
    @Autowired
    private WaresService waresService;

    @Autowired
    private WaresTypeService waresTypeService;
    @Autowired
    private WaresTypeAttributeKeyService waresTypeAttributeKeyService;
    @Autowired
    private WaresTypeAttributeValueService waresTypeAttributeValueService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private WaresSpecsService waresSpecsService;
    @Autowired
    private StoreDiscountService storeDiscountService;
    /*@Autowired
    private PrivateValueService privateValueService;*/

    @ModelAttribute
    public Wares get(@RequestParam(required=false) String id) {
        Wares entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = waresService.get(id);
        }
        if (entity == null){
            entity = new Wares();
        }
        return entity;
    }


    @ApiOperation(notes = "appGetStoreDetail", httpMethod = "POST", value = "获取商家端所有商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="store_id",value = "商家id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="name",value = "名称",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="pageNum",value = "第几页",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="pageSize",value = "每页个数",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appGetStoreDetail")
    public AjaxJson appGetStoreDetail(String store_id) {
        AjaxJson j=new AjaxJson();
        Store store=storeService.get(store_id);

        Wares wares=new Wares();
        wares.setIsShang("1");
        wares.setStore(store);

        List<Wares> list=waresService.findList(wares);

        j.put("data",list);
        j.setSuccess(true);
        j.setMsg("查询成功");


        return j;
    }


    @ApiOperation(notes = "appGetKeyByWaresType", httpMethod = "GET", value = "根据分类获取key和value")
    @ApiImplicitParams({
            @ApiImplicitParam(name="wares_type_id",value = "商品分类id",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appGetKeyByWaresType")
    public AjaxJson appGetKeyByWaresType(String wares_type_id){
        AjaxJson j=new AjaxJson();

        if("".equals(wares_type_id) || null == wares_type_id){
            j.setMsg("参数错误");
            j.setSuccess(false);
            j.setErrorCode("777");
            return j;
        }

        WaresType waresType=waresTypeService.get(wares_type_id);
        if(null == waresType){
            j.setErrorCode("777");
            j.setSuccess(false);
            j.setMsg("分类不存在");
            return j;
        }

        //根据分类查询key

        WaresTypeAttributeKey wk=new WaresTypeAttributeKey();
        wk.setWaresType(waresType);
        List<WaresTypeAttributeKey> list=waresTypeAttributeKeyService.findList(wk);

        for (WaresTypeAttributeKey w:
             list) {
            //根据key查询value集合
            WaresTypeAttributeValue waresTypeAttributeValue=new WaresTypeAttributeValue();
            waresTypeAttributeValue.setAttributeKey(w);
            List<WaresTypeAttributeValue> list1=waresTypeAttributeValueService.findList(waresTypeAttributeValue);
            w.setWaresTypeAttributeValueList(list1);
        }
        if(list.size()>0){
            j.setSuccess(true);
            j.put("data",list);
            j.setMsg("查询成功");
        }else {
            j.setSuccess(false);
            j.setErrorCode("777");
            j.setMsg("暂无数据");
        }
        return j;
    }






    @ApiOperation(notes = "appAddWaresTypeAttributeKey", httpMethod = "POST", value = "添加商品属性key")
    @ApiImplicitParams({
            @ApiImplicitParam(name="wares_type_id",value = "商品分类id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="name",value = "名称",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="sort",value = "0 1 2 3(做排序用户端展示用)",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="is_img",value = "图片0否1是",required = true, paramType = "query",dataType = "string")
    })
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    @RequestMapping(value = "/appAddWaresTypeAttributeKey")
    public AjaxJson appAddWaresTypeAttributeKey(String wares_type_id,String name,String sort,String is_img){
        AjaxJson j=new AjaxJson();


        try {
            if("".equals(name)||sort.equals("")||"".equals(wares_type_id)||null ==wares_type_id ||null == name || null == sort||"".equals(is_img)||null==is_img){
                j.setErrorCode("777");
                j.setSuccess(false);
                j.setMsg("参数错误,请仔细核对参数不能为空并且不能为null");
                return j;
            }

            //根据id获取商品分类对象
            WaresType waresType=waresTypeService.get(wares_type_id);
            if(null == waresType){
                j.setMsg("此分类不存在");
                j.setSuccess(false);
                j.setErrorCode("111");
                return  j;
            }

            WaresTypeAttributeKey wk=new WaresTypeAttributeKey();
            wk.setWaresType(waresType);
            wk.setName(name);
            wk.setSort(sort);
            wk.setIsImg(is_img);
            waresTypeAttributeKeyService.save(wk);


            Wares wares=new Wares();
            wares.setWaresType(waresType);
            List<Wares> list1 = waresService.findList(wares);
            for (Wares w:list1){

                String json=w.getAttributeList();
                List<Map> list=new ArrayList<>();
                if(!StringUtils.isEmpty(json)){
                    list = JSONUtils.toList(json,Map.class);
                }
                Map<String,Object> map=new LinkedHashMap<>();
                map.put("name",wk.getName());
                map.put("keyid",wk.getId());
                map.put("selectedList",new ArrayList<>());
                list.add(map);

                w.setAttributeList(JSON.toJSONString(list));


                waresService.save(w);
            }

            j.setMsg("添加成功");
            j.setSuccess(true);
            j.setErrorCode("666");
            j.put("data",wk);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            j.setMsg("添加失败");
            j.setSuccess(false);
            return j;
        }
        return j;
    }


    @ApiOperation(notes = "appAddWaresTypeAttributeValue", httpMethod = "POST", value = "添加商品属性value")
    @ApiImplicitParams({
            @ApiImplicitParam(name="key_id",value = "属性id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="name",value = "名称",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="sort",value = "0 1 2 3(做排序用户端展示用)",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="img",value = "图片",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appAddWaresTypeAttributeValue")
    public AjaxJson appAddWaresTypeAttributeValue(String key_id,String name,String img){

        AjaxJson j=new AjaxJson();
        if("".equals(name)||"".equals(key_id)||null ==key_id ||null == name){
            j.setErrorCode("777");
            j.setSuccess(false);
            j.setMsg("参数错误,请仔细核对参数不能为空并且不能为null");
            return j;
        }

        WaresTypeAttributeKey wk=waresTypeAttributeKeyService.get(key_id);
        if(null == wk){
            j.setErrorCode("111");
            j.setSuccess(false);
            j.setMsg("此属性不存在");
            return  j;
        }

        WaresTypeAttributeValue wv=new WaresTypeAttributeValue();
        wv.setAttributeKey(wk);
        wv.setAttributeValue(name);
        wv.setImg(img);
        waresTypeAttributeValueService.save(wv);
        j.setMsg("添加成功");
        j.setSuccess(true);
        j.setErrorCode("666");
        return j;
    }


    @ApiOperation(notes = "appDeleteWaresTypeAttributeValue", httpMethod = "DELETE", value = "删除商品属性value")
    @ApiImplicitParams({
            @ApiImplicitParam(name="value_id",value = "值id",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/appDeleteWaresTypeAttributeValue")
    public AjaxJson appDeleteWaresTypeAttributeValue(String value_id){

        AjaxJson j=new AjaxJson();
        if("".equals(value_id)||null ==value_id) {
            j.setErrorCode("777");
            j.setSuccess(false);
            j.setMsg("参数错误,请仔细核对参数不能为空并且不能为null");
            return j;
        }
        try {
            WaresTypeAttributeValue value = waresTypeAttributeValueService.get(value_id);
            waresTypeAttributeValueService.delete(value);
            WaresTypeAttributeKey wk = waresTypeAttributeKeyService.get(value.getAttributeKey().getId());
            WaresType waresType = waresTypeService.get(wk.getWaresType());

            Wares wares=new Wares();
            wares.setWaresType(waresType);
            List<Wares> list1 = waresService.findList(wares);
            for (Wares w:list1){
                String json=w.getAttributeList();
                List<Map> ts=new ArrayList<>();
                if(!StringUtils.isEmpty(json)){
                    ts= JSONUtils.toList(json,Map.class);
                }

                List<Map<String,Object>> list2=new ArrayList<>();
                if(ts.size()>0){
                    for (Map<String,Object> map:ts){
                        List<Map<String,Object>> selectList=(List<Map<String,Object>>)map.get("selectedList");
                        List<Map<String,Object>> selects=new ArrayList<>();
                        if(selectList.size()>0){
                            for (Map<String,Object> m1:selectList){
                                if(!m1.get("id").equals(value.getId())){
                                    selects.add(m1);
                                }
                            }
                        }
                        map.put("selectedList",selects);
                        list2.add(map);

                    }
                }

                if (list2.size()>0){
                    w.setAttributeList(JSON.toJSONString(list2));
                }else{
                    w.setAttributeList(null);
                }

                waresService.save(w);
            }

        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        j.setMsg("删除成功");
        j.setSuccess(true);
        j.setErrorCode("666");
        return j;
    }


    @ApiOperation(notes = "appChuLiShuGuiGe", httpMethod = "POST", value = "处理规格数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name="keyid",value = "keyid",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="name",value = "name",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="nameList",value = "[{name:'红色',img:'图片',id:'id',valueId:'valueid'}]",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appChuLiShuGuiGe")
    public AjaxJson appChuLiShuGuiGe(@RequestBody Map<String,Object> mapRequest){
        String waresId=mapRequest.get("waresId")==null?null:(String)mapRequest.get("waresId");
        List<Map<String,Object>> map=mapRequest.get("map")==null?null:(List<Map<String,Object>>)mapRequest.get("map");

        AjaxJson j=new AjaxJson();
        Map<String,Object> a=new LinkedHashMap<>();
        int i=0;
        Map<String,Object> c=new LinkedHashMap<>();

        for (Map<String,Object> result:map){
           /* String name=result.get("name").toString();
            String keyId=result.get("keyid").toString();*/

            List<Map<String,Object>> list=(List<Map<String,Object>>)result.get("selectedList");
            if(list.size()>0){
                List<String> b=new LinkedList<>();
                /*List<String> privateValueIds=new ArrayList<>();*/
                for (Map<String,Object> r:list){
                    String name1=r.get("attributeValue").toString();
                    String img=null;
                    if(r.containsKey("img")){

                        img=r.get("img").toString();
                        c.put(name1,img);


                    }
                    /*String id=r.get("id").toString();*/
                    /*String valueId=r.get("id").toString();*/
                   /* if(!StringUtils.isEmpty(id)){

                        PrivateValue privateValue=privateValueService.selectById(id);
                        if(privateValue!=null){
                            privateValueIds.add(privateValue.getId());
                            privateValue.setImg(img);
                            privateValue.setAttributeValue(name1);
                            privateValueService.updateById(privateValue);
                        }
                    }else{
                        PrivateValue privateValue=new PrivateValue();
                        privateValue.setAttributeId(keyId);
                        privateValue.setWaresId(waresId);
                        privateValue.setAttributeValue(name);
                        privateValue.setImg(img);
                        privateValue.setPublicValueId(valueId);
                        privateValueService.insertPrivateValue(privateValue);
                        privateValueIds.add(privateValue.getId());
                    }*/
                    b.add(name1);


                }
                a.put("name"+i,b);
                i++;
            }

           /* Map<String,Object> deletePrivate=new HashMap<>();
            deletePrivate.put("waresId",waresId);
            deletePrivate.put("attributeId",keyId);
            deletePrivate.put("ids",privateValueIds);*/

            /*privateValueService.deleteByWaresIdAndAttrIdAndIds(deletePrivate);*/

        }
        Map<String,Object> sd=new LinkedHashMap<>();
        sd.put("name", JSONUtils.toJson(a));
        sd.put("img",c);
        List<Map<String,Object>> listMap=test1.getSpec(sd);
        if(!StringUtils.isEmpty(waresId)){
            WaresSpecs waresSpecs=new WaresSpecs();
            Wares wares=new Wares();
            wares.setId(waresId);
            waresSpecs.setWares(wares);
            List<WaresSpecs> listWaresSpecs=waresSpecsService.findList(waresSpecs);

                List<String> xingList=new LinkedList<>();
                List<Map<String,Object>> ll=new ArrayList<>();
                Map<String,Object> map2=new LinkedHashMap<>();
                for (WaresSpecs waresSpecs1:listWaresSpecs){
                    xingList.add(waresSpecs1.getWaresSpecs());
                    map2.put(waresSpecs1.getWaresSpecs(),waresSpecs1.getId());
                   /* Map<String,Object> m=new HashMap<>();
                    m.put("nameList",Arrays.asList(waresSpecs1.getWaresSpecs().split(",")));
                    m.put("id",waresSpecs1.getId());
                    m.put("stock",waresSpecs1.getWaresStock());
                    m.put("price",waresSpecs1.getWaresPrice());
                    ll.add(m);*/
                }
                List<Map<String,Object>> l=new LinkedList<>();
                List<String> ids=new LinkedList<>();
                for (Map<String,Object> listm:listMap){
                    Map<String,Object> map1=new LinkedHashMap<>();
                    List<String> k=new ExtendsArrayList<>();
                    for(Map.Entry<String, Object> entry : listm.entrySet()){
                        String value=(String)entry.getValue();
                        k.add(value.trim());

                    }
                    map1.put("stock","0");
                    map1.put("price","0");
                    map1.put("nameList",k);
                    String s = k.toString();
                    String ke=k.toString().replace("[","").replace("]","");
                    if(!xingList.contains(ke)){

                        l.add(map1);
                    }else{
                        String id=(String)map2.get(ke);
                        ids.add(id);
                    }
                }
                for (String id:ids){
                    for (WaresSpecs waresSpecs1:listWaresSpecs){
                        if(id.equals(waresSpecs1.getId())){
                            Map<String,Object> m=new LinkedHashMap<>();
                            m.put("nameList",Arrays.asList(waresSpecs1.getWaresSpecs().split(",")));
                            m.put("id",waresSpecs1.getId());
                            m.put("stock",waresSpecs1.getWaresStock());
                            m.put("price",waresSpecs1.getWaresPrice());
                            ll.add(m);
                        }
                    }
                }
                /*for (WaresSpecs waresSpecs1:listWaresSpecs){
                    if(ids.contains(waresSpecs1.getId())){
                        Map<String,Object> m=new LinkedHashMap<>();
                        m.put("nameList",Arrays.asList(waresSpecs1.getWaresSpecs().split(",")));
                        m.put("id",waresSpecs1.getId());
                        m.put("stock",waresSpecs1.getWaresStock());
                        m.put("price",waresSpecs1.getWaresPrice());
                        ll.add(m);
                    }
                    }*/

                listMap=l;
                listMap.addAll(ll);

        }else{
            List<Map<String,Object>> m=new ArrayList<>();
            for (Map<String,Object> listm:listMap){
                Map<String,Object> map1=new HashMap<>();
                List<String> l=new ArrayList<>();
                for(Map.Entry<String, Object> entry : listm.entrySet()){
                    String value=(String)entry.getValue();
                    l.add(value);
                }
                map1.put("nameList",l);
                map1.put("stock","0");
                map1.put("price","0");
                m.add(map1);
            }
            listMap=m;
        }

        j.put("data",listMap);
        j.setMsg("处理成功");
        return j;
    }


    @ApiOperation(notes = "appDeleteWaresTypeAttributeKey", httpMethod = "DELETE", value = "删除商品属性key")
    @ApiImplicitParams({
            @ApiImplicitParam(name="key_id",value = "属性id",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appDeleteWaresTypeAttributeKey")
    public AjaxJson appDeleteWaresTypeAttributeKey(String key_id){
        AjaxJson j=new AjaxJson();

        WaresTypeAttributeKey wk=waresTypeAttributeKeyService.get(key_id);

        WaresType waresType = waresTypeService.get(wk.getWaresType().getId());
        Wares wares=new Wares();
        wares.setWaresType(waresType);
        List<Wares> list1 = waresService.findList(wares);
        for (Wares w:list1){
            String json=w.getAttributeList();
            List<Map> ts=new ArrayList<>();
            if(!StringUtils.isEmpty(json)){
                 ts= JSONUtils.toList(json,Map.class);
            }

            List<Map<String,Object>> list2=new ArrayList<>();
            if(ts.size()>0){
                for (Map<String,Object> map:ts){
                    if(!map.get("name").equals(wk.getName())){
                        list2.add(map);

                    }
                }
            }

            if (list2.size()>0){
                w.setAttributeList(JSON.toJSONString(list2));
            }else{
                w.setAttributeList(null);
            }

            waresService.save(w);
        }


        //级联删除value值
        WaresTypeAttributeValue waresTypeAttributeValue=new WaresTypeAttributeValue();
        waresTypeAttributeValue.setAttributeKey(wk);
        List<WaresTypeAttributeValue> list=waresTypeAttributeValueService.findList(waresTypeAttributeValue);
        for (WaresTypeAttributeValue w:
             list) {
            waresTypeAttributeValueService.delete(w);
        }
        waresTypeAttributeKeyService.delete(wk);
        j.setErrorCode("666");
        j.setSuccess(true);
        j.setMsg("删除成功");
        return j;
    }

    @ApiOperation(notes = "appSwitchKey", httpMethod = "DELETE", value = "拖拽key")
    @ApiImplicitParams({
            @ApiImplicitParam(name="keyId",value = "keyid数组",required = true, paramType = "query",dataType = "string"),

    })
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    @RequestMapping(value = "/appSwitchKey")
    public AjaxJson appDeleteWaresTypeAttributeKey(String [] keyId){
        AjaxJson j=new AjaxJson();

        try {
            if(keyId!=null&&keyId.length>0){
                for (int i=0;i< keyId.length;i++){
                    WaresTypeAttributeKey key=new WaresTypeAttributeKey();
                    key.setId(keyId[i]);
                    key.setSort(String.valueOf(i));
                    waresTypeAttributeKeyService.save(key);
                }
                String keyid=keyId[0];
                WaresTypeAttributeKey key=waresTypeAttributeKeyService.get(keyid);
                Wares wares=new Wares();

                wares.setWaresType(key.getWaresType());
                List<Wares> list = waresService.findList(wares);
                for (Wares ware:list){
                    String attribute=ware.getAttributeList();
                    List<Map> ts=null;
                    if(!StringUtils.isEmpty(attribute)){
                        ts= JSONUtils.toList(attribute,Map.class);
                    }
                    if(ts.size()>0){
                        List<Map<String,Object>> map=new ArrayList<>();
                        for (String ke:keyId){
                            for (Map<String,Object> json:ts){
                                if(json.get("keyid").equals(ke)){
                                    map.add(json);
                                    break;
                                }
                            }
                        }
                        if(map.size()>0){
                            ware.setAttributeList(JSON.toJSONString(map));
                        }
                        waresService.save(ware);
                    }

                }
                /*System.out.println(1/0);*/
                j.setErrorCode("666");
                j.setSuccess(true);
                j.setMsg("拖拽成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return j;
    }




    @ApiOperation(notes = "appAddWares", httpMethod = "POST", value = "添加商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "商品id（编辑传新增不传）",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="store_id",value = "商家id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="wares_type_id",value = "分类id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="byOne",value = "平台分类",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="name",value = "商品名称",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="title",value = "简介",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="price",value = "价格",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="movie",value = "视频",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="img",value = "主图",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="description",value = "介绍",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="dImg",value = "商品详情",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="attributeList",value ="属性集合(格式{'内存':['2G','4G'],'颜色':['红色']})",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="attributeLists",value ="属性集合(格式{'内存':['2G','4G'],'颜色':['红色']})",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="type",value = "0:新增1:编辑",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @Transactional(rollbackFor=Exception.class)
    @RequestMapping(value = "/appAddWares")
    public AjaxJson appAddWares(@RequestBody Map<String,Object> map) throws  Exception{
        AjaxJson j=new AjaxJson();

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInfo = objectMapper.writeValueAsString(map.get("wares"));
            Wares wares = objectMapper.readValue(jsonInfo,Wares.class);
            /*Map<String,Object> wares=map.get("wares")==null?null:(Map) map.get("wares");*/
            String attributeValue=map.get("attributeValue")==null?null:(String)map.get("attributeValue");
            List<Map<String,Object>> waresSpecList=map.get("waresSpec")==null?null:(List<Map<String,Object>>)map.get("waresSpec");
            String wares_type_id=map.get("wares_type_id")==null?null:(String)map.get("wares_type_id");
            String store_id=map.get("store_id")==null?null:(String)map.get("store_id");

            if(null==store_id || "".equals(store_id) || null==wares_type_id || "".equals(wares_type_id)) {
                j.setMsg("参数错误");
                j.setSuccess(false);
                j.setErrorCode("555");
                return j;
            }else{
                /*Wares w=new Wares();*/
                if(wares!=null){
                    if(!StringUtils.isEmpty(wares.getId())){
                        Wares w=waresService.get(wares.getId());
                        wares=(Wares) Merge.combineSydwCore(wares,w);
                    }else{
                        wares.setIsYou("0");
                        wares.setIsShang("0");
                        wares.setSaleNum("0");
                    }

                    Store store=storeService.get(store_id);
                    WaresType waresType=waresTypeService.get(wares_type_id);
                   /* w.setId(wares.get("id")==null?null:(String)wares.get("id"));
                    w.setName(wares.get("name")==null?null:(String)wares.get("name"));
                    w.setByOne(wares.get("byOne")==null?null:(String)wares.get("byOne"));
                    w.setTitle(wares.get("title")==null?null:(String)wares.get("title"));
                    w.setImg(wares.get("img")==null?null:(String)wares.get("img"));
                    w.setDImg(wares.get("dImg")==null?null:(String)wares.get("dImg"));
                    w.setDescription(wares.get("description")==null?null:(String)wares.get("description"));
                    w.setAttributeList(wares.get("attributeList")==null?null:(String)wares.get("attributeList"));*/
                    wares.setStore(store);
                    wares.setWaresType(waresType);


                    wares.setAttributeList(attributeValue);


                    waresService.save(wares);
                }

                /*PrivateValue p=privateValueService.selectByWaresId(wares.getId());
                if(p!=null){
                    p.setAttributeValue(attributeValue);
                    privateValueService.updateById(p);
                }else{
                    PrivateValue pValue=new PrivateValue();
                    pValue.setWaresId(wares.getId());
                    pValue.setAttributeValue(attributeValue);
                    privateValueService.insertPrivateValue(pValue);
                }*/
                if(waresSpecList.size()>0){
                    List<String> specIdList=new ArrayList<>();
                    for (Map<String,Object> waresSpecs:waresSpecList){
                        WaresSpecs waresSpecsl=new WaresSpecs();
                        waresSpecsl.setId(waresSpecs.get("id")==null?null:(String)waresSpecs.get("id"));
                       String spec= waresSpecs.get("nameList")==null?null:(String)waresSpecs.get("nameList");
                        waresSpecsl.setWaresSpecs(spec);
                        String stock=waresSpecs.get("stock")==null?null:(String)waresSpecs.get("stock");
                        waresSpecsl.setWaresStock(stock);
                        String price=waresSpecs.get("price")==null?null:(String)waresSpecs.get("price");
                        waresSpecsl.setWaresPrice(price);
                        waresSpecsl.setWares(wares);
                        waresSpecsService.save(waresSpecsl);
                        specIdList.add(waresSpecsl.getId());

                    }
                    Map<String,Object> m=new HashMap<>();
                    m.put("waresId",wares.getId());
                    m.put("ids",specIdList);
                    waresSpecsService.deleteByWaresId(m);
                }
                j.setMsg("成功");
                j.setSuccess(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return j;
    }



    @ApiOperation(notes = "appGetWaresByNameIsAdd", httpMethod = "DELETE", value = "判断商品是否已经添加过")
    @ApiImplicitParams({
            @ApiImplicitParam(name="store_id",value = "商家id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="wares_type_id",value = "分类id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="name",value = "商品名称",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appGetWaresByNameIsAdd")
    public AjaxJson appGetWaresByNameIsAdd(String store_id,String wares_type_id,String name){
        AjaxJson j=new AjaxJson();

        if(null==store_id || "".equals(store_id) || null==wares_type_id || "".equals(wares_type_id) ||  null==name || "".equals(name)){
            j.setMsg("参数错误");
            j.setSuccess(false);
            j.setErrorCode("555");
            return j;
        }
        Wares wares=new Wares();
        wares.setName(name);
        wares.setWaresType(waresTypeService.get(wares_type_id));
        wares.setStore(storeService.get(store_id));
        List<Wares> list=waresService.findList(wares);

        if(list.size()>0){
            j.setErrorCode("111");
            j.setSuccess(false);
            j.setMsg("此商品已经添加过,请勿重新添加");
        }else{
            j.setMsg("可以添加");
            j.setSuccess(true);
            j.setErrorCode("666");
        }
        return j;
    }




    @ApiOperation(notes = "appDeleteWares", httpMethod = "DELETE", value = "删除商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name="wares_id",value = "商品id",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appDeleteWares")
    public AjaxJson appDeleteWares(String wares_id){

        AjaxJson j=new AjaxJson();

        Wares wares=waresService.get(wares_id);

        waresService.delete(wares);
        //级联删除商品规格
        WaresSpecs waresSpecs=new WaresSpecs();
        waresSpecs.setWares(wares);
        List<WaresSpecs> list=waresSpecsService.findList(waresSpecs);
        for (WaresSpecs w:
             list) {

            waresSpecsService.delete(w);
        }
        j.setMsg("删除成功");
        j.setSuccess(true);

        return j;

    }

    @ApiOperation(notes = "appUpOrDownWares", httpMethod = "DELETE", value = "上架/下架商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name="wares_id",value = "商品id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="type",value = "0:下架1:上架",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appUpOrDownWares")
    public AjaxJson appUpOrDownWares(String wares_id,String type){
        AjaxJson j=new AjaxJson();

        Wares wares=waresService.get(wares_id);

        if(type.equals("0")){
            //下架
        wares.setIsShang("0");
        waresService.save(wares);
        j.setSuccess(true);
        j.setMsg("下架成功");
        j.setErrorCode("666");
        } else if(type.equals("1")){
            //上架
            wares.setIsShang("1");
            waresService.save(wares);
            j.setSuccess(true);
            j.setMsg("上架成功");
            j.setErrorCode("666");
        }
        return j;
    }



    @ApiOperation(notes = "appGetWaresById", httpMethod = "GET", value = "根据商品id获取详情/商家优惠/商品优惠")
    @ApiImplicitParams({
            @ApiImplicitParam(name="wares_id",value = "商品id",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appGetWaresById")
    public AjaxJson appGetWaresById(String wares_id){
        AjaxJson j=new AjaxJson();
        Wares wares=waresService.get(wares_id);
        if(null == wares){
            j.setSuccess(false);
            j.setMsg("查询失败");
            j.setErrorCode("111");
        }else {
            //获取商品优惠
            StoreDiscount storeDiscount=new StoreDiscount();
            //开启
            storeDiscount.setYesNo("1");
            storeDiscount.setWares(wares);
            List<StoreDiscount> list=storeDiscountService.findList(storeDiscount);


            String id=wares.getStore().getId();
            Store store=storeService.get(id);
            //获取店铺优惠
            StoreDiscount storeDiscount1=new StoreDiscount();
            //开启
            storeDiscount1.setYesNo("1");
            storeDiscount1.setStore(store);
            List<StoreDiscount> list1=storeDiscountService.findList(storeDiscount1);
            j.put("data",wares);
            if(list.size()>0){
                j.put("waresy",list.get(0));
            }else{
                j.put("waresy",null);
            }
            if(list1.size()>0){
                j.put("storey",list1.get(0));
            }else{
                j.put("storey",null);
            }


            j.setMsg("查询成功");
            j.setSuccess(true);
            j.setErrorCode("666");
        }
        return j;

    }

    public static List processData(String s)throws Exception{
        JSONArray objects=JSON.parseArray(s);
        JsonNode jsonNode = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readTree(s);

        List<String> ss= new ArrayList();
        for (int i=0; i <jsonNode.size() ; i++) {
            ss.add(jsonNode.get(i).toString());
            System.out.println("xxx");
        }

//        for (String value : values) {
//            JSONObject jsonObject = new JSONObject(16,true);
//            jsonObject.put(key, value);
//
//            for (String s1 : map.keySet()) {
//                if (key.equals(s1)) {
//                    continue;
//                }
//                values = map.get(s1);
//                for (String value1 : values) {
//                    jsonObject.put(s1, value1);
//                    ss.add(jsonObject.toJSONString());
//                    System.out.println(jsonObject.toJSONString());
//                }
//            }
//        }
        return ss;
    }


    public  void AddWaresSpecs(List list,Wares wares){
        for (int i=0; i <list.size() ; i++) {
            String o=list.get(i).toString();
            LinkedHashMap<String, Object> json = JSON.parseObject(o,LinkedHashMap.class, Feature.OrderedField);
            JSONObject jsonObject=new JSONObject(true);
            jsonObject.putAll(json);
            String price=String.valueOf(jsonObject.get("价格"));
            String num=String.valueOf(jsonObject.get("数量"));
            jsonObject.remove("价格");
            jsonObject.remove("数量");
            WaresSpecs waresSpecss=new WaresSpecs();
            waresSpecss.setWares(wares);
            waresSpecss.setWaresSpecs(String.valueOf(jsonObject));
            waresSpecss.setWaresPrice(price);
            waresSpecss.setWaresStock(num);
            waresSpecsService.save(waresSpecss);
        }
    }

}
