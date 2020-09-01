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
 * 动态表
 *
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dynamic")
public class Dynamic extends Model<Dynamic> {

    private static final long serialVersionUID = 1L;

    public Dynamic() {
        this.dynamicId = Utils.getUUID();
    }

    /**
     * 动态id
     */
    @TableId("dynamic_id")
    private String dynamicId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 类别id
     */
    private String categoryId;

    /**
     * 内容描述
     */
    private String content;

    /**
     * 图片集合（分号隔开）
     */
    private String images;

    /**
     * 视频集合（分号隔开）
     */
    private String video;

    /**
     * 红包金额
     */
    private BigDecimal redPackageMoney;

    /**
     * 红包数量
     */
    private Integer redPackageNumber;

    /**
     * 热门时间
     */
    private String hotTime;

    /**
     * 置顶时间
     */
    private String stickTime;

    /**
     * 访问人数
     */
    private Integer visitNumber;

    /**
     * 点赞人数
     */
    private Integer giveLikeNumber;

    /**
     * 收藏人数
     */
    private Integer collectNumber;

    /**
     * 添加时间
     */
    private String addtime;

    /**
     * 修改时间
     */
    private String updatetime;

    /**
     * 0热门和置顶1热门2置顶3什么没有
     */
    private String stick;

    /**
     * 刷新时间
     *
     * @return
     */
    private String refreshTime;

    /**
     * 热门天数
     *
     * @return
     */
    private Integer hotDay;

    /**
     * 置顶天数
     *
     * @return
     */
    private Integer stickDay;

    /**
     * 评论人数
     *
     * @return
     */
    private Integer commentPeople;

    /**
     * 视频第一帧
     */
    private String headVideo;


    @Override
    protected Serializable pkVal() {
        return this.dynamicId;
    }

}
