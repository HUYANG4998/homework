package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Garag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车库 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
public interface GaragMapper extends BaseMapper<Garag> {

    List<Map<String,Object>> selectByUserId(String userid);
}
