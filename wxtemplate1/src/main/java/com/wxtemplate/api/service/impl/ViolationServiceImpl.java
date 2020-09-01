package com.wxtemplate.api.service.impl;

import com.wxtemplate.api.entity.Violation;
import com.wxtemplate.api.mapper.ViolationMapper;
import com.wxtemplate.api.service.IViolationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-21
 */
@Service
public class ViolationServiceImpl extends ServiceImpl<ViolationMapper, Violation> implements IViolationService {

    @Resource
    private ViolationMapper violationMapper;

    @Override
    public Violation selectViolation() {
        return violationMapper.selectViolation();
    }

    @Override
    public void addViolation(Violation violation) {
        if(violation!=null){
            violationMapper.insert(violation);
        }
    }

    @Override
    public void updateViolation(Violation violation) {
        if(violation!=null){
            violationMapper.updateById(violation);
        }
    }
}
