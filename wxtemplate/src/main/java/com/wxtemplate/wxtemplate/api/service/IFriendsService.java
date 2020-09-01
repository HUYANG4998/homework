package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.Friends;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 好友 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-21
 */
public interface IFriendsService extends IService<Friends> {

    List<Map<String, Object>> selectFriends(String userId,String nickName);

    void deleteFriendsById(String friendsId);
}
