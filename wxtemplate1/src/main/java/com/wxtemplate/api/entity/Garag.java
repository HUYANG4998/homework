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
 * 车库
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("garag")
public class Garag extends Model<Garag> {

    private static final long serialVersionUID = 1L;
    public Garag(){
        garageid= Utils.getUUID();
    }
    @TableId("garageid")
    private String garageid;

    private String userid;

    private String carid;

    private String carcolor;

    private String addtime;

    /**
     * 用途/main
     */
    private String purpose;

    /**
     * l联系人
     */
    private String touchname;

    /**
     * 联系人电话
     */
    private String touchephone;

    private String carnum;

    private String packageid;





    @Override
    protected Serializable pkVal() {
        return this.garageid;
    }

}
