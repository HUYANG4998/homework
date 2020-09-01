package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Imgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 图集 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
public interface ImgsMapper extends BaseMapper<Imgs> {

    public void deleteByObjId(String carId);

    void updateByCarid(Imgs img);

    Imgs selectByObjId(String objid);
}
