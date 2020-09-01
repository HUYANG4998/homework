package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Carousel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 轮播图 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
public interface CarouselMapper extends BaseMapper<Carousel> {

    List<String> selectCarouselByUserid();

    void deleteCarouse();
}
