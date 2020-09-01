package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Carcolor;
import com.wxtemplate.api.entity.Package;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 套餐 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-16
 */
public interface IPackageService extends IService<Package> {

    List<Map<String,Object>> selectPackage();

    void deletePackage(String packageid);

    void updatePackage(Map<String, Object> result);

    void addPackage(Map<String, Object> result);

    Map<String, Object> selectPackageById(String packageid);

    List<Carcolor> selectCarColor(String carid);

    List<Map<String, Object>> selectPacckageFour();

    List<Map<String, Object>> selectWebPackage(Map<String,Object> map);
}
