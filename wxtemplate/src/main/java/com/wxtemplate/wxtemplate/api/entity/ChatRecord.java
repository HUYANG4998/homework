package com.wxtemplate.wxtemplate.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.wxtemplate.wxtemplate.api.util.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("chat_record")
public class ChatRecord extends Model<ChatRecord> {

    private static final long serialVersionUID = 1L;

    public ChatRecord() {
        this.chatRecordId = Utils.getUUID();
    }

    /**
     * 聊天记录id
     */
    @TableId("chat_record_id")
    private String chatRecordId;

    /**
     * 发起聊天id
     */
    private String fromUserId;

    /**
     * 被动聊天id   群聊id/用户id
     */
    private String toId;

    /**
     * 类型 0群聊1个人
     */
    private String status;

    /**
     * 聊天方式0文本1文件2红包
     */
    private String type;

    /**
     * 红包金额
     */
    private BigDecimal redMoney;

    /**
     * 红包数量
     */
    private Integer redNumber;

    /**
     * 添加时间
     */
    private String addtime;

    /**
     * 是否已读 0未读1已读
     */
    private String isRead;

    /**
     * 逻辑删除0隐藏1显示
     */
    private String delFlag;

    /**
     * 内容
     *
     * @return
     */
    private String content;

    private String body;


    @Override
    protected Serializable pkVal() {
        return this.chatRecordId;
    }

}
