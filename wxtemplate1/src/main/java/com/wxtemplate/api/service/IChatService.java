package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Chat;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-04
 */
public interface IChatService extends IService<Chat> {

    List<Map<String, Object>> selectChatByServiceId(String userid);

    Chat selectChatByChatId(String chatid);

    Map<String,Object> getUserAndService(String userid);

    List<String> selectChatBySereviceIdAndContentIsNotNull(String userid);
}
