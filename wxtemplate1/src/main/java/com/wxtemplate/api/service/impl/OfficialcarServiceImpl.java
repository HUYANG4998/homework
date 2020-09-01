package com.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.wxtemplate.api.entity.Carcolor;
import com.wxtemplate.api.mapper.*;
import org.apache.commons.lang3.StringUtils;
import com.wxtemplate.api.entity.Brand;
import com.wxtemplate.api.entity.Imgs;
import com.wxtemplate.api.entity.Officialcar;
import com.wxtemplate.api.service.IOfficialcarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxtemplate.api.util.ChineseCharacterUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 平台车 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Service
public class OfficialcarServiceImpl extends ServiceImpl<OfficialcarMapper, Officialcar> implements IOfficialcarService {

    @Resource
    private OfficialcarMapper officialcarMapper;
    @Resource
    private BrandMapper brandMapper;
    @Resource
    private ImgsMapper imgsMapper;
    @Resource
    private CarcolorMapper carcolorMapper;
    @Resource
    private CommentuserMapper commentuserMapper;
    @Override
    public List<Map<String, Object>> selectofficialcar(Map<String, Object> map) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        listMap= officialcarMapper.selectofficialcar(map);
        return listMap;
    }

    @Override
    public List<Map<String, Object>> selectofficialcarByStatus(String status) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        if(!StringUtils.isEmpty(status)){
            listMap=officialcarMapper.selectofficialcarByStatus(status);
        }
        return listMap;
    }

    @Override
    public Map<String, Object> selectofficialcarByCarId(String carid) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(carid)){
            map= officialcarMapper.selectofficialcarByCarId(carid);
            if(map!=null&&!map.isEmpty()){
                if(map.containsKey("imgurl")){
                    String imgurl=map.get("imgurl").toString();
                    List<String> list = Arrays.asList(imgurl.split(";"));
                    map.put("imgurl",list);
                }
                List<Carcolor> carcolorList=carcolorMapper.selectCarColorByCarid(carid);
                if(carcolorList.size()>0){
                    map.put("carcolor",carcolorList);
                }
            }
        }
        return map;
    }

    @Override
    public void addOfficialcar(Map<String, Object> result) {
        if(!result.isEmpty()){
            Officialcar car=new Officialcar();
            Brand brand =new Brand();
            car.setCardesc(result.get("cardesc")==null?null:result.get("cardesc").toString());
            car.setOvertimeprice(result.get("overtimeprice")==null?null:Double.valueOf(result.get("overtimeprice").toString()));
            List<Map<String,Object>> carcolorList=result.get("carcolor")==null?null:(List<Map<String,Object>>)result.get("carcolor");
            if(!StringUtils.isEmpty(car.getCarid())){
                carcolorMapper.deleteCarColorByCarid(car.getCarid());
            }
            if(carcolorList.size()>0){
                for(Map map:carcolorList){
                    Carcolor carcolor=new Carcolor();
                    if(!StringUtils.isEmpty(car.getCarid())){
                        carcolor.setCarid(car.getCarid());
                    }
                    carcolor.setColor(map.get("color")==null?null:map.get("color").toString());
                    carcolor.setInventory(map.get("inventory")==null?null:Integer.valueOf(map.get("inventory").toString()));
                    carcolorMapper.insert(carcolor);
                }

            }
            car.setCarprice(result.get("carprice")==null?null:Double.valueOf(result.get("carprice").toString()));
            car.setCarmodel(result.get("carmodel")==null?null:result.get("carmodel").toString());
            car.setAddtime(DateUtil.now());
            car.setUpdatetime(DateUtil.now());
            car.setCarnumber(result.get("carnumber")==null?null:result.get("carnumber").toString());
            String cartype=result.get("cartype")==null?null:result.get("cartype").toString();
            car.setCartype(cartype);
            if(cartype!=null){
                cartype=ChineseCharacterUtil.getUpperCase(cartype,false).substring(0,1);
                brand.setFirstletter(cartype);
            }


            car.setSumnum(result.get("sumnum")==null?null:result.get("sumnum").toString());
            car.setSales(0);
            car.setSeat(result.get("seat")==null?null:result.get("seat").toString());
            car.setType(result.get("type")==null?null:result.get("type").toString());
            String status=result.get("status")==null?null:result.get("status").toString();
            car.setStatus(status);

            /**添加品牌*/
            brand.setAddtime(DateUtil.now());
            brand.setBrandname(car.getCartype());
            brand.setUpdatetime(DateUtil.now());
            brandMapper.insert(brand);
            car.setBrandid(brand.getBrandid());
            officialcarMapper.insert(car);
            if(result.containsKey("imgurl")){
                Imgs img=new Imgs();
                img.setAddtime(DateUtil.now());
                img.setObjid(car.getCarid());
                List<String> list=result.get("imgurl")==null?null:(List)result.get("imgurl");
                img.setImgurl(StringUtils.join(list.toArray(),";"));
                imgsMapper.insert(img);
            }


        }
    }

    @Override
    public void deleteOfficialcar(String carid) {
        if(!StringUtils.isEmpty(carid)){
            Officialcar car=officialcarMapper.selectById(carid);
            brandMapper.deleteById(car.getBrandid());
            imgsMapper.deleteByObjId(car.getCarid());
            officialcarMapper.deleteById(car.getCarid());
            carcolorMapper.deleteCarColorByCarid(carid);
        }
    }

    @Override
    public List<Map<String, Object>> selectAllofficialcar(String cartype) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        listMap= officialcarMapper.selectAllofficialcar(cartype);
        return listMap;
    }

    @Override
    public List<Map<String, Object>> selectofficialcarComment() {

        return commentuserMapper.selectofficialcarComment();
    }

    @Override
    public void updateOfficialcar(Map<String, Object> result) {
        if(!result.isEmpty()){
            Officialcar car=new Officialcar();
            Brand brand =new Brand();
            car.setCarid(result.get("carid")==null?null:result.get("carid").toString());
            car.setCardesc(result.get("cardesc")==null?null:result.get("cardesc").toString());
            List<Map<String,Object>> carcolorList=result.get("carcolor")==null?null:(List<Map<String,Object>>)result.get("carcolor");
            /*if(!StringUtils.isEmpty(car.getCarid())){
                carcolorMapper.deleteCarColorByCarid(car.getCarid());
            }*/
            if(carcolorList.size()>0){
                List<String> carcoloridList=new ArrayList<>();
                for(Map map:carcolorList){
                    String carcolorid=map.get("carcolorid")==null?null:map.get("carcolorid").toString();
                    if(!StringUtils.isEmpty(carcolorid)){
                        carcoloridList.add(carcolorid);
                    }
                }
                /**临时Map*/
                Map<String,Object> m=new HashMap<>();
                m.put("carcoloridList",carcoloridList);
                m.put("carid",car.getCarid());
                carcolorMapper.deleteCarcolorOther(m);
                for(Map map:carcolorList){
                    String carcolorid=map.get("carcolorid")==null?null:map.get("carcolorid").toString();
                    Carcolor carcolor=new Carcolor();
                    if(!StringUtils.isEmpty(carcolorid)){
                        carcolor=carcolorMapper.selectById(carcolorid);
                    }
                    if(!StringUtils.isEmpty(carcolor.getCarid())){
                        carcolor.setColor(map.get("color")==null?null:map.get("color").toString());
                        carcolor.setInventory(map.get("inventory")==null?0:Integer.valueOf(map.get("inventory").toString()));
                        carcolorMapper.updateById(carcolor);
                    }else{
                        carcolor.setCarid(result.get("carid")==null?null:result.get("carid").toString());
                        carcolor.setColor(map.get("color")==null?null:map.get("color").toString());
                        carcolor.setInventory(map.get("inventory")==null?0:Integer.valueOf(map.get("inventory").toString()));
                        carcolorMapper.insert(carcolor);
                    }

                }

            }
            car.setOvertimeprice(result.get("overtimeprice")==null?null:Double.valueOf(result.get("overtimeprice").toString()));
            car.setCarprice(result.get("carprice")==null?null:Double.valueOf(result.get("carprice").toString()));
            car.setCarmodel(result.get("carmodel")==null?null:result.get("carmodel").toString());
            car.setUpdatetime(DateUtil.now());
            car.setCarnumber(result.get("carnumber")==null?null:result.get("carnumber").toString());
            car.setBrandid(result.get("brandid")==null?null:result.get("brandid").toString());
            String cartype=result.get("cartype")==null?null:result.get("cartype").toString();
            car.setCartype(cartype);
            if(cartype!=null){
                cartype=ChineseCharacterUtil.getUpperCase(cartype,false).substring(0,1);
                brand.setFirstletter(cartype);
            }


            car.setSumnum(result.get("sumnum")==null?null:result.get("sumnum").toString());
            car.setSeat(result.get("seat")==null?null:result.get("seat").toString());
            car.setType(result.get("type")==null?null:result.get("type").toString());
            String status=result.get("status")==null?null:result.get("status").toString();
            car.setStatus(status);

            /**添加品牌*/
            brand.setBrandid(car.getBrandid());
            brand.setBrandname(car.getCartype());
            brand.setUpdatetime(DateUtil.now());
            brandMapper.updateById(brand);
            car.setBrandid(brand.getBrandid());
            officialcarMapper.updateById(car);
            if(result.containsKey("imgurl")){
                Imgs imgs=imgsMapper.selectByObjId(car.getCarid());
                if(imgs!=null){
                    List<String> list=result.get("imgurl")==null?null:(List)result.get("imgurl");
                    imgs.setImgurl(StringUtils.join(list.toArray(),";"));
                    imgsMapper.updateByCarid(imgs);
                }else{
                    Imgs img=new Imgs();
                    img.setObjid(car.getCarid());
                    List<String> list=result.get("imgurl")==null?null:(List)result.get("imgurl");
                    img.setImgurl(StringUtils.join(list.toArray(),";"));
                    img.setAddtime(DateUtil.now());
                    imgsMapper.insert(img);
                }


            }
        }

    }
}
