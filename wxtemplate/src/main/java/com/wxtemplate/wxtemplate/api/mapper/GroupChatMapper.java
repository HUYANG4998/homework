package com.wxtemplate.wxtemplate.api.mapper;

import com.wxtemplate.wxtemplate.api.entity.GroupChat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 群聊 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface GroupChatMapper extends BaseMapper<GroupChat> {

    Map<String, Object> selectGroupChatByGroupID(String id);

    List<String> selectByUserId(String userId);

    List<GroupChat> selectByUserIdAndGroupChatId(Map<String, Object> map);

    List<GroupChat> selectGroupChatByUserId(String userId);
}
