package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Officialcar;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 平台车 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
public interface IOfficialcarService extends IService<Officialcar> {

    List<Map<String,Object>> selectofficialcar(Map<String,Object> map);

    List<Map<String, Object>> selectofficialcarByStatus(String status);

    Map<String, Object> selectofficialcarByCarId(String carid);

    void addOfficialcar(Map<String, Object> result);

    void deleteOfficialcar(String carid);

    List<Map<String, Object>> selectAllofficialcar(String cartype);

    void updateOfficialcar(Map<String, Object> result);

    List<Map<String, Object>> selectofficialcarComment();
}
