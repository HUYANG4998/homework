package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Violation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-21
 */
public interface ViolationMapper extends BaseMapper<Violation> {

    Violation selectViolation();
}
