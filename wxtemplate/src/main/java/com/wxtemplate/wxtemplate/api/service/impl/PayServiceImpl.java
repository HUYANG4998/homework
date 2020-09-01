package com.wxtemplate.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxtemplate.wxtemplate.api.entity.MoneyRecord;
import com.wxtemplate.wxtemplate.api.entity.Pay;
import com.wxtemplate.wxtemplate.api.mapper.MoneyRecordMapper;
import com.wxtemplate.wxtemplate.api.mapper.PayMapper;
import com.wxtemplate.wxtemplate.api.pay.controller.WxPay;
import com.wxtemplate.wxtemplate.api.service.IPayService;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.SortedMap;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-07-18
 */
@Service
public class PayServiceImpl extends ServiceImpl<PayMapper, Pay> implements IPayService {


    @Resource
    private MoneyRecordMapper moneyRecordMapper;

    @Override
    public Result topUp(String userId, String money,String type) {
        if(!StringUtils.isEmpty(userId)&&!StringUtils.isEmpty(money)){
            MoneyRecord moneyRecord= new MoneyRecord();
            moneyRecord.setUserId(userId);
            moneyRecord.setMoney(new BigDecimal(money));
            moneyRecord.setAudit("0");
            moneyRecord.setWithdrawal("0");
            moneyRecord.setWay(type);
            moneyRecord.setAddtime(DateUtil.now());
            moneyRecordMapper.insert(moneyRecord);

            if("0".equals(type)){
                //支付宝
                String isSuccess = com.wxtemplate.wxtemplate.api.pay.controller.Pay.AppPay(money, moneyRecord.getMoneyRecordId());
                return Result.success(isSuccess);
            }else{
                //微信
                SortedMap<Object, Object> objectObjectSortedMap = WxPay.WxOrder(money, moneyRecord.getMoneyRecordId());
                return Result.success(objectObjectSortedMap);
            }
        }
        return Result.fail("充值失败");
    }
}
