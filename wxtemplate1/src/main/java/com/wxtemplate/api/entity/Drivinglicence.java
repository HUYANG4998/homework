package com.wxtemplate.api.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

import com.wxtemplate.api.util.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 行驶证
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Drivinglicence extends Model<Drivinglicence> {

    private static final long serialVersionUID = 1L;

    public Drivinglicence(){
        drivingid= Utils.getUUID();
    }
    private String drivingid;

    private String idcard;

    private String addtime;
    private String updatetime;

    @Override
    protected Serializable pkVal() {
        return this.drivingid;
    }

}
