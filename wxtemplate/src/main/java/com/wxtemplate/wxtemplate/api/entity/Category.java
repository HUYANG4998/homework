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
 * 动态分类表
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("category")
public class Category extends Model<Category> {

    private static final long serialVersionUID = 1L;

    public Category() {
        this.categoryId = Utils.getUUID();
    }

    /**
     * 分类id
     */
    @TableId("category_id")
    private String categoryId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 添加时间
     */
    private String addtime;

    /**
     * 修改时间
     */
    private String updatetime;


    @Override
    protected Serializable pkVal() {
        return this.categoryId;
    }

}
