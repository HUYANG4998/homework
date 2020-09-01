package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Enduser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-22
 */
public interface EnduserMapper extends BaseMapper<Enduser> {

    Enduser selectByName(String name);

    String selectRandomOne();

    Enduser selectByPhone(String phone);

    List<Enduser> selectEndUser(String userid);
}
