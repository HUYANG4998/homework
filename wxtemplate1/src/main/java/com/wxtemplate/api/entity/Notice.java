package com.wxtemplate.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.wxtemplate.api.util.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统通知
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("notice")
public class Notice extends Model<Notice> {

    private static final long serialVersionUID = 1L;
    public Notice(){
        super();
        noticeid= Utils.getUUID();
    }
    /**
     * 通知id
     */
    @TableId("noticeid")
    private String noticeid;

    /**
     * 内容
     */
    private String content;

    /**
     * 附加
     */
    private String addition;

    /**
     * 添加时间
     */
    private String addtime;

    private String userid;

    private String isread;



    @Override
    protected Serializable pkVal() {
        return this.noticeid;
    }

}
