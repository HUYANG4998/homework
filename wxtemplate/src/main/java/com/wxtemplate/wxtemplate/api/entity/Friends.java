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
 * 好友
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName
public class Friends extends Model<Friends> {

    private static final long serialVersionUID = 1L;

    public Friends() {
        this.friendsId = Utils.getUUID();
    }

    /**
     * 好友id
     */
    @TableId
    private String friendsId;

    /**
     * 我方用户id
     */
    private String fromUserId;

    /**
     * 对方用户id
     */
    private String toUserId;

    /**
     * 添加时间
     */
    private String addtime;

    private String fromNickName;

    private String toNickName;


    @Override
    protected Serializable pkVal() {
        return this.friendsId;
    }

}
