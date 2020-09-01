package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.CarDecoration;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-24
 */
public interface CarDecorationMapper extends BaseMapper<CarDecoration> {

    List<CarDecoration> findDecoration();
}
