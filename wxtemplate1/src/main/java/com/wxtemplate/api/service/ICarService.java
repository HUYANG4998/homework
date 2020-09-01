package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Car;
import com.wxtemplate.api.entity.VO.CarVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 个人车 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
public interface ICarService extends IService<Car> {

    List<Map<String,Object>> selectMyCar(Map<String,Object> map);

    List<Map<String, Object>> selectAllMyCar(String carnumber);

    Map<String, Object> selectMyCarByCarid(String carid);

    void carAudit(Map<String, Object> map);
}
