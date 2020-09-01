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
 * 图集
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("imgs")
public class Imgs extends Model<Imgs> {

    private static final long serialVersionUID = 1L;

    public Imgs(){
        imgsid= Utils.getUUID();
    }
    @TableId("imgsid")
    private String imgsid;

    private String imgurl;

    private String addtime;

    private String objid;


    @Override
    protected Serializable pkVal() {
        return this.imgsid;
    }

}
