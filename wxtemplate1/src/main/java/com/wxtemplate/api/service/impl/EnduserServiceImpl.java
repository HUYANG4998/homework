package com.wxtemplate.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Enduser;
import com.wxtemplate.api.entity.User;
import com.wxtemplate.api.mapper.EnduserMapper;
import com.wxtemplate.api.service.IEnduserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxtemplate.api.util.Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-22
 */
@Service
public class EnduserServiceImpl extends ServiceImpl<EnduserMapper, Enduser> implements IEnduserService {

    @Resource
    private EnduserMapper enduserMapper;

    @Override
    public void updateUser(Enduser enduser) {
        if(enduser!=null){
            enduserMapper.updateById(enduser);
        }
    }

    @Override
    public Enduser findUser(String userid) {
        Enduser enduser=null;
        if(!StringUtils.isEmpty(userid)){
            enduser=enduserMapper.selectById(userid);
        }
        return enduser;
    }

    @Override
    public Enduser findUserName(String name) {
        Enduser enduser=null;
        if(!StringUtils.isEmpty(name)){
            enduser=enduserMapper.selectByName(name);
        }
        return enduser;
    }

    @Override
    public void addEndUser(Enduser user) {

        if(user!=null){

            enduserMapper.insert(user);
        }
    }

    @Override
    public Enduser findUserID(String userid) {
        Enduser user=new Enduser();
        if(!StringUtils.isEmpty(userid)){
            user=enduserMapper.selectById(userid);
        }
        return user;
    }

    @Override
    public void updateEndUser(Enduser user) {
        if(user!=null){
            enduserMapper.updateById(user);
        }
    }

    @Override
    public Enduser findUserPhone(String phone) {
        Enduser user=new Enduser();
        if(!StringUtils.isEmpty(phone)){
            user=enduserMapper.selectByPhone(phone);
        }
        return user;
    }

    @Override
    public void deleteEndUser(String userid) {
        if(!StringUtils.isEmpty(userid)){
            enduserMapper.deleteById(userid);
        }
    }

    @Override
    public List<Enduser> selectEndUser(String userid) {
        List<Enduser> enduserList=new ArrayList<>();
        if(!StringUtils.isEmpty(userid)){
            enduserList=enduserMapper.selectEndUser(userid);
        }
        return enduserList;
    }
}
