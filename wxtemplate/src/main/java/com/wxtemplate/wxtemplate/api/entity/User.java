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
 * 用户表
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    public User() {
        this.userId = SnowflakeIdWorker.getSnowFlakerId();
    }

    /**
     * 用户id
     */
    @TableId("user_id")
    private String userId;

    /**
     * 推荐人用户id
     */
    private String lastUserId;

    /**
     * ID号
     */
    private String serNumber;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 微信号
     */
    private String wechat;

    /**
     * QQ号
     */
    private String qq;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 添加时间
     */
    private String addtime;

    /**
     * 修改时间
     */
    private String updatetime;

    /**
     * 余额
     */
    private BigDecimal money;

    /**
     * 每天发布次数
     */
    private Integer releaseNumber;

    /**
     * 每天刷新次数
     */
    private Integer refreshNumber;

    /**
     * 0普通会员1VIP会员
     */
    private String vip;

    /**
     * 地址
     */
    private String address;

    /**
     * VIP到期时间
     */
    private String vipExpirationTime;

    /**
     * 广告次数
     */
    private Integer advertisingNumber;

    /**
     * 权限0永久封号1开启2禁用
     */
    private String permission;

    /**
     * 二维码
     */
    private String qrCode;

    private String gender;

    private String birth;

    private String nation;

    private Integer age;

    private String contact;

    private String location;

    private String version;

    private Integer limits;

    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

}
