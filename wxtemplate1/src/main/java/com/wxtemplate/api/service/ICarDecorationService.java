package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.CarDecoration;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-24
 */
public interface ICarDecorationService extends IService<CarDecoration> {

    List<CarDecoration> findDecoration();

    void addDecoration(CarDecoration carDecoration);

    void updateDecoration(CarDecoration carDecoration);

    CarDecoration selectDecorationOne(String carDecorationId);

    void deleteDecoration(String carDecorationId);
}
