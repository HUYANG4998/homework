package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Package;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 套餐 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-16
 */
public interface PackageMapper extends BaseMapper<Package> {

    List<Map<String, Object>> selectPackage();

    Map<String, Object> selectPackageById(String packageid);

    List<Map<String, Object>> selectPacckageFour();

    List<Map<String, Object>> selectWebPackage(Map<String, Object> map);
}
