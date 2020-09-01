package com.wxtemplate.wxtemplate.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.wxtemplate.wxtemplate.api.util.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 广告表
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("advertising")
public class Advertising extends Model<Advertising> {

    private static final long serialVersionUID = 1L;

    public Advertising() {
        this.advertisingId = Utils.getUUID();
    }

    /**
     * 广告id
     */
    @TableId("advertising_id")
    private String advertisingId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 轮播位置1主页2红包3朋友圈
     */
    private String type;

    /**
     * 广告所属0系统1私人
     */
    private String status;

    /**
     * 广告图
     */
    private String image;

    /**
     * 网站地址
     */
    private String location;

    /**
     * 发布时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 添加时间
     */
    private String addtime;

    /**
     * 是否有效0无效1有效
     *
     * @return
     */
    private String isValidity;

    /**
     * 天数
     *
     * @return
     */
    private Integer numberDays;


    @Override
    protected Serializable pkVal() {
        return this.advertisingId;
    }

}
