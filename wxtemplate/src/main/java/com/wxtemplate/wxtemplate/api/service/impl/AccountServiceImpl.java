package com.wxtemplate.wxtemplate.api.service.impl;

import com.wxtemplate.wxtemplate.api.entity.Account;
import com.wxtemplate.wxtemplate.api.mapper.AccountMapper;
import com.wxtemplate.wxtemplate.api.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 微信支付宝账号 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-27
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

}
