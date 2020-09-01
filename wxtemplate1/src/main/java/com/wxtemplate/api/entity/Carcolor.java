package com.wxtemplate.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 车颜色库存
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("carcolor")
public class Carcolor extends Model<Carcolor> {

    private static final long serialVersionUID = 1L;
    public Carcolor(){
        this.carcolorid= Utils.getUUID();
    }
    /**
     * 颜色对应库存id
     */
    @TableId("carcolorid")
    private String carcolorid;

    /**
     * 车辆id
     */
    private String carid;

    /**
     * 颜色
     */
    @TableField("color")
    private String color;

    /**
     * 库存
     */
    private Integer inventory;


    @Override
    protected Serializable pkVal() {
        return this.carcolorid;
    }

}
