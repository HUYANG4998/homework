package com.wxtemplate.api.entity;

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
 * 评论用户
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("commentuser")
public class Commentuser extends Model<Commentuser> {

    private static final long serialVersionUID = 1L;
    public Commentuser(){
        commentid= Utils.getUUID();
    }
    @TableId("commentid")
    private String commentid;

    private String content;

    private String userid;

    /**
     * 评论者id
     */
    private String commuserid;

    private String xing;

    private String addtime;

    private String orderid;

    private String updatetime;

    @Override
    protected Serializable pkVal() {
        return this.commentid;
    }

}
