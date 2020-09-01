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
 * 帮助中心
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("helper")
public class Help extends Model<Help> {

    private static final long serialVersionUID = 1L;

    public Help(){
        this.helpId= Utils.getUUID();
    }
    /**
     * 帮助中心id
     */
    @TableId("help_id")
    private String helpId;

    /**
     * 父id
     */
    private String firstId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    private String addtime;


    @Override
    protected Serializable pkVal() {
        return this.helpId;
    }

}
