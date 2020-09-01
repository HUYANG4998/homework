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
 * 资产表
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("asset")
public class Asset extends Model<Asset> {

    private static final long serialVersionUID = 1L;
    public Asset(){
        assetid= Utils.getUUID();
    }
    /**
     * 资产id
     */
    @TableId("assetid")
    private String assetid;

    /**
     * 用户id
     */
    private String userid;
    /**
     * 渠道
     */
    private String ditch;

    private String accountid;
    /**
     * 金额
     */
    private Double price;

    /**
     * 实际金额
     */
    private Double realPrice;



    /**
     * 添加时间
     */
    private String addtime;

    /**
     * 0待审核1通过2不通过
     */
    private String status;

    /**
     * 驳回原因
     */
    private String cause;

    /**
     * 修改时间
     */
    private String updatetime;

    /**
     * 0充值1提现
     */
    private String cate;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
