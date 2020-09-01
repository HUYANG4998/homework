package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Problem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 常见问题 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-08
 */
public interface ProblemMapper extends BaseMapper<Problem> {

    List<Problem> selectProductDes();
}
