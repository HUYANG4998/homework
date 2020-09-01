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
 * 推广网站基本信息
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("promotebasic")
public class Promotebasic extends Model<Promotebasic> {

    private static final long serialVersionUID = 1L;

    public Promotebasic(){
        this.promotebasicid= Utils.getUUID();
    }
    /**
     * 推广网站基本信息id
     */
    @TableId("promotebasicid")
    private String promotebasicid;

    /**
     * 公司简介
     */
    private String introduction;

    /**
     * 公司电话
     */
    private String companyphone;

    /**
     * 公司地址
     */
    private String companyaddress;

    /**
     * APP地址
     */
    private String appaddress;

    /**
     * 微信二维码图片
     */
    private String weixin;

    private String introduce;

    private String title;

    private String content;


    @Override
    protected Serializable pkVal() {
        return this.promotebasicid;
    }

}
