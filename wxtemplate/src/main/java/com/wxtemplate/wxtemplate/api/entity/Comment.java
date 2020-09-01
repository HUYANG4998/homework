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
 * 评论表
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("comment")
public class Comment extends Model<Comment> {

    private static final long serialVersionUID = 1L;

    public Comment() {
        this.commentId = Utils.getUUID();
    }

    /**
     * 评论id
     */
    @TableId("comment_id")
    private String commentId;

    /**
     * 上一级用户id
     */
    private String lastCommentId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 动态id
     */
    private String dynamicId;

    /**
     * 内容
     */
    private String content;

    /**
     * 添加时间
     */
    private String addtime;


    @Override
    protected Serializable pkVal() {
        return this.commentId;
    }

}
