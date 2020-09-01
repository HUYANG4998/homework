package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wxtemplate.wxtemplate.tools.Result;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface IUserService extends IService<User> {

    void startTime();

    List<Map<String, Object>> selectDynamicCollection(String userId,String type,Integer pageNo,Integer pageSize);

    Result register(String phone, String password,String code);

    Result SignIn(String phone, String password);

    Result forgetPassword(String phone, String code, String newPassword);

    List<User> selectAllUser(String phone,String lastUserId);

    Result renewalVip(String userId);

    Map<String, Object> selectIsFriendsUser(String userId, String myUserId);

    void deleteUser(String userId);

    void updateJurisdiction(User user);

    Map<String, Object> selectSevenDay(Integer number);

    Map<String, Object> selectCountUser();

    Result selectSubordinate(String userId,String index);

    Result selectMyReferees(String userId);

    List<Map<String, Object>> selectComment(String userId, Integer pageNo, Integer pageSize);
}
