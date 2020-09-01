package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Officialcar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 平台车 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
public interface OfficialcarMapper extends BaseMapper<Officialcar> {

    List<Map<String,Object>> selectofficialcar(Map<String,Object> map);

    List<Map<String,Object>> selectofficialcarByStatus(String status);

    Map<String,Object> selectofficialcarByCarId(String carid);

    List<Map<String, Object>> selectAllofficialcar(@Param("cartype") String cartype);

    /*List<Carcolor> selectCarColorByCarid(String carid);*/
}
