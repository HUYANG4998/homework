package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Chat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-04
 */
public interface ChatMapper extends BaseMapper<Chat> {

    List<Map<String, Object>> selectChatByServiceId(String userid);

    Chat myIsServiceOrUser(String serviceid,String userid);

    Map<String, Object> selectUserAndService(String userid);

    List<Chat> selectChatBySereviceIdAndContentIsNotNull(String userid);

    void updateChatUserContent(String chatid);

    void updateChatServiceContent(String chatid);
}
