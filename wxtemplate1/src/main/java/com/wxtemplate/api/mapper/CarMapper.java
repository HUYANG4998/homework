package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Car;
import com.wxtemplate.api.entity.VO.CarVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 个人车 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Mapper
public interface CarMapper extends BaseMapper<Car> {

    /**删除个人车辆*/
    public void deleteCarById(@Param("carid") String carid);
    /**查询我的车辆信息*/
    List<Map<String,Object>> selectMyCarByUserId(Map<String,Object> map);

    List<Map<String, Object>> selectCarByUserIdAndOrderId(@Param("userid") String userid, @Param("orderid")String orderid,@Param("type")String type);

    List<Map<String, Object>> selectCarByOrderId(Map<String,Object> map);

    List<Map<String, Object>> selectAllMyCar(@Param("carnumber") String carnumber);

    Map<String, Object> selectMyCarByCarid(String carid);

    List<Car> selectCarByNotMarrayTime(Map<String, Object> map);
}
