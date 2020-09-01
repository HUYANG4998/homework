package com.wxtemplate.wxtemplate.api.mapper;

import com.wxtemplate.wxtemplate.api.entity.FriendsInvite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 好友邀请 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-21
 */
public interface FriendsInviteMapper extends BaseMapper<FriendsInvite> {

    List<FriendsInvite> selectFriendsInvite(Map<String,Object> map);
}
