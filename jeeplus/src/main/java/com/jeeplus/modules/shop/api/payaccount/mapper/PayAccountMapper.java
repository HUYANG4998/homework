package com.jeeplus.modules.shop.api.payaccount.mapper;

import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.shop.api.payaccount.entity.PayAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface PayAccountMapper {
    PayAccount selectPayAccountByStatus(@Param("userId") String userId, @Param("status") String status);

    void addPayAccount(PayAccount payAccount);

    void updatePayAccount(PayAccount payAccount);

    void deletePayAccount(String payId);

    PayAccount selectPayAccountByPayId(String payId);
}
