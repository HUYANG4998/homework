package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Promotebasic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 推广网站基本信息 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-08
 */
public interface PromotebasicMapper extends BaseMapper<Promotebasic> {

    Promotebasic selectBasic();
}
