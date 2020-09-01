package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Violation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-21
 */
public interface IViolationService extends IService<Violation> {

    Violation selectViolation();

    void addViolation(Violation violation);

    void updateViolation(Violation violation);
}
