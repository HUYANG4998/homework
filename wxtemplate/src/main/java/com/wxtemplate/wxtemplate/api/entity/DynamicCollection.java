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
 * 动态点赞/收藏
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dynamic_collection")
public class DynamicCollection extends Model<DynamicCollection> {

    private static final long serialVersionUID = 1L;

    public DynamicCollection() {
        this.dynamicCollectionId = Utils.getUUID();
    }

    /**
     * 动态点赞/收藏id
     */
    @TableId("dynamic_collection_id")
    private String dynamicCollectionId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 动态id
     */
    private String dynamicId;

    /**
     * 0点赞1收藏
     */
    private String type;

    /**
     * 添加时间
     */
    private String addtime;


    @Override
    protected Serializable pkVal() {
        return this.dynamicCollectionId;
    }

}
