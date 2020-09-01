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
 * 群聊消息是否未读
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("group_chat_read")
public class GroupChatRead extends Model<GroupChatRead> {

    private static final long serialVersionUID = 1L;

    public GroupChatRead() {
        this.groupChatReadId = Utils.getUUID();
    }

    /**
     * 群聊未读消息集
     */
    @TableId("group_chat_read_id")
    private String groupChatReadId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 群聊id
     */
    private String groupChatId;

    /**
     * 是否已读0未读1已读
     */
    private Integer isRead;

    /**
     * 添加时间
     *
     * @return
     */
    private String addtime;

    /**
     * 修改时间
     *
     * @return
     */
    private String updatetime;


    @Override
    protected Serializable pkVal() {
        return this.groupChatReadId;
    }

}
