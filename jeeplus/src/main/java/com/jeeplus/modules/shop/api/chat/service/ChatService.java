package com.jeeplus.modules.shop.api.chat.service;

import com.jeeplus.modules.shop.api.chat.entity.Chat;

import java.util.List;
import java.util.Map;

public interface ChatService {
    List<Map<String, Object>> selectByUseridAndStatus(String userid, String status);

    Map<String, Object> selectMessageByFormAndTo(Map<String,Object> map);

    void updateByChatId(Chat chat);

    Integer selectMessageNumber(String userid);

    Chat selectMessageById(String chatid);

    void insert(Chat chat);
}
