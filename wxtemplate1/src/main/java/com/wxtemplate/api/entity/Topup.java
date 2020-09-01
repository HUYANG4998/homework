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
 * 手动充值记录
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("topup")
public class Topup extends Model<Topup> {

    private static final long serialVersionUID = 1L;

    public Topup(){
        this.topupid= Utils.getUUID();
    }
    /**
     * 充值记录id
     */
    @TableId("topupid")
    private String topupid;

    /**
     * 用户id
     */
    private String userid;

    /**
     * 0减1加
     */
    private String addorsub;

    /**
     * 金额
     */
    private String price;

    /**
     * 添加时间
     */
    private String createtime;


    @Override
    protected Serializable pkVal() {
        return this.topupid;
    }

}
