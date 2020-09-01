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
 * 个人车
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("car")
public class Car extends Model<Car> {

    private static final long serialVersionUID = 1L;

    public Car(){
        carid= Utils.getUUID();
    }
    @TableId("carid")
    private String carid;

    private String cardesc;

    private String carcolor;

    private String carmodel;

    private String addtime;

    private String updatetime;

    private String userid;

    private String carnumber;

    /**
     * 行驶证
     */
    private String drivinglicense;

    /**
     * 类型
     */
    private String type;

    /**
     * 审核状态
     */
    private String status;
    /**
     * 驳回原因
     * @return
     */
    private String cause;

    /**
     * 开始时间
     * @return
     */
    private String starttime;

    /**
     * 结束时间
     * @return
     */
    private String endtime;
    @Override
    protected Serializable pkVal() {
        return this.carid;
    }

}
