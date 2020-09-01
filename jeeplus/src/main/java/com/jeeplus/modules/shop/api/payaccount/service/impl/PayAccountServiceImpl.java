package com.jeeplus.modules.shop.api.payaccount.service.impl;

import cn.hutool.core.date.DateUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.shop.api.payaccount.entity.PayAccount;
import com.jeeplus.modules.shop.api.payaccount.mapper.PayAccountMapper;
import com.jeeplus.modules.shop.api.payaccount.service.PayAccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayAccountServiceImpl implements PayAccountService {

    @Resource
    private PayAccountMapper payAccountMapper;
    @Override
    public PayAccount selectPayAccountByStatus(String userid, String status) {
        PayAccount payAccount=null;
        if(!StringUtils.isEmpty(userid)){
            payAccount=payAccountMapper.selectPayAccountByStatus(userid,status);
        }
        return payAccount;
    }

    @Override
    public void addPayAccount(PayAccount payAccount) {
        if(payAccount!=null){
            payAccount.setUpdatetime(DateUtil.now());
            payAccount.setAddtime(DateUtil.now());
            payAccountMapper.addPayAccount(payAccount);
        }
    }

    @Override
    public void updatePayAccount(PayAccount payAccount) {

        if(payAccount!=null){
            payAccount.setUpdatetime(DateUtil.now());
            payAccountMapper.updatePayAccount(payAccount);
        }
    }

    @Override
    public void deletePayAccount(String payId) {
        if(!StringUtils.isEmpty(payId)){
            payAccountMapper.deletePayAccount(payId);
        }
    }

    @Override
    public PayAccount selectPayAccountByPayId(String payId) {
        PayAccount payAccount=null;
        if(!StringUtils.isEmpty(payId)){
            payAccount=payAccountMapper.selectPayAccountByPayId(payId);
        }
        return payAccount;
    }
}
