package com.wxtemplate.wxtemplate.api.controller;


import com.alibaba.druid.util.StringUtils;
import com.wxtemplate.wxtemplate.api.entity.MoneyRecord;
import com.wxtemplate.wxtemplate.api.service.IMoneyRecordService;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 资金记录 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-27
 */
@RestController
@RequestMapping("/api/money-record")
@CrossOrigin
public class MoneyRecordController {

    @Autowired
    private IMoneyRecordService moneyRecordService;

    /**
     * 根据类型查询资金记录type0充值1提现
     * @param type
     * @return
     */
    @PostMapping("/selectMoneyRecordByType")
    public Result deleteUser(String type,String audit) {

        return moneyRecordService.selectMoneyRecordByType(type,audit);
    }

    /**
     * 根据资金id查询
     * @param moneyRecordId
     * @return
     */
    @PostMapping("/selctMoneyRecordByMoneyRecordId")
    public Result selctMoneyRecordByMoneyRecordId(String moneyRecordId) {
        if(!StringUtils.isEmpty(moneyRecordId)){
            return moneyRecordService.selctMoneyRecordByMoneyRecordId(moneyRecordId);
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 提现审核
     * @param moneyRecord
     * @return
     */
    @PostMapping("/withdrawalAudit")
    public Result withdrawalAudit(MoneyRecord moneyRecord) {
        if(moneyRecord!=null){
            return moneyRecordService.withdrawalAudit(moneyRecord);
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 用户提交充值或提现申请
     */
    @PostMapping(value = "/addAssetByWithdrawal")
    public Result selectMoneyRecord(MoneyRecord moneyRecord, HttpServletRequest request) {
        try {
            if (moneyRecord != null) {
                return moneyRecordService.selectMoneyRecord(moneyRecord);
            } else {
                return Result.fail("条件不足");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("addAssetByRechargeOrWithdrawal fail");
        }
    }
}
