package com.wxtemplate.wxtemplate.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.wxtemplate.wxtemplate.api.util.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 收益记录
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("earn")
public class Earn extends Model<Earn> {

    private static final long serialVersionUID = 1L;

    public Earn() {
        this.earnId = Utils.getUUID();
    }

    public Earn(String userId, String title, BigDecimal price, String earn, String addtime) {
        this.earnId = Utils.getUUID();
        this.userId = userId;
        this.title = title;
        this.price = price;
        this.earn = earn;
        this.addtime = addtime;
    }

    public Earn(String userId, String title, BigDecimal price, String earn, String addtime, String dynamicId) {
        this.earnId = Utils.getUUID();
        this.userId = userId;
        this.title = title;
        this.price = price;
        this.earn = earn;
        this.addtime = addtime;
        this.dynamicId = dynamicId;
    }

    /**
     * 收益记录id
     */
    @TableId("earn_id")
    private String earnId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 金额
     */
    private BigDecimal price;

    /**
     * 加减0减1加
     */
    private String earn;

    /**
     * 动态id
     */
    private String dynamicId;

    /**
     * 添加时间
     */
    private String addtime;


    @Override
    protected Serializable pkVal() {
        return this.earnId;
    }

}
