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
 * 
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("violation")
public class Violation extends Model<Violation> {

    private static final long serialVersionUID = 1L;
    public Violation(){
        this.violationid= Utils.getUUID();
    }
    /**
     * 违规条例id
     */
    @TableId("violationid")
    private String violationid;

    /**
     * 内容
     */
    private String content;
    /**
     * 发布流程
     */
    private String releaseprocess;
    /**
     * 订车须知
     */
    private String ordering;
    /**
     * 接单流程
     */
    private String orderprocess;
    /**
     * 接单须知
     */
    private String takeorder;
    /**
     * 费用说明
     */
    private String costdescription;
    /**
     * 行程说明
     */
    private String tripdescription;

    /**
     * 协议
     * @return
     */
    private String agreement;

    private String servicephone;

    private String payway;

    private String coupleprocess;

    private String companyin;

    private String vipagree;

    private String privacyagree;

    private String copyright;

    private String useragreement;

    private String privacypolicy;

    @Override
    protected Serializable pkVal() {
        return this.violationid;
    }

}
