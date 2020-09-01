package com.wxtemplate.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.wxtemplate.wxtemplate.api.entity.FriendCircle;
import com.wxtemplate.wxtemplate.api.mapper.AdvertisingMapper;
import com.wxtemplate.wxtemplate.api.mapper.DynamicMapper;
import com.wxtemplate.wxtemplate.api.mapper.FriendCircleMapper;
import com.wxtemplate.wxtemplate.api.mapper.FriendsMapper;
import com.wxtemplate.wxtemplate.api.service.IFriendCircleService;
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
 * @since 2020-05-18
 */
@Service
public class FriendCircleServiceImpl extends ServiceImpl<FriendCircleMapper, FriendCircle> implements IFriendCircleService {

    @Resource
    private FriendCircleMapper friendCircleMapper;
    @Resource
    private FriendsMapper friendsMapper;
    @Resource
    private AdvertisingMapper advertisingMapper;
    @Resource
    private DynamicMapper dynamicMapper;

    @Override
    public void deleteFriendCircle(String friendCircleId) {
        if(!StringUtils.isEmpty(friendCircleId)){
            FriendCircle friendCircle = friendCircleMapper.selectById(friendCircleId);
            if(friendCircle!=null){
                friendCircleMapper.deleteById(friendCircle);
            }
        }
    }

    @Override
    public void insertFriendCircle(FriendCircle friendCircle) {
        if(friendCircle!=null){
            friendCircle.setAddtime(DateUtil.now());
            friendCircleMapper.insert(friendCircle);
        }
    }

    @Override
    public List<Map<String, Object>> selectFriendCircle(String userId,Integer pageNum,Integer pageSize) {
        List<Map<String,Object>> list=new ArrayList<>();
        if(!StringUtils.isEmpty(userId)){
            //查询我的所有好友
            List<Map<String, Object>> friendsList = friendsMapper.selectFriends(userId,null);
            List<String> friends=new ArrayList<>();
            /*friends.add(userId);*/
            for (Map<String,Object> map:friendsList){
                String from_user_id = (String)map.get("from_user_id");
                String to_user_id = (String)map.get("to_user_id");
                if(userId.equals(from_user_id)){
                    friends.add(to_user_id);
                }else{
                    friends.add(from_user_id);
                }
            }
            //用所有好友进行查询
            PageHelper.startPage(pageNum, pageSize, true, false, true);
            list=dynamicMapper.selectDynamicByFriends(friends);

            if(list.size()>4){
                Map<String,Object> advertising = advertisingMapper.selectAdvertingRandom();
                if(advertising!=null){
                    list.add(5,advertising);
                }

            }
        }

        return list;
    }

    @Override
    public Map<String, Object> selectFriendCircleById(String friendCircleId, String userId) {
        Map<String,Object> result=new HashMap<>();
        if(!StringUtils.isEmpty(friendCircleId)&&!StringUtils.isEmpty(userId)){
            result=friendCircleMapper.selectFriendCircleById(friendCircleId);
        }
        return result;
    }
}
