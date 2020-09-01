package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Version;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
