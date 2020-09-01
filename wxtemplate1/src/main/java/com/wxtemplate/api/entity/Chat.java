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
 * 
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("chat")
public class Chat extends Model<Chat> {

    private static final long serialVersionUID = 1L;
    public Chat(){
        chatid= Utils.getUUID();
    }
    /**
     * 聊天id
     */
    @TableId("chatid")
    private String chatid;

    /**
     * 客服id
     */
    private String serviceid;

    /**
     * 用户id
     */
    private String userid;
    /**
     * 客服数据
     */
    private String servicecontent;
    /**
     * 用户数据
     */
    private String usercontent;

    /**
     * 添加时间
     */
    private String addtime;

    /**
     * 修改时间
     */
    private String updatetime;


    @Override
    protected Serializable pkVal() {
        return this.chatid;
    }

}
