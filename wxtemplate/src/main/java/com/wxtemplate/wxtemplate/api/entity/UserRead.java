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
 * 用户读公告表
 * </p>
 *
 * @author LIUWEI
 * @since 2020-07-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_read")
public class UserRead extends Model<UserRead> {

    private static final long serialVersionUID = 1L;

    public UserRead() {
        this.userReadId = Utils.getUUID();
    }

    /**
     * 用户读id
     */
    @TableId("user_read_id")
    private String userReadId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 公告id
     */
    private String noticeId;

    /**
     * 是否已读0未读1已读
     */
    private Integer isRead;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
