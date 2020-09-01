package com.wxtemplate.wxtemplate.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.wxtemplate.wxtemplate.api.util.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 微信支付宝账号
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName
public class Account extends Model<Account> {

    private static final long serialVersionUID = 1L;
    public Account(){
        this.accountId= Utils.getUUID();
    }
    /**
     * 账号id
     */
    @TableId
    private String accountId;

    /**
     * 0微信1支付宝
     */
    private String payWay;

    /**
     * 姓名
     */
    private String name;

    /**
     * 账号
     */
    private String account;

    /**
     * 用户id
     */
    private String userId;

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
        return this.accountId;
    }

}
