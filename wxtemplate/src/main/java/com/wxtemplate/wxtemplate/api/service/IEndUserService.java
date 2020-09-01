package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.EndUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 后台用户 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-26
 */
public interface IEndUserService extends IService<EndUser> {

    List<EndUser> selectEndUser(String userid);

    void deleteEndUser(String userid);

    EndUser findUserPhone(String userName);

    void addEndUser(EndUser user);

    void updateEndUser(EndUser user);

    EndUser findUserID(String userid);
}
