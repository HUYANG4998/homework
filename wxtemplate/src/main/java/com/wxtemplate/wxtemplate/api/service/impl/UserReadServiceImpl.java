package com.wxtemplate.wxtemplate.api.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.wxtemplate.wxtemplate.api.entity.UserRead;
import com.wxtemplate.wxtemplate.api.mapper.UserReadMapper;
import com.wxtemplate.wxtemplate.api.service.IUserReadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户读公告表 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-07-12
 */
@Service
public class UserReadServiceImpl extends ServiceImpl<UserReadMapper, UserRead> implements IUserReadService {

    @Resource
    private UserReadMapper userReadMapper;

    @Override
    public List<Map<String, Object>> myNotice(String userId) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        if(!StringUtils.isEmpty(userId)){
            listMap=userReadMapper.myNotice(userId);
        }
        return listMap;
    }

    @Override
    public void updateNotice(String userReadId) {
        if(!StringUtils.isEmpty(userReadId)){
            UserRead userRead = userReadMapper.selectById(userReadId);
            userRead.setIsRead(1);
            userReadMapper.updateById(userRead);
        }
    }
}
