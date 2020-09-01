package com.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.wxtemplate.api.entity.Carousel;
import com.wxtemplate.api.mapper.CarouselMapper;
import com.wxtemplate.api.service.ICarouselService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 轮播图 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Service
public class CarouselServiceImpl extends ServiceImpl<CarouselMapper, Carousel> implements ICarouselService {

    @Resource
    private CarouselMapper carouselMapper;
    @Override
    public List<String> findCarousel() {
        List<String> carouselList=new ArrayList<>();
            carouselList=carouselMapper.selectCarouselByUserid();
        return carouselList;
    }

    @Override
    public void addCarousel(List<String> url) {
        if(url.size()>0){
            /**删除轮播图*/
            carouselMapper.deleteCarouse();
            Integer i=0;
            for (String file:url ) {
                Carousel carousel=new Carousel();
                carousel.setAddtime(DateUtil.format(DateUtil.offsetSecond(DateUtil.date(),i++),"yyyy-MM-dd HH:mm:ss"));
                carousel.setUpdatetime(DateUtil.now());
                carousel.setUrl(file);
                carousel.setStatus("1");
                carouselMapper.insert(carousel);
            }


        }
    }
}
