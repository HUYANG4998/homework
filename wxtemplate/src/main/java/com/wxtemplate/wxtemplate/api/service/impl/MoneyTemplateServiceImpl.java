package com.wxtemplate.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.wxtemplate.wxtemplate.api.entity.MoneyTemplate;
import com.wxtemplate.wxtemplate.api.mapper.MoneyTemplateMapper;
import com.wxtemplate.wxtemplate.api.service.IMoneyTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 金钱模板表 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Service
public class MoneyTemplateServiceImpl extends ServiceImpl<MoneyTemplateMapper, MoneyTemplate> implements IMoneyTemplateService {

    @Resource
    private MoneyTemplateMapper moneyTemplateMapper;

    @Override
    public MoneyTemplate selectMoneyTemplate() {
        return moneyTemplateMapper.selectMoneyTemplate();
    }

    @Override
    public void updateMoneyTemplate(MoneyTemplate moneyTemplate) {
        if (moneyTemplate != null) {
            moneyTemplate.setUpdatetime(DateUtil.now());
            moneyTemplateMapper.updateById(moneyTemplate);
        }
    }

    @Override
    public void addMoneyTemplate(MoneyTemplate moneyTemplate) {
        if (moneyTemplate != null) {
            moneyTemplate.setAddtime(DateUtil.now());
            moneyTemplateMapper.insert(moneyTemplate);
        }
    }
}
