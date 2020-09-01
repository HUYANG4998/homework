package com.wxtemplate.wxtemplate.api.controller;


import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageInfo;
import com.wxtemplate.wxtemplate.api.service.IChatRecordService;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  消息
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@RestController
@RequestMapping("/api/chat-record")
@CrossOrigin
public class ChatRecordController {

    @Autowired
    private IChatRecordService chatRecordService;

    /**
     * 获取聊天消息
     * @return
     */
    @PostMapping("/getMessage")
    public Result getMessage(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if(!StringUtils.isEmpty(userId)){
            List<Map<String,Object>> chatListMap=chatRecordService.getMessage(userId);
            return Result.success(chatListMap);
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 后台获取消息
     * @param request
     * @return
     */
    @PostMapping("/getEndMessage")
    public Result getEndMessage(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");

        if(!StringUtils.isEmpty(userId)){
            List<Map<String,Object>> chatListMap=chatRecordService.getEndMessage(userId);
            return Result.success(chatListMap);
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 删除消息
     * @param chat_record_id
     * @return
     */
    @PostMapping("/deleteMessage")
    public Result deleteMessage(String chat_record_id,HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if(!StringUtils.isEmpty(chat_record_id)&&!StringUtils.isEmpty(userId)){
            chatRecordService.deleteMessage(chat_record_id,userId);
            return Result.success("删除成功");
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 查询当前好友消息数量
     * @return
     */
    @PostMapping("/selectMessageNumber")
    public Result selectMessageNumber(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");

        if(!StringUtils.isEmpty(userId)){
            Integer number=chatRecordService.selectMessageNumber(userId);
            return Result.success(number);
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 查询消息内容
     * @param toId
     * @param status
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @PostMapping("/selectMessageContent")
    public Result selectMessageContent(String toId,String status, Integer pageNo,Integer pageSize,HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        pageNo=pageNo==null?1:pageNo;
        pageSize=pageSize==null?10:pageSize;
        if(!StringUtils.isEmpty(userId)&&!StringUtils.isEmpty(toId)){
            List<Map<String,Object>> listMap=chatRecordService.selectMessageContent(toId,status,userId,pageNo,pageSize);
            PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(listMap);
            return Result.success(pageInfo);
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 群聊消息设置已读
     * @param groupChatId
     * @return
     */
    @PostMapping("/setGroupChatIsRead")
    public Result setGroupChatIsRead(String groupChatId,HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if(!StringUtils.isEmpty(userId)&&!StringUtils.isEmpty(groupChatId)){
            chatRecordService.setGroupChatIsRead(userId,groupChatId);
            return Result.success("设置成功");
        }else{
            return Result.fail("参数异常");
        }
    }
    /**
     * 个人消息设置已读
     * @param friendsUserId
     * @return
     */
    @PostMapping("/setPeopleIsRead")
    public Result setPeopleIsRead(String friendsUserId,HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if(!StringUtils.isEmpty(userId)&&!StringUtils.isEmpty(friendsUserId)){
            chatRecordService.setPeopleIsRead(userId,friendsUserId);
            return Result.success("设置成功");
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 抢红包
     */
    @PostMapping("/robRedMoney")
    public Result robRedMoney(String chatRecordId,HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if(!StringUtils.isEmpty(userId)&&!StringUtils.isEmpty(chatRecordId)){
            return Result.success(chatRecordService.robRedMoney(userId,chatRecordId));
        }else{
            return Result.fail("参数异常");
        }
    }
    /**
     *
     */
    @PostMapping("/sendRedMoney")
    public Result sendRedMoney(String chatRecordId,Integer redNumber,BigDecimal redMoney,HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if(!StringUtils.isEmpty(userId)&&redNumber!=null&&redMoney!=null){
            return chatRecordService.sendRedMoney(chatRecordId,userId,redNumber,redMoney);

        }else{
            return Result.fail("参数异常");
        }
    }

}
