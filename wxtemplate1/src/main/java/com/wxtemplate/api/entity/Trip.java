package com.wxtemplate.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 行程
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("trip")
public class Trip extends Model<Trip> {

    private static final long serialVersionUID = 1L;

    /**
     * 行程id
     */
    @TableId("tripid")
    private String tripid;

    /**
     * 用户id
     */
    private String userid;

    /**
     * 新娘地址
     */
    private String brideadress;

    /**
     * 新郎地址
     */
    private String groomadress;

    /**
     * 酒店地址
     */
    private String hoteladress;

    /**
     * 接亲日期
     */
    private String marrytime;

    /**
     * 小时
     */
    private String hour;

    /**
     * 公里
     */
    private String kilometre;

    /**
     * 结婚阶段/早宴
     */
    private String marrystage;

    private String bridejwd;

    private String groomjwd;

    private String hoteljwd;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
