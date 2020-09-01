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
 * 网站三个大图
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("webpic")
public class Webpic extends Model<Webpic> {

    private static final long serialVersionUID = 1L;

    public Webpic(){
        this.webpicId= Utils.getUUID();
    }
    /**
     * 网站图片
     */
    @TableId("webpic_id")
    private String webpicId;

    /**
     * 首页图片
     */
    private String homepage;

    /**
     * 自选婚车图片
     */
    private String choose;

    /**
     * 套餐图片
     */
    private String packagepic;


    @Override
    protected Serializable pkVal() {
        return this.webpicId;
    }

}
