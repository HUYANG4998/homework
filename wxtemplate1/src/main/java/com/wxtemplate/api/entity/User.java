package com.wxtemplate.api.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

import com.wxtemplate.api.util.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User extends Model<User> {


    private static final long serialVersionUID = 1L;

    public User(){
        super();
        userid= Utils.getUUID();
    }
    @TableId("userid")
    private String userid;

    private String name;

    private String password;

    private String phone;

    private String headurl;

    private String addtime;

    private String updatetime;

    /**
     * 个人认证
     */
    private String realname;

    /**
     * 企业认证
     */
    private String license;

    /**
     * 余额
     * @return
     */
    private Double price;
    @TableField(strategy= FieldStrategy.IGNORED)
    private String cid;

    private String version;

    private String isuser;

    @Override
    protected Serializable pkVal() {
        return this.userid;
    }

}
