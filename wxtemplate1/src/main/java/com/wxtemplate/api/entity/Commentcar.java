package com.wxtemplate.api.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

import com.wxtemplate.api.util.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 评论车
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Commentcar extends Model<Commentcar> {

    private static final long serialVersionUID = 1L;
    public Commentcar(){
        commentid= Utils.getUUID();
    }
    private String commentid;

    private String content;

    private String userid;

    private String addtime;

    private String carid;


    @Override
    protected Serializable pkVal() {
        return this.commentid;
    }

}
