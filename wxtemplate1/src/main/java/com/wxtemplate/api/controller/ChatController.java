package com.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Chat;
import com.wxtemplate.api.service.ICarouselService;
import com.wxtemplate.api.service.IChatService;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-04
 */
@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatController {

    @Autowired
    private IChatService chatService;
    /**查询跟我相关聊天信息*/
    @PostMapping(value = "/selectChatByServiceId")
    public Result selectChatByServiceId(HttpServletRequest request){
        String userid=(String)request.getAttribute("userid");
        try {
            List<Map<String,Object>> chatList= chatService.selectChatByServiceId(userid);
            return Result.success(chatList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectChatByServiceId fail");
        }
    }
    /**查询跟我相关聊天信息*/
    @PostMapping(value = "/selectChatByChatId")
    public Result selectChatByChatId(String chatid){

        try {
            if(!StringUtils.isEmpty(chatid)){
                Chat chat=chatService.selectChatByChatId(chatid);
                return Result.success(chat);
            }else{
                return Result.fail("chatid为空");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectChatByChatId fail");
        }
    }
    /**查询跟我相关聊天信息*/
    @PostMapping(value = "/selectChatBySereviceIdAndContentIsNotNull")
    public Result selectChatBySereviceIdAndContentIsNotNull(HttpServletRequest request){
        String userid=(String)request.getAttribute("userid");
        try {
            if(!StringUtils.isEmpty(userid)){
                List<String> contentList=chatService.selectChatBySereviceIdAndContentIsNotNull(userid);
                return Result.success(contentList);
            }else{
                return Result.fail("用户未登录");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectChatBySereviceIdAndContentIsNotNull fail");
        }
    }

}
