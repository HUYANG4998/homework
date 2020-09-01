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
 *
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("friend_circle")
public class FriendCircle extends Model<FriendCircle> {

    private static final long serialVersionUID = 1L;

    public FriendCircle() {
        this.friendCircleId = Utils.getUUID();
    }

    /**
     * 朋友圈id
     */
    @TableId("friend_circle_id")
    private String friendCircleId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 内容
     */
    private String content;

    /**
     * 图片（分号分割）
     */
    private String images;

    /**
     * 添加时间
     */
    private String addtime;


    @Override
    protected Serializable pkVal() {
        return this.friendCircleId;
    }

}
