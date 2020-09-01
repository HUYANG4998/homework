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
 * 群成员
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("group_member")
public class GroupMember extends Model<GroupMember> {

    private static final long serialVersionUID = 1L;

    public GroupMember() {
        this.groupMemberId = Utils.getUUID();
    }

    /**
     * 群成员id
     */
    @TableId("group_member_id")
    private String groupMemberId;

    /**
     * 群聊id
     */
    private String groupChatId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 权限0群主1管理员2成员
     */
    private String permission;

    /**
     * 禁言 0关闭1开启
     */
    private String banned;

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
        return this.groupMemberId;
    }

}
