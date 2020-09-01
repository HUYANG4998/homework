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
 * 好友邀请
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("friends_invite")
public class FriendsInvite extends Model<FriendsInvite> {

    private static final long serialVersionUID = 1L;

    public FriendsInvite() {
        this.friendsInviteId = Utils.getUUID();
    }

    /**
     * 好友邀请id
     */
    @TableId("friends_invite_id")
    private String friendsInviteId;

    /**
     * 用户id--邀请者
     */
    private String fromUserId;

    /**
     * 用户id--被邀请者
     */
    private String toUserId;

    /**
     * 群里邀请的人
     */
    private String groupChatId;

    /**
     * 审核0等待验证1通过2拒绝
     */
    private String audit;

    /**
     * 类型0群1好友
     */
    private String type;

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
        return this.friendsInviteId;
    }

}
