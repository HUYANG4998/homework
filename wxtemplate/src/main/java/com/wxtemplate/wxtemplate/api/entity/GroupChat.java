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
 * 群聊
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("group_chat")
public class GroupChat extends Model<GroupChat> {

    private static final long serialVersionUID = 1L;

    public GroupChat() {
        this.groupChatId = Utils.getUUID();
    }

    /**
     * 群聊id
     */
    @TableId("group_chat_id")
    private String groupChatId;

    /**
     * 群ID
     */
    private String groupId;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 群名称
     */
    private String groupName;

    /**
     * 群头像
     */
    private String groupHeadImage;

    /**
     * 群二维码
     */
    private String groupQrCode;

    /**
     * 群公告
     */
    private String groupNotice;

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
        return this.groupChatId;
    }

}
