package com.wxtemplate.wxtemplate.api.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxtemplate.wxtemplate.api.entity.Friends;
import com.wxtemplate.wxtemplate.api.entity.User;
import com.wxtemplate.wxtemplate.api.mapper.FriendsMapper;
import com.wxtemplate.wxtemplate.api.mapper.UserMapper;
import com.wxtemplate.wxtemplate.api.service.IFriendsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 好友 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-21
 */
@Service
public class FriendsServiceImpl extends ServiceImpl<FriendsMapper, Friends> implements IFriendsService {

    @Resource
    private FriendsMapper friendsMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public List<Map<String,Object>> selectFriends(String userId,String nickName) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        if(!StringUtils.isEmpty(userId)){
            listMap=friendsMapper.selectFriends(userId,nickName);
            if(listMap.size()>0){
                for (Map<String,Object> map:listMap){
                    String from_user_id = (String)map.get("from_user_id");
                    String to_user_id = (String)map.get("to_user_id");
                    User user=null;
                    if(userId.equals(from_user_id)){
                        user=userMapper.selectById(to_user_id);
                    }else{
                        user=userMapper.selectById(from_user_id);
                    }
                    map.put("user",user);
                }
            }

        }
        return listMap;
    }

    @Override
    public void deleteFriendsById(String friendsId) {
        if(!StringUtils.isEmpty(friendsId)){
            Friends friends = friendsMapper.selectById(friendsId);
            if(friends!=null){
                friendsMapper.deleteById(friends);
            }
        }
    }
}
