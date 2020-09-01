package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.MoneyRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wxtemplate.wxtemplate.tools.Result;

/**
 * <p>
 * 资金记录 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-27
 */
public interface IMoneyRecordService extends IService<MoneyRecord> {

    Result selectMoneyRecordByType(String type,String audit);

    Result selctMoneyRecordByMoneyRecordId(String moneyRecordId);

    Result withdrawalAudit(MoneyRecord moneyRecord);

    void changeStatus(String out_trade_no);

    Result selectMoneyRecord(MoneyRecord moneyRecord);
}
