package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wxtemplate.api.entity.Asset;

/**
 * <p>
 * 账户表 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-11
 */
public interface IAccountService extends IService<Account> {

    Account selectAccountXHTByDitch(String type);

    void addAccount(String moneyCode, String ditch);

    void updateAccount(String accountid, String moneyCode);
}
