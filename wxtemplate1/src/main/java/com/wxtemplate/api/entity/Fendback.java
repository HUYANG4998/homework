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
 * 表
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("fendback")
public class Fendback extends Model<Fendback> {

    private static final long serialVersionUID = 1L;
    public Fendback(){
        feedbackid= Utils.getUUID();
    }
    @TableId("feedbackid")
    private String feedbackid;

    private String content;

    private String addtime;

    private String userid;

    private String name;

    private String phone;


    @Override
    protected Serializable pkVal() {
        return this.feedbackid;
    }

}
