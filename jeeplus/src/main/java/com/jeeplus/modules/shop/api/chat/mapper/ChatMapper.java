package com.jeeplus.modules.shop.api.chat.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.shop.api.chat.entity.Chat;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisMapper
public interface ChatMapper  extends BaseMapper<Chat> {


    List<Map<String, Object>> selectByUseridAndStatus(@Param("userid") String userid, @Param("status") String status);

    Map<String, Object> selectByFormAndGoto(@Param("id") String id,@Param("userid") String userid);

    Map<String, Object> selectByFormAndGotoCostomer(@Param("id")String id, @Param("userid")String userid);

    Map<String, Object> selectByFormAndGotoStore(@Param("id")String id, @Param("userid")String userid);

    Map<String, Object> selectByFormAndGotoRider(@Param("id")String id, @Param("userid")String userid);

    Integer selectByFormAndGotoCount(@Param("id")String id, @Param("userid")String userid);

    List<Map<String, Object>> selectMessageByFormAndTo(@Param("userid") String userid, @Param("oppositeid") String oppositeid);

    void updateByChatId(Chat chat);

    Integer selectMessageNumber(String userid);

    Chat selectMessageById(String chatId);
}
