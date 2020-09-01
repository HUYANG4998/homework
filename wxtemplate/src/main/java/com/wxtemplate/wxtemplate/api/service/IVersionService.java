package com.wxtemplate.wxtemplate.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxtemplate.wxtemplate.api.entity.Version;
import com.wxtemplate.wxtemplate.tools.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-25
 */
public interface IVersionService extends IService<Version> {

    Version selectVersion();

    void updateVersion(Version version);

    void addVersion(Version version);

    void updateUserVersion(String userId, String version);

    Result contrastVersion(String version);
}
