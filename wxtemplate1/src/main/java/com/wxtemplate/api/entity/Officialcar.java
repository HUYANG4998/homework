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
 * 平台车
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("officialcar")
public class Officialcar extends Model<Officialcar> {

    private static final long serialVersionUID = 1L;

    public Officialcar(){
        carid= Utils.getUUID();
    }
    @TableId("carid")
    private String carid;

    /**
     * 车描述
     */
    private String cardesc;

    /**
     * 车颜色
     */
    private String carcolor;

    /**
     * 车型号
     */
    private String carmodel;

    private String addtime;

    private String updatetime;

    private String userid;

    /**
     * 车牌号
     */
    private String carnumber;

    /**
     * 品牌id
     */
    private String brandid;

    /**
     * 车类型
     */
    private String cartype;

    private String sumnum;

    private Double carprice;

    private Integer sales;

    private String seat;

    private String type;

    private String status;

    private Double overtimeprice;


    @Override
    protected Serializable pkVal() {
        return this.carid;
    }

}
