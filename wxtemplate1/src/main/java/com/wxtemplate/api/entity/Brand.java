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
 * 品牌
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("brand")
public class Brand extends Model<Brand> {

    private static final long serialVersionUID = 1L;
    public Brand(){
        brandid= Utils.getUUID();
    }
    @TableId("brandid")
    private String brandid;

    private String addtime;

    /**
     * 第一个字母
     */
    private String firstletter;

    /**
     * 品牌名
     */
    private String brandname;

    private String updatetime;


    @Override
    protected Serializable pkVal() {
        return this.brandid;
    }

}
