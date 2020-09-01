package com.wxtemplate.wxtemplate.api.mapper;

import com.wxtemplate.wxtemplate.api.entity.ChatRecord;
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
public interface ChatRecordMapper extends BaseMapper<ChatRecord> {

    List<Map<String, Object>> selectPeopleMessageByUserId(@Param("userId") String userId);


    Map<String, Object> selectChatByUserIdAndGroupId(@Param("groupId") String groupId, @Param("userId") String userId);

    Map<String, Object> selectChatByUserIdAndFriendsUserId(@Param("userId") String userId, @Param("friendsUserId") String friendsUserId);

    Integer selectByFormAndGotoCount(@Param("userId") String userId, @Param("friendsUserId") String friendsUserId);

    Integer selectMessageNumber(String userId);

    List<Map<String, Object>> selectGroupChatContent(@Param("groupId") String groupId);

    List<Map<String, Object>> selectFriendsChatContent(@Param("fromUserId") String fromUserId, @Param("toId") String toId);

    //删除聊天信息
    void deleteByGroupChatId(String groupChatId);

    List<ChatRecord> selectChatRecordByUserIdAndFirendsUserIdIsRead(String userId, String friendsUserId);
}
