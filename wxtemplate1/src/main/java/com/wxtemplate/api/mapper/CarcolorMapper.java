package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Carcolor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车颜色库存 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-15
 */
public interface CarcolorMapper extends BaseMapper<Carcolor> {

    List<Carcolor> selectCarColorByCarid(String carid);

    void deleteCarColorByCarid(String carid);

    Carcolor selectCarColorByCaridAndColor(String carid, String color);

    List<Carcolor> selectCarcolorByCarid(String carid);

    void deleteCarcolorOther(Map<String,Object> map);
}
