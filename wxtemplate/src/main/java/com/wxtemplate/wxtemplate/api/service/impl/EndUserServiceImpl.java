package com.wxtemplate.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.wxtemplate.api.entity.EndUser;
import com.wxtemplate.wxtemplate.api.mapper.EndUserMapper;
import com.wxtemplate.wxtemplate.api.service.IEndUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 后台用户 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-26
 */
@Service
public class EndUserServiceImpl extends ServiceImpl<EndUserMapper, EndUser> implements IEndUserService {

    @Resource
    private EndUserMapper endUserMapper;
    @Override
    public List<EndUser> selectEndUser(String userid) {
        List<EndUser> enduserList=new ArrayList<>();
        if(!StringUtils.isEmpty(userid)){
            enduserList=endUserMapper.selectEndUser(userid);
        }
        return enduserList;
    }

    @Override
    public void deleteEndUser(String userid) {
        if(!StringUtils.isEmpty(userid)){
            endUserMapper.deleteById(userid);
        }
    }

    @Override
    public EndUser findUserPhone(String userName) {
        EndUser user=null;
        if(!StringUtils.isEmpty(userName)){
            user=endUserMapper.selectByUserName(userName);
        }
        return user;
    }

    @Override
    public void addEndUser(EndUser user) {
        if(user!=null){
            endUserMapper.insert(user);
        }
    }

    @Override
    public void updateEndUser(EndUser user) {
        if(user!=null){
            user.setAddtime(DateUtil.now());
            user.setUpdatetime(DateUtil.now());
            endUserMapper.updateById(user);
        }
    }

    @Override
    public EndUser findUserID(String userid) {
        EndUser user=new EndUser();
        if(!StringUtils.isEmpty(userid)){
            user=endUserMapper.selectById(userid);
        }
        return user;
    }
}
