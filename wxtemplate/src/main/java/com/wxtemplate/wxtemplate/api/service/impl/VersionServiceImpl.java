package com.wxtemplate.wxtemplate.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxtemplate.wxtemplate.api.entity.User;
import com.wxtemplate.wxtemplate.api.entity.Version;
import com.wxtemplate.wxtemplate.api.mapper.UserMapper;
import com.wxtemplate.wxtemplate.api.mapper.VersionMapper;
import com.wxtemplate.wxtemplate.api.service.IVersionService;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-25
 */
@Service
public class VersionServiceImpl extends ServiceImpl<VersionMapper, Version> implements IVersionService {

    @Resource
    private VersionMapper versionMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public Version selectVersion() {

        return versionMapper.selectVersion();
    }

    @Override
    public void updateVersion(Version version) {
        if(version!=null){
            versionMapper.updateById(version);
        }
    }

    @Override
    public void addVersion(Version version) {
        if(version!=null){
            versionMapper.insert(version);
        }
    }

    @Override
    public void updateUserVersion(String userId, String version) {
        if(!StringUtils.isEmpty(userId)&&!StringUtils.isEmpty(version)){
            User user = userMapper.selectById(userId);
            if(user!=null){
                if(!version.equals(user.getVersion())){
                    user.setVersion(version);
                    userMapper.updateById(user);
                }
            }

        }
    }

    @Override
    public Result contrastVersion(String version) {

        if(!StringUtils.isEmpty(version)){
            Version version1 = versionMapper.selectVersion();
            if(version1!=null){
                if(!version.equals(version1.getVersionNumber())){
                    return Result.success(version1.getVersionFile());
                }else{
                    return Result.success();
                }

            }else{
                return Result.fail("暂无资源包");
            }
        }else{
            return Result.fail("参数错误");
        }

    }
}
