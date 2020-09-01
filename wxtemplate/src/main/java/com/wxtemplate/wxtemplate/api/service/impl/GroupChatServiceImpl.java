package com.wxtemplate.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.wxtemplate.wxtemplate.api.entity.GroupChat;
import com.wxtemplate.wxtemplate.api.entity.GroupChatRead;
import com.wxtemplate.wxtemplate.api.entity.GroupMember;
import com.wxtemplate.wxtemplate.api.entity.User;
import com.wxtemplate.wxtemplate.api.mapper.*;
import com.wxtemplate.wxtemplate.api.service.IGroupChatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxtemplate.wxtemplate.api.util.QRCodeGenerator;
import com.wxtemplate.wxtemplate.api.util.RandomSix;
import com.wxtemplate.wxtemplate.api.util.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 群聊 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Service
public class GroupChatServiceImpl extends ServiceImpl<GroupChatMapper, GroupChat> implements IGroupChatService {

    @Resource
    private GroupChatMapper groupChatMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private GroupMemberMapper groupMemberMapper;
    @Resource
    private GroupChatReadMapper groupChatReadMapper;
    @Resource
    private ChatRecordMapper chatRecordMapper;
    @Value("${info.imgpath}")
    private String imgpath;
    @Value("${info.imgget}")
    private String imgget;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertGroupChat(Map<String, Object> map, String userId) {
        if (map != null && !map.isEmpty()) {
            List<String> userIdList = map.get("userIdList") == null ? null : (List<String>) map.get("userIdList");
            String groupName = map.get("groupName") == null ? null : (String) map.get("groupName");
            String groupNotice = map.get("groupNotice") == null ? null : (String) map.get("groupNotice");
            String groupHeadImage = map.get("groupHeadImage") == null ? null : (String) map.get("groupHeadImage");
            User user = userMapper.selectById(userId);
            /**数据穿插*/
            GroupChat groupChat = new GroupChat();
            groupChat.setGroupId(user.getSerNumber().substring(0, 2) + RandomSix.Num());
            groupChat.setUserId(userId);
            groupChat.setGroupName(groupName);
            groupChat.setGroupHeadImage(groupHeadImage);
            groupChat.setGroupNotice(groupNotice);

            groupChat.setAddtime(DateUtil.now());
            groupChat.setUpdatetime(DateUtil.now());
            String uuid = Utils.getUUID();
            groupChat.setGroupQrCode(imgget+uuid+".png");
            QRCodeGenerator.getBarCode(groupChat.getGroupChatId()+",0",imgpath+uuid+".png");
            /**添加群聊*/
            groupChatMapper.insert(groupChat);
            //将自己添加到成员中
            userIdList.add(userId);
            if (userIdList.size() > 0) {
                for (String userid : userIdList) {
                    /**将自己添加到群聊成员中*/
                    GroupMember groupMember = new GroupMember();
                    groupMember.setGroupChatId(groupChat.getGroupChatId());
                    groupMember.setUserId(userid);
                    if (userId.equals(userid)) {
                        groupMember.setPermission("0");
                    } else {
                        groupMember.setPermission("2");
                    }
                    groupMember.setBanned("0");
                    groupMember.setAddtime(DateUtil.now());
                    groupMember.setUpdatetime(DateUtil.now());
                    groupMemberMapper.insert(groupMember);
                    /**自己已读群聊信息*/
                    GroupChatRead groupChatRead = new GroupChatRead();
                    groupChatRead.setIsRead(0);
                    groupChatRead.setGroupChatId(groupChat.getGroupChatId());
                    groupChatRead.setUserId(userid);
                    groupChatRead.setAddtime(DateUtil.now());
                    groupChatRead.setUpdatetime(DateUtil.now());
                    groupChatReadMapper.insert(groupChatRead);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGroupChat(String groupChatId) {
        if (!StringUtils.isEmpty(groupChatId)) {
            GroupChat groupChat = groupChatMapper.selectById(groupChatId);
            if (groupChat != null) {
                //删除群聊
                groupChatMapper.deleteById(groupChatId);
                //删除群成员已读未读信息
                groupChatReadMapper.deleteByGroupChatId(groupChatId);
                //删除群成员
                groupMemberMapper.deleteByGroupChatId(groupChatId);
                //删除群聊天记录
                chatRecordMapper.deleteByGroupChatId(groupChatId);
            }
        }
    }

    @Override
    public Map<String,Object> selectGroupChatById(String groupChatId,String userId) {
        Map<String,Object> map=new HashMap<>();
        if (!StringUtils.isEmpty(groupChatId)&&!StringUtils.isEmpty(userId)) {
            GroupChat groupChat  = groupChatMapper.selectById(groupChatId);
            map.put("groupChat",groupChat);
            Integer count = groupMemberMapper.selectChatByUserIdAndGroupId(userId, groupChatId);
            map.put("isGroupChatMember",count);

        }
        return map;
    }

    @Override
    public List<Map<String, Object>> selectGroupMember(String groupChatId) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        if (!StringUtils.isEmpty(groupChatId)) {
            listMap=groupMemberMapper.selectGroupMemberByGroupChatId(groupChatId,null);
        }
        return listMap;
    }

    @Override
    public List<Map<String, Object>> selectGroupMemberStaff(String groupChatId) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        if (!StringUtils.isEmpty(groupChatId)) {
            listMap=groupMemberMapper.selectGroupMemberByGroupChatId(groupChatId,"2");
        }
        return listMap;
    }

    @Override
    public void updateGroupChat(GroupChat groupChat) {
        if(groupChat!=null){
            groupChatMapper.updateById(groupChat);
        }
    }

    @Override
    public List<GroupChat> selectGroupChatCreateOrJoin(String userId, String type,Integer pageNo,Integer pageSize) {
        List<GroupChat> groupChatList=new ArrayList<>();
        if(!StringUtils.isEmpty(userId)&&!StringUtils.isEmpty(type)){
            Map<String,Object> map=new HashMap<>();
            if("0".equals(type)){
                List<String> groupChatIdList = groupMemberMapper.selectGroupMemberByUserId(userId);
                if(groupChatIdList.size()>0){
                    map.put("userId",userId);
                    map.put("list",groupChatIdList);
                }
            }
            PageHelper.startPage(pageNo, pageSize, true, false, true);
            if(map!=null&&!map.isEmpty()){
                groupChatList=groupChatMapper.selectByUserIdAndGroupChatId(map);
            }else{
                groupChatList=groupChatMapper.selectGroupChatByUserId(userId);
            }
        }
        return groupChatList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setAdmin(List<String> groupMemberIdList) {
        if(groupMemberIdList!=null&&groupMemberIdList.size()>0){
            for (String groupMemberId:groupMemberIdList){
                GroupMember groupMember = groupMemberMapper.selectById(groupMemberId);
                if(groupMember!=null){
                    groupMember.setPermission("1");
                    groupMemberMapper.updateById(groupMember);
                }
            }
        }
    }

    @Override
    public void setBanned(List<String> groupMemberIdList) {
        if(groupMemberIdList!=null&&groupMemberIdList.size()>0){
            for (String groupMemberId:groupMemberIdList){
                GroupMember groupMember = groupMemberMapper.selectById(groupMemberId);
                if(groupMember!=null){
                    groupMember.setBanned("1");
                    groupMemberMapper.updateById(groupMember);
                }
            }
        }
    }

    @Override
    public void exitGroupChat(List<String> groupMemberIdList) {
        if(groupMemberIdList!=null&&groupMemberIdList.size()>0){
            for (String groupMemberId:groupMemberIdList){
                GroupMember groupMember = groupMemberMapper.selectById(groupMemberId);
                if(groupMember!=null){
                    GroupChatRead groupChatRead = groupChatReadMapper.selectByUserIdAndGroupChatId(groupMember.getUserId(), groupMember.getGroupChatId());
                    if(groupChatRead!=null){
                        groupChatReadMapper.deleteById(groupChatRead);
                    }
                    groupMemberMapper.deleteById(groupMember);
                }
            }
        }
    }
}
