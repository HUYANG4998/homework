package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxtemplate.api.entity.Asset;

/**
 * <p>
 * 账户表 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-11
 */
public interface AccountMapper extends BaseMapper<Account> {

    Account selectAccountXHTByDitch(String ditch);

    Account selectAccountByUseridAndDitch(String userid, String ditch);
}
