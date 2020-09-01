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
 * @since 2020-04-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("version")
public class Version extends Model<Version> {

    private static final long serialVersionUID = 1L;

    public Version(){
        this.versionId= Utils.getUUID();
    }
    /**
     * 版本id
     */
    @TableId("version_id")
    private String versionId;

    /**
     * 版本号
     */
    private String versionNumber;

    /**
     * 版本文件
     */
    private String versionFile;


    @Override
    protected Serializable pkVal() {
        return this.versionId;
    }

}
