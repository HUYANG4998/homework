package com.wxtemplate.wxtemplate.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxtemplate.wxtemplate.api.entity.Version;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-25
 */
public interface VersionMapper extends BaseMapper<Version> {

    Version selectVersion();
}
