package com.wxtemplate.wxtemplate.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 群公告
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("group_notice")
public class GroupNotice extends Model<GroupNotice> {

    private static final long serialVersionUID = 1L;

    /**
     * 群公告
     */
    private String groupNoticeId;

    /**
     * 群聊id
     */
    private String groupChatId;

    /**
     * 文本内容
     */
    private String content;

    /**
     * 图片集合（分号分割）
     */
    private String images;

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
        return this.groupNoticeId;
    }

}
