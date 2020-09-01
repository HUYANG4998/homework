package com.wxtemplate.wxtemplate.api.mapper;

import com.wxtemplate.wxtemplate.api.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */

public interface UserMapper extends BaseMapper<User> {

    List<User> selectUser();

    Map<String, Object> selectBySerNumber(String id);

    User selectbyPhone(String phone);

    List<User> selectAllUser(@Param("phone") String phone,@Param("lastUserId")String lastUserId);

    Integer selectLikeAddTime(@Param("addtime") String addtime,@Param("vip") String vip);

    List<User> selectSubordinate(String userId);

}
