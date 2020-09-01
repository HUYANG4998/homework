package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Promotebasic;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 推广网站基本信息 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-08
 */
public interface IPromotebasicService extends IService<Promotebasic> {

    Promotebasic selectBasic();

    void updateBasic(Promotebasic promotebasic);

    void deleteBasic(String promotebasicid);

    void addBasic(Promotebasic promotebasic);
}
