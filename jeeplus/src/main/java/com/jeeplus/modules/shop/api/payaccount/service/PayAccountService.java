package com.jeeplus.modules.shop.api.payaccount.service;

import com.jeeplus.modules.shop.api.payaccount.entity.PayAccount;

import java.util.List;

public interface PayAccountService {
    PayAccount selectPayAccountByStatus(String userid, String status);

    void addPayAccount(PayAccount payAccount);

    void updatePayAccount(PayAccount payAccount);

    void deletePayAccount(String payId);

    PayAccount selectPayAccountByPayId(String payId);
}
