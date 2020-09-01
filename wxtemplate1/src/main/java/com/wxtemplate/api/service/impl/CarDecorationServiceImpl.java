package com.wxtemplate.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.CarDecoration;
import com.wxtemplate.api.mapper.CarDecorationMapper;
import com.wxtemplate.api.service.ICarDecorationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-24
 */
@Service
public class CarDecorationServiceImpl extends ServiceImpl<CarDecorationMapper, CarDecoration> implements ICarDecorationService {

    @Resource
    private CarDecorationMapper carDecorationMapper;

    @Override
    public List<CarDecoration> findDecoration() {

        return carDecorationMapper.findDecoration();
    }

    @Override
    public void addDecoration(CarDecoration carDecoration) {

        if(carDecoration!=null){
            carDecorationMapper.insert(carDecoration);
        }

    }

    @Override
    public void updateDecoration(CarDecoration carDecoration) {
        if(carDecoration!=null){

            carDecorationMapper.updateById(carDecoration);

        }
    }

    @Override
    public CarDecoration selectDecorationOne(String carDecorationId) {
        CarDecoration carDecoration=new CarDecoration();
        if(!StringUtils.isEmpty(carDecorationId)){
            carDecoration= carDecorationMapper.selectById(carDecorationId);
        }
        return carDecoration;
    }

    @Override
    public void deleteDecoration(String carDecorationId) {
        if(!StringUtils.isEmpty(carDecorationId)){
            carDecorationMapper.deleteById(carDecorationId);
        }
    }
}
