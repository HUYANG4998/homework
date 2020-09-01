package com.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Account;
import com.wxtemplate.api.mapper.AccountMapper;
import com.wxtemplate.api.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 账户表 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-11
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

    @Resource
    private AccountMapper accountMapper;

    @Override
    public Account selectAccountXHTByDitch(String type) {
        Account account=null;
        if(!StringUtils.isEmpty(type)){
            account=accountMapper.selectAccountXHTByDitch(type);
        }
        return account;
    }

    @Override
    public void addAccount(String moneyCode, String ditch) {
        if(!StringUtils.isEmpty(moneyCode)&&!StringUtils.isEmpty(ditch)){
            Account account=new Account();
            account.setUserid("xht_website_account");
            account.setMoneyCode(moneyCode);
            account.setDitch(ditch);
            account.setAddtime(DateUtil.now());
            account.setUpdatetime(DateUtil.now());
            accountMapper.insert(account);
        }
    }

    @Override
    public void updateAccount(String accountid, String moneyCode) {
        if(!StringUtils.isEmpty(accountid)&&!StringUtils.isEmpty(moneyCode)){
            Account account=new Account();
            account.setAccountid(accountid);
            account.setMoneyCode(moneyCode);
            account.setUpdatetime(DateUtil.now());
            accountMapper.updateById(account);
        }
    }
}
