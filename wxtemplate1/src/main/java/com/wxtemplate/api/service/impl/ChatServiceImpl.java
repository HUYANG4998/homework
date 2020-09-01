package com.wxtemplate.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Chat;
import com.wxtemplate.api.mapper.ChatMapper;
import com.wxtemplate.api.service.IChatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-04
 */
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements IChatService {

    @Resource
    private ChatMapper chatMapper;
    @Override
    public List<Map<String, Object>> selectChatByServiceId(String userid) {
        List<Map<String,Object>> chatList=new ArrayList<>();
        if(!StringUtils.isEmpty(userid)){
            chatList=chatMapper.selectChatByServiceId(userid);
        }

        return chatList;
    }

    @Override
    public Chat selectChatByChatId(String chatid) {
        Chat chat=null;
        if(!StringUtils.isEmpty(chatid)){
            chat=chatMapper.selectById(chatid);

        }
        return chat;
    }

    @Override
    public Map<String, Object> getUserAndService(String userid) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(userid)){
            map=chatMapper.selectUserAndService(userid);
        }
        return map;
    }

    @Override
    public List<String> selectChatBySereviceIdAndContentIsNotNull(String userid) {
        List<String> contentList=new ArrayList<>();
        if(!StringUtils.isEmpty(userid)){
            List<Chat> chatList=chatMapper.selectChatBySereviceIdAndContentIsNotNull(userid);
            chatList.parallelStream().forEach((chat)->{
                if(!StringUtils.isEmpty(chat.getUsercontent())){
                    contentList.add(chat.getUsercontent());
                    chatMapper.updateChatUserContent(chat.getChatid());
                }
            });
        }
        return contentList;
    }
}
