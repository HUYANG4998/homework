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
 * 金钱模板表
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("money_template")
public class MoneyTemplate extends Model<MoneyTemplate> {

    private static final long serialVersionUID = 1L;

    public MoneyTemplate() {
        this.moneyTemplateId = Utils.getUUID();
    }

    /**
     * 金钱模板id
     */
    @TableId("money_template_id")
    private String moneyTemplateId;

    /**
     * 会员价钱
     */
    private BigDecimal vip;

    /**
     * 热门每天价格
     */
    private BigDecimal hot;

    /**
     * 置顶每天价格
     */
    private BigDecimal stick;

    /**
     * 主页轮播价钱/月
     */
    private BigDecimal homePage;

    /**
     * 红包轮播价格/月
     */
    private BigDecimal redPackage;

    /**
     * 朋友圈广告价钱/张
     */
    private BigDecimal friendsCircle;

    /**
     * 添加时间
     */
    private String addtime;

    /**
     * 修改时间
     */
    private String updatetime;

    private String images;


    @Override
    protected Serializable pkVal() {
        return this.moneyTemplateId;
    }


}
