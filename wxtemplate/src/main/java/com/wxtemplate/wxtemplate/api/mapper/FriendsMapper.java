package com.wxtemplate.wxtemplate.api.mapper;

import com.wxtemplate.wxtemplate.api.entity.Friends;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 好友 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-21
 */
public interface FriendsMapper extends BaseMapper<Friends> {

    Integer selectFriendsByUserIdAndFriendsId(String userId, String friendsId);

    List<Map<String,Object>> selectFriends(@Param("userId") String userId,@Param("nickName") String nickName);
}
