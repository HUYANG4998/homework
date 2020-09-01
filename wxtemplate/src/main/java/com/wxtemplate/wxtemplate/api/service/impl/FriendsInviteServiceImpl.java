package com.wxtemplate.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.wxtemplate.wxtemplate.api.entity.*;
import com.wxtemplate.wxtemplate.api.mapper.*;
import com.wxtemplate.wxtemplate.api.service.IFriendsInviteService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 好友邀请 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-21
 */
@Service
public class FriendsInviteServiceImpl extends ServiceImpl<FriendsInviteMapper, FriendsInvite> implements IFriendsInviteService {

    @Resource
    private FriendsInviteMapper friendsInviteMapper;
    @Resource
    private GroupChatMapper groupChatMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private GroupMemberMapper groupMemberMapper;
    @Resource
    private GroupChatReadMapper groupChatReadMapper;
    @Resource
    private FriendsMapper friendsMapper;

    @Override
    public boolean insertFriends(List<FriendsInvite> friendsInviteList) {
        boolean isSuccess=true;
        if(friendsInviteList!=null&&friendsInviteList.size()>0){
            for (FriendsInvite friendsInvite:friendsInviteList){


                if(!StringUtils.isEmpty(friendsInvite.getGroupChatId())){
                    Integer groupCount = groupMemberMapper.selectChatByUserIdAndGroupId(friendsInvite.getToUserId(), friendsInvite.getGroupChatId());
                    if(groupCount >0 ){
                        isSuccess=false;
                        break;
                    }
                }else{
                    Integer friendCount = friendsMapper.selectFriendsByUserIdAndFriendsId(friendsInvite.getFromUserId(), friendsInvite.getToUserId());
                    if(friendCount >0){
                        isSuccess=false;
                        break;
                    }
                }


                friendsInvite.setAddtime(DateUtil.now());
                friendsInvite.setUpdatetime(DateUtil.now());
                friendsInvite.setAudit("0");
                friendsInviteMapper.insert(friendsInvite);
            }
        }
        return isSuccess;
    }

    @Override
    public List<Map<String,Object>> selectFriendsInvite(String userId,Integer pageNo,Integer pageSize) {
        List<Map<String,Object>> friendsMapList=new ArrayList<>();
        if(!StringUtils.isEmpty(userId)){
            //查询我的群
            List<String> groupChatIdList=groupChatMapper.selectByUserId(userId);
            Map<String,Object> result=new HashMap<>();
            result.put("userId",userId);
            result.put("list",groupChatIdList);
            PageHelper.startPage(pageNo, pageSize, true, false, true);
            List<FriendsInvite> friendsInviteList=friendsInviteMapper.selectFriendsInvite(result);
            if(friendsInviteList!=null&&friendsInviteList.size()>0){

                for (FriendsInvite friendsInvite:friendsInviteList){
                    Map<String,Object> map=new HashMap<>();
                    if(friendsInvite!=null){
                        map.put("friends",friendsInvite);
                        if("0".equals(friendsInvite.getType())){
                            GroupChat groupChat = groupChatMapper.selectById(friendsInvite.getGroupChatId());

                            map.put("group",groupChat);

                            User user = userMapper.selectById(friendsInvite.getFromUserId());
                            map.put("fromUser",user);
                            if(!StringUtils.isEmpty(friendsInvite.getToUserId())){
                                User user1=userMapper.selectById(friendsInvite.getToUserId());
                                map.put("toUser",user1);
                            }else{
                                map.put("toUser",null);
                            }


                        }else{
                            User user = userMapper.selectById(friendsInvite.getFromUserId());
                            map.put("fromUser",user);
                            map.put("group",null);

                        }
                    }

                    friendsMapList.add(map);
                }
            }
        }
        return friendsMapList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inviteAudit(FriendsInvite friendsInvite) {

        if(friendsInvite!=null){
            FriendsInvite friendsInvite1 = friendsInviteMapper.selectById(friendsInvite.getFriendsInviteId());
            if(friendsInvite1!=null){
                friendsInvite1.setAudit(friendsInvite.getAudit());
                if("1".equals(friendsInvite.getAudit())){
                    if("0".equals(friendsInvite1.getType())){
                        Integer groupCount = groupMemberMapper.selectChatByUserIdAndGroupId(friendsInvite1.getToUserId(), friendsInvite1.getGroupChatId());
                        if(groupCount >0){
                            return;
                        }
                        //群聊邀请
                        String userId=null;
                        if(!StringUtils.isEmpty(friendsInvite1.getToUserId())){
                            userId=friendsInvite1.getToUserId();
                        }else{
                            userId=friendsInvite1.getFromUserId();
                        }
                        //加入群成员
                        GroupMember groupMember=new GroupMember();
                        groupMember.setGroupChatId(friendsInvite1.getGroupChatId());
                        groupMember.setUserId(userId);
                        groupMember.setPermission("2");
                        groupMember.setBanned("0");
                        groupMember.setAddtime(DateUtil.now());
                        groupMember.setUpdatetime(DateUtil.now());
                        groupMemberMapper.insert(groupMember);
                        //已读未读信息
                        GroupChatRead groupChatRead=new GroupChatRead();
                        groupChatRead.setUserId(userId);
                        groupChatRead.setGroupChatId(friendsInvite1.getGroupChatId());
                        groupChatRead.setIsRead(0);
                        groupChatRead.setAddtime(DateUtil.now());
                        groupChatRead.setUpdatetime(DateUtil.now());
                        groupChatReadMapper.insert(groupChatRead);
                    }else{
                        //好友邀请
                        //添加到好友列表
                        Integer count = friendsMapper.selectFriendsByUserIdAndFriendsId(friendsInvite1.getFromUserId(), friendsInvite1.getToUserId());
                        if(count==0){
                            User fromUser =userMapper.selectById(friendsInvite1.getFromUserId());
                            User toUser =userMapper.selectById(friendsInvite1.getToUserId());
                            Friends friends=new Friends();
                            friends.setFromUserId(friendsInvite1.getFromUserId());
                            friends.setToUserId(friendsInvite1.getToUserId());
                            friends.setFromNickName(fromUser.getNickname());
                            friends.setToNickName(toUser.getNickname());
                            friends.setAddtime(DateUtil.now());
                            friendsMapper.insert(friends);
                        }

                    }
                }
                friendsInviteMapper.updateById(friendsInvite1);

            }
        }
    }

    @Override
    public Map<String, Object> selectUserAndGroupChat(String id,String userId) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(id)){
            //搜索群聊
            map=groupChatMapper.selectGroupChatByGroupID(id);
            if(map!=null&&!map.isEmpty()){
                map.put("type","0");//0群聊
                Integer count = groupMemberMapper.selectChatByUserIdAndGroupId(userId, (String) map.get("group_chat_id"));
                map.put("friends",count);
            }else{
                //搜索用户
                map=userMapper.selectBySerNumber(id);
                if(map!=null&&!map.isEmpty()){
                    map.put("type","1");//1个人
                    Integer count = friendsMapper.selectFriendsByUserIdAndFriendsId(userId, (String) map.get("user_id"));
                    map.put("friends",count);

                }
            }

        }
        return map;
    }

    @Override
    public void deleteInvite(String friendsInviteId) {
        friendsInviteMapper.deleteById(friendsInviteId);
    }
}
