package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.GroupChat;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 群聊 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface IGroupChatService extends IService<GroupChat> {

    void insertGroupChat(Map<String, Object> map,String userId);

    void deleteGroupChat(String groupChatId);

    Map<String,Object> selectGroupChatById(String groupChatId,String userId);

    void updateGroupChat(GroupChat groupChat);

    List<GroupChat> selectGroupChatCreateOrJoin(String userId, String type,Integer pageNo,Integer pageSize);

    List<Map<String, Object>> selectGroupMember(String groupChatId);

    List<Map<String, Object>> selectGroupMemberStaff(String groupChatId);

    void setAdmin(List<String> groupMemberIdList);

    void setBanned(List<String> groupMemberIdList);

    void exitGroupChat(List<String> groupMemberIdList);
}
