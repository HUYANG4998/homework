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
 * 实名
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("realname")
public class Realname extends Model<Realname> {

    private static final long serialVersionUID = 1L;

    public Realname(){
        super();
        realnameid= Utils.getUUID();
    }
    @TableId("realnameid")
    private String realnameid;

    private String idcard;

    private String realname;

    private String userid;

    private String addtime;

    /**
     * 个人认证状状态
     */
    private String realstatus;

    private String updatetime;

    private String cause;


    @Override
    protected Serializable pkVal() {
        return this.realnameid;
    }

}
