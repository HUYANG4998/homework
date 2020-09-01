package com.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Carcolor;
import com.wxtemplate.api.entity.Imgs;
import com.wxtemplate.api.entity.Package;
import com.wxtemplate.api.mapper.CarcolorMapper;
import com.wxtemplate.api.mapper.ImgsMapper;
import com.wxtemplate.api.mapper.OfficialcarMapper;
import com.wxtemplate.api.mapper.PackageMapper;
import com.wxtemplate.api.service.IPackageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 套餐 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-16
 */
@Service
public class PackageServiceImpl extends ServiceImpl<PackageMapper, Package> implements IPackageService {

    @Resource
    private PackageMapper packageMapper;
    @Resource
    private CarcolorMapper carcolorMapper;
    @Resource
    private ImgsMapper imgsMapper;


    @Override
    public List<Map<String,Object>> selectPackage() {

        return packageMapper.selectPackage();
    }

    @Override
    public void deletePackage(String packageid) {

        if(!StringUtils.isEmpty(packageid)){
            packageMapper.deleteById(packageid);
            imgsMapper.deleteByObjId(packageid);
        }
    }

    @Override
    public void updatePackage(Map<String, Object> result) {
        Package p=new Package();
        p.setPackageid(result.get("packageid")==null?null:result.get("packageid").toString());
        p.setMainId(result.get("main_id")==null?null:result.get("main_id").toString());
        p.setMainColor(result.get("main_color")==null?null:result.get("main_color").toString());
        p.setDeputyId(result.get("deputy_id")==null?null:result.get("deputy_id").toString());
        p.setDeputyColor(result.get("deputy_color")==null?null:result.get("deputy_color").toString());
        p.setDeputyNumber(result.get("deputy_number")==null?null:result.get("deputy_number").toString());
        p.setPrice(result.get("price")==null?null:Double.valueOf(result.get("price").toString()));
        p.setUpdateTime(DateUtil.now());
        packageMapper.updateById(p);
        if(result.containsKey("imgurl")){
            List<String> imgurl=result.get("imgurl")==null?null:(List)result.get("imgurl");
            if(imgurl.size()>0){
                Imgs img=new Imgs();
                img.setImgurl(org.apache.commons.lang3.StringUtils.join(imgurl.toArray(),";"));
                img.setObjid(p.getPackageid());
                imgsMapper.updateByCarid(img);
            }

        }

    }

    @Override
    public void addPackage(Map<String, Object> result) {
        if(result!=null&&!result.isEmpty()){
            Package p=new Package();
            p.setMainId(result.get("main_id")==null?null:result.get("main_id").toString());
            p.setMainColor(result.get("main_color")==null?null:result.get("main_color").toString());
            p.setDeputyId(result.get("deputy_id")==null?null:result.get("deputy_id").toString());
            p.setDeputyColor(result.get("deputy_color")==null?null:result.get("deputy_color").toString());
            p.setDeputyNumber(result.get("deputy_number")==null?null:result.get("deputy_number").toString());
            p.setPrice(result.get("price")==null?null:Double.valueOf(result.get("price").toString()));
            p.setCreateTime(DateUtil.now());
            p.setUpdateTime(DateUtil.now());
            packageMapper.insert(p);
            if(result.containsKey("imgurl")){
                List<String> imgurl=result.get("imgurl")==null?null:(List)result.get("imgurl");
                if(imgurl.size()>0){
                    Imgs img=new Imgs();
                    img.setImgurl(org.apache.commons.lang3.StringUtils.join(imgurl.toArray(),";"));
                    img.setAddtime(DateUtil.now());
                    img.setObjid(p.getPackageid());
                    imgsMapper.insert(img);
                }

            }

        }
    }

    @Override
    public Map<String, Object> selectPackageById(String packageid) {
        Map<String, Object> packMap=new HashMap<>();
        if(!StringUtils.isEmpty(packageid)){
            packMap=packageMapper.selectPackageById(packageid);
            if(packMap!=null&&!packMap.isEmpty()) {
                if (packMap.containsKey("imgurl")) {
                    String imgurl = packMap.get("imgurl").toString();
                    List<String> list = Arrays.asList(imgurl.split(";"));
                    packMap.put("imgurl", list);
                }
            }
        }
        return packMap;
    }

    @Override
    public List<Carcolor> selectCarColor(String carid) {
        List<Carcolor> colorList=new ArrayList<>();
        if(!StringUtils.isEmpty(carid)){
            colorList=carcolorMapper.selectCarcolorByCarid(carid);
        }
        return colorList;
    }

    @Override
    public List<Map<String, Object>> selectPacckageFour() {
        return packageMapper.selectPacckageFour();
    }

    @Override
    public List<Map<String, Object>> selectWebPackage(Map<String,Object> map) {
        List<Map<String,Object>> packageList=new ArrayList<>();
        List<Map<String,Object>> packagesql=packageMapper.selectWebPackage(map);
        for (Map<String,Object> result:packagesql){
            String maincolor=result.get("main_color")==null?null:result.get("main_color").toString();
            String deputy_color=result.get("deputy_color")==null?null:result.get("deputy_color").toString();
            String deputy_number=result.get("deputy_number")==null?null:result.get("deputy_number").toString();
            Carcolor mainCarColor=carcolorMapper.selectById(maincolor);
            Carcolor deputyCarColor=carcolorMapper.selectById(deputy_color);
            if(mainCarColor.getInventory()>0&&deputyCarColor.getInventory()>=Integer.valueOf(deputy_number)){
                packageList.add(result);
            }
        }

        return packageList;
    }
}
