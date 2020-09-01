package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
public interface UserMapper extends BaseMapper<User> {

    /**根据手机号查询用户*/
    User selectUserByPhone(String phone);
    /**根据手机号码修改密码*/
    void updatePasswordByPhone(User user);

    User selectUser(String userid);

    List<User> selectUserList(@Param("phone") String phone);

    List<User> selectUserAll();
}
