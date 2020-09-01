package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.MoneyTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 金钱模板表 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface IMoneyTemplateService extends IService<MoneyTemplate> {

    MoneyTemplate selectMoneyTemplate();

    void updateMoneyTemplate(MoneyTemplate moneyTemplate);

    void addMoneyTemplate(MoneyTemplate moneyTemplate);
}
