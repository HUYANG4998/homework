package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.FriendsInvite;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 好友邀请 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-21
 */
public interface IFriendsInviteService extends IService<FriendsInvite> {

    boolean insertFriends(List<FriendsInvite> friendsInviteList);

    List<Map<String,Object>> selectFriendsInvite(String userId,Integer pageNo,Integer pageSize);

    void inviteAudit(FriendsInvite friendsInvite);

    Map<String, Object> selectUserAndGroupChat(String id,String userId);

    void deleteInvite(String friendsInviteId);
}
