package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Carousel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 轮播图 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
public interface ICarouselService extends IService<Carousel> {

    List<String> findCarousel();

    void addCarousel(List<String> url);
}
