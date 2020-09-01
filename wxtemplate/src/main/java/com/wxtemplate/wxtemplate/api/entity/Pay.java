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
 *
 * </p>
 *
 * @author LIUWEI
 * @since 2020-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pay")
public class Pay extends Model<Pay> {

    private static final long serialVersionUID = 1L;


    public Pay() {
        this.payId = SnowflakeIdWorker.getSnowFlakerId();
    }

    /**
     * 支付
     */
    @TableId("pay_id")
    private String payId;

    private String userId;

    /**
     * 支付金额
     */
    private BigDecimal money;

    /**
     * 支付方式0支付宝1微信
     */
    private String type;

    /**
     * 支付状态0未支付1已支付
     */
    private String status;

    /**
     * 添加时间
     */
    private String addtime;


    @Override
    protected Serializable pkVal() {
        return this.payId;
    }

}
