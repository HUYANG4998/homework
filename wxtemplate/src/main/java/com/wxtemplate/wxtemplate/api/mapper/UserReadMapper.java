package com.wxtemplate.wxtemplate.api.mapper;

import com.wxtemplate.wxtemplate.api.entity.UserRead;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户读公告表 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-07-12
 */
public interface UserReadMapper extends BaseMapper<UserRead> {

    List<Map<String, Object>> myNotice(@Param("userId") String userId);
}
