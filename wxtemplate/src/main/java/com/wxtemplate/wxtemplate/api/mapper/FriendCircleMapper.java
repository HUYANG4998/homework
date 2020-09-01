package com.wxtemplate.wxtemplate.api.mapper;

import com.wxtemplate.wxtemplate.api.entity.FriendCircle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface FriendCircleMapper extends BaseMapper<FriendCircle> {

    Map<String, Object> selectFriendCircleById(@Param("friendCircleId") String friendCircleId);

    List< Map<String, Object>> selectFriendCircle(List<String> list);
}
