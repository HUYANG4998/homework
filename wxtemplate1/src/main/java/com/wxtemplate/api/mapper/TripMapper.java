package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Trip;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 行程 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-18
 */
public interface TripMapper extends BaseMapper<Trip> {

    List<Trip>  selectList(String userid);
}
