package com.wxtemplate.api.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

import com.wxtemplate.api.util.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 轮播图
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Carousel extends Model<Carousel> {

    private static final long serialVersionUID = 1L;
    public Carousel(){
        carouselid= Utils.getUUID();
    }
    private String carouselid;

    private String url;

    private String addtime;

    private String updatetime;

    private String status;


    @Override
    protected Serializable pkVal() {
        return this.carouselid;
    }

}
