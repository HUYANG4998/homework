package com.wxtemplate.wxtemplate.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.wxtemplate.wxtemplate.api.util.SnowflakeIdWorker;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 资金记录
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName
public class MoneyRecord extends Model<MoneyRecord> {

    private static final long serialVersionUID = 1L;

    public MoneyRecord() {
        this.moneyRecordId = SnowflakeIdWorker.getSnowFlakerId();
    }

    /**
     * 资金记录id
     */
    @TableId
    private String moneyRecordId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 账号id
     */
    private String accountId;

    /**
     * 方式0微信1支付宝
     */
    private String way;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 审核0待审核1通过2不通过
     */
    private String audit;
    /**
     * 充值提现0充值1提现
     */
    private String withdrawal;

    /**
     * 添加时间
     */
    private String addtime;

    /**
     * 修改时间
     */
    private String updatetime;


    @Override
    protected Serializable pkVal() {
        return this.moneyRecordId;
    }

}
