package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.FriendCircle;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface IFriendCircleService extends IService<FriendCircle> {

    void deleteFriendCircle(String friendCircleId);

    void insertFriendCircle(FriendCircle friendCircle);

    List<Map<String, Object>> selectFriendCircle(String userId,Integer pageNum,Integer pageSize);

    Map<String, Object> selectFriendCircleById(String friendCircleId, String userId);
}
