package com.wxtemplate.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.wxtemplate.api.util.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 常见问题
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("problem")
public class Problem extends Model<Problem> {

    private static final long serialVersionUID = 1L;
    public Problem(){
        super();
        problemid= Utils.getUUID();
    }
    /**
     * 问题id
     */
    @TableId("problemid")
    private String problemid;

    /**
     * 问题描述
     */
    private String problemdes;

    /**
     * 问题内容
     */
    private String problemcontent;

    /**
     * 添加时间
     */
    private String addtime;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
