package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Enduser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wxtemplate.api.entity.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-22
 */
public interface IEnduserService extends IService<Enduser> {

    void updateUser(Enduser user);

    Enduser findUser(String userid);

    Enduser findUserName(String name);

    void addEndUser(Enduser user);

    Enduser findUserID(String userid);

    void updateEndUser(Enduser user);

    Enduser findUserPhone(String phone);

    void deleteEndUser(String userid);

    List<Enduser> selectEndUser(String userid);
}
