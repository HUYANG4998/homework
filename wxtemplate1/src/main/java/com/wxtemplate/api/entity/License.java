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
 * 企业认证
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("license")
public class License extends Model<License> {

    private static final long serialVersionUID = 1L;

    public License(){
        licenseid= Utils.getUUID();
    }
    @TableId("licenseid")
    private String licenseid;

    private String licenseurl;

    private String licensenumber;

    private String addtime;

    private String userid;

    private String licensestatus;

    private String cause;


    @Override
    protected Serializable pkVal() {
        return this.licenseid;
    }

}
