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
 * 
 * </p>
 *
 * @author LIUWEI
 * @since 2020-08-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("complaint")
public class Complaint extends Model<Complaint> {

    private static final long serialVersionUID = 1L;

    public Complaint(){
        this.complaintId= Utils.getUUID();
    }
    /**
     * 投诉id
     */
    @TableId("complaint_id")
    private String complaintId;

    private String userId;

    /**
     * 动态id
     */
    private String dynamicId;

    /**
     * 投诉内容
     */
    private String content;

    /**
     * 添加时间
     */
    private String addtime;


    @Override
    protected Serializable pkVal() {
        return this.complaintId;
    }

}
