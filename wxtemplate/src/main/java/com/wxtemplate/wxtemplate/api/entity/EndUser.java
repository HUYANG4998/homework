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
 * 后台用户
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("end_user")
public class EndUser extends Model<EndUser> {

    private static final long serialVersionUID = 1L;

    public EndUser() {
        this.endUserId = Utils.getUUID();
    }

    /**
     * 后台用户id
     */
    @TableId
    private String endUserId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 添加时间
     */
    private String addtime;

    /**
     * 修改时间
     */
    private String updatetime;

    /**
     * 权限
     *
     * @return
     */
    private String permission;


    @Override
    protected Serializable pkVal() {
        return this.endUserId;
    }

}
