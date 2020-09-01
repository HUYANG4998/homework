package com.wxtemplate.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.wxtemplate.wxtemplate.api.entity.ChatRecord;
import com.wxtemplate.wxtemplate.api.entity.Earn;
import com.wxtemplate.wxtemplate.api.entity.GroupChatRead;
import com.wxtemplate.wxtemplate.api.entity.User;
import com.wxtemplate.wxtemplate.api.mapper.*;
import com.wxtemplate.wxtemplate.api.service.IChatRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxtemplate.wxtemplate.api.service.IDynamicService;
import com.wxtemplate.wxtemplate.api.util.BigDecimalUtils;
import com.wxtemplate.wxtemplate.api.util.RedPackageUtils;
import com.wxtemplate.wxtemplate.api.util.Sort;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Service
public class ChatRecordServiceImpl extends ServiceImpl<ChatRecordMapper, ChatRecord> implements IChatRecordService {

    @Resource
    private ChatRecordMapper chatRecordMapper;
    @Resource
    private GroupMemberMapper groupMemberMapper;
    @Resource
    private GroupChatReadMapper groupChatReadMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private EarnMapper earnMapper;
    @Resource
    private IDynamicService dynamicService;

    @Override
    public List<Map<String, Object>> getMessage(String userId) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        if (!StringUtils.isEmpty(userId)) {
            //查询与我关联的群
            List<String> groupIdList = groupMemberMapper.selectGroupMemberByUserId(userId);
            //查询群聊消息
            for (String groupId : groupIdList) {
                Map<String, Object> stringObjectMap = chatRecordMapper.selectChatByUserIdAndGroupId(groupId, userId);
                if (stringObjectMap != null && !stringObjectMap.isEmpty()) {
                    if (stringObjectMap.containsKey("del_flag")) {
                        if ("1".equals(stringObjectMap.get("del_flag"))) {

                            listMap.add(stringObjectMap);
                        }
                    }
                }

            }
            //查询个人消息
            List<Map<String, Object>> resultList = new ArrayList<>();
            List<String> userList = new ArrayList<>();
            resultList = chatRecordMapper.selectPeopleMessageByUserId(userId);
            for (Map<String, Object> map : resultList) {
                String from = map.get("from_user_id").toString();
                String to = map.get("to_id").toString();
                if (userId.equals(from)) {
                    if (!userList.contains(to)) {
                        userList.add(to);
                    }
                } else if (userId.equals(to)) {
                    if (!userList.contains(from)) {
                        userList.add(from);
                    }
                }
            }
            for (String friendsUserId : userList) {
                Map<String, Object> stringObjectMap = chatRecordMapper.selectChatByUserIdAndFriendsUserId(userId, friendsUserId);
                User user = userMapper.selectById(friendsUserId);
                stringObjectMap.put("friends", user);
                if (stringObjectMap != null && !stringObjectMap.isEmpty()) {
                    if (stringObjectMap.containsKey("del_flag")) {
                        if ("1".equals(stringObjectMap.get("del_flag"))) {
                            stringObjectMap.put("number", chatRecordMapper.selectByFormAndGotoCount(userId, friendsUserId));
                            listMap.add(stringObjectMap);
                        }
                    }
                }

            }
        }
        return Sort.ListSort(listMap);
    }

    @Override
    public void deleteMessage(String chat_record_id, String userId) {
        if (!StringUtils.isEmpty(chat_record_id)) {
            ChatRecord chatRecord = chatRecordMapper.selectById(chat_record_id);
            chatRecord.setDelFlag("0");
            chatRecord.setIsRead("1");
            if ("0".equals(chatRecord.getStatus())) {
                GroupChatRead groupChatRead = groupChatReadMapper.selectByUserIdAndGroupChatId(userId, chatRecord.getToId());
                groupChatRead.setIsRead(1);
                groupChatReadMapper.updateById(groupChatRead);
            }
            chatRecordMapper.updateById(chatRecord);
        }
    }

    @Override
    public Integer selectMessageNumber(String userId) {
        Integer number = 0;
        if (!StringUtils.isEmpty(userId)) {
            number += chatRecordMapper.selectMessageNumber(userId);
        }
        return number;
    }

    @Override
    public List<Map<String, Object>> selectMessageContent(String toId, String status, String userId, Integer pageNum, Integer pageSize) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        if (!StringUtils.isEmpty(toId) && !StringUtils.isEmpty(userId)) {
            PageHelper.startPage(pageNum, pageSize, true, false, true);
            if ("0".equals(status)) {
                //群聊信息内容
                listMap = chatRecordMapper.selectGroupChatContent(toId);
            } else {
                //个人信息
                listMap = chatRecordMapper.selectFriendsChatContent(userId, toId);
            }
        }
        return listMap;
    }

    @Override
    public void setGroupChatIsRead(String userId, String groupChatId) {
        if (!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(groupChatId)) {
            GroupChatRead groupChat = groupChatReadMapper.selectByUserIdAndGroupChatId(userId, groupChatId);
            if (groupChat != null) {
                groupChat.setIsRead(1);
                groupChatReadMapper.updateById(groupChat);
            }
        }
    }

    @Override
    public void setPeopleIsRead(String userId, String friendsUserId) {
        if (!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(friendsUserId)) {
            List<ChatRecord> chatRecords = chatRecordMapper.selectChatRecordByUserIdAndFirendsUserIdIsRead(userId, friendsUserId);
            for (ChatRecord chatRecord : chatRecords) {
                chatRecord.setIsRead("1");
                chatRecordMapper.updateById(chatRecord);
            }
        }
    }

    @Override
    public List<Map<String, Object>> getEndMessage(String userId) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        if (!StringUtils.isEmpty(userId)) {

            //查询个人消息
            List<Map<String, Object>> resultList = new ArrayList<>();
            List<String> userList = new ArrayList<>();
            resultList = chatRecordMapper.selectPeopleMessageByUserId(userId);
            for (Map<String, Object> map : resultList) {
                String from = map.get("from_user_id").toString();
                String to = map.get("to_id").toString();
                if (userId.equals(from)) {
                    if (!userList.contains(to)) {
                        userList.add(to);
                    }
                } else if (userId.equals(to)) {
                    if (!userList.contains(from)) {
                        userList.add(from);
                    }
                }
            }
            for (String friendsUserId : userList) {
                Map<String, Object> stringObjectMap = chatRecordMapper.selectChatByUserIdAndFriendsUserId(userId, friendsUserId);
                User user = userMapper.selectById(friendsUserId);
                stringObjectMap.put("nickname", user.getNickname());
                stringObjectMap.put("userId", user.getUserId());
                stringObjectMap.put("headImage", user.getHeadImage());
                if (stringObjectMap != null && !stringObjectMap.isEmpty()) {
                    if (stringObjectMap.containsKey("del_flag")) {
                        if ("1".equals(stringObjectMap.get("del_flag"))) {
                            stringObjectMap.put("number", chatRecordMapper.selectByFormAndGotoCount(userId, friendsUserId));
                            listMap.add(stringObjectMap);
                        }
                    }
                }

            }
        }
        return Sort.ListSort(listMap);
    }

    @Override
    public BigDecimal robRedMoney(String userId, String chatRecordId) {
        BigDecimal randomMoney = null;
        if (!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(chatRecordId)) {
            boolean isSuccess = dynamicService.judgeRedWars(chatRecordId, userId);
            if(!isSuccess){
                User user = userMapper.selectById(userId);
                ChatRecord chatRecord = chatRecordMapper.selectById(chatRecordId);
                randomMoney = RedPackageUtils.getRandomMoney(chatRecord);
                user.setMoney(BigDecimalUtils.add(user.getMoney(), randomMoney));
                chatRecordMapper.updateById(chatRecord);
                Earn earn=new Earn(userId,"抢红包",randomMoney,"1", DateUtil.now(),chatRecordId);
                earnMapper.insert(earn);
            }else{
                randomMoney=new BigDecimal(0);
            }

        }
        return randomMoney;
    }

    @Override
    public Result sendRedMoney(String chatRecordId,String userId, Integer redNumber, BigDecimal redMoney) {

        if (!StringUtils.isEmpty(userId) && redNumber != null && redMoney != null) {
            User user = userMapper.selectById(userId);
            BigDecimal multiply = BigDecimalUtils.multiply(new BigDecimal(redNumber), redMoney);
            if (user.getMoney().compareTo(multiply) > -1) {
                user.setMoney(BigDecimalUtils.subtract(user.getMoney(), multiply));
                return Result.success("支付成功");
            } else {
                return Result.fail("余额不足");
            }
        }
        return Result.fail("支付失败");
    }
}
