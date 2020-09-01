package com.wxtemplate.api.entity;

import java.math.BigDecimal;

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
 * 套餐
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("package")
public class Package extends Model<Package> {

    private static final long serialVersionUID = 1L;

    public Package(){
        this.packageid= Utils.getUUID();
    }
    /**
     * 套餐id
     */
    @TableId("packageid")
    private String packageid;

    /**
     * 主车id
     */
    private String mainId;

    /**
     * 主车颜色
     */
    private String mainColor;

    /**
     * 副车id
     */
    private String deputyId;

    /**
     * 副车颜色
     */
    private String deputyColor;

    /**
     * 副车数量
     */
    private String deputyNumber;

    /**
     * 价格
     */
    private Double price;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;


    @Override
    protected Serializable pkVal() {
        return this.packageid;
    }

}
