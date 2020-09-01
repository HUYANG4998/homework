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
 * @since 2020-01-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("enduser")
public class Enduser extends Model<Enduser> {

    private static final long serialVersionUID = 1L;
    public Enduser(){
        this.enduserid= Utils.getUUID();
    }

    /**
     * 后台用户id
     */
    @TableId("enduserid")
    private String enduserid;

    /**
     * 后台用户名
     */
    private String endusername;

    /**
     * 后台手机号
     */
    private String endphone;

    /**
     * 后台密码
     */
    private String endpassword;

    private String endimage;

    private String permission;


    @Override
    protected Serializable pkVal() {
        return this.enduserid;
    }

}
