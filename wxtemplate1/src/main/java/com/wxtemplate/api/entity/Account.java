package com.wxtemplate.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.wxtemplate.api.util.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 账户表
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("account")
public class Account extends Model<Account> {

    private static final long serialVersionUID = 1L;
    public Account(){
        this.accountid= Utils.getUUID();
    }
    /**
     * 账户id
     */
    @TableId("accountid")
    private String accountid;

    /**
     * 用户id
     */
    private String userid;

    /**
     * 渠道 0微信1支付宝
     */
    private String ditch;

    /**
     * 账号
     */
    private String username;

    private String bank;

    private String name;

    /**
     * 收款码
     */
    private String moneyCode;

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
        return this.accountid;
    }

}
