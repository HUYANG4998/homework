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
 * 
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("car_decoration")
public class CarDecoration extends Model<CarDecoration> {


    private static final long serialVersionUID = 1L;

    public CarDecoration(){
        this.carDecorationId= Utils.getUUID();
    }
    /**
     * 婚车装饰
     */
    @TableId("car_decoration_id")
    private String carDecorationId;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片
     */
    private String image;

    private String price;


    @Override
    protected Serializable pkVal() {
        return this.carDecorationId;
    }

}
