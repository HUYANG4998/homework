package com.wxtemplate.wxtemplate.api.mapper;

import com.wxtemplate.wxtemplate.api.entity.Advertising;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 广告表 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface AdvertisingMapper extends BaseMapper<Advertising> {

    List<Advertising> selectAllAdvertising(@Param("type") String type);

    List<Advertising> selectAdvertisingFive(String type);

    List<Advertising> selectAdvertingByUserId(String userId);

    Map<String,Object> selectAdvertingRandom();
}
