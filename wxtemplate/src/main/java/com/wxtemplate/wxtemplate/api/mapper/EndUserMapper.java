package com.wxtemplate.wxtemplate.api.mapper;

import com.wxtemplate.wxtemplate.api.entity.EndUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 后台用户 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-26
 */
public interface EndUserMapper extends BaseMapper<EndUser> {

    List<EndUser> selectEndUser(String userId);

    EndUser selectByUserName(String userName);

    String selectRandomOne();
}
