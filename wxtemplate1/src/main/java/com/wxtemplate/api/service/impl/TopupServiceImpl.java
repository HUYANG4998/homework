package com.wxtemplate.api.service.impl;

import com.wxtemplate.api.entity.Topup;
import com.wxtemplate.api.mapper.TopupMapper;
import com.wxtemplate.api.service.ITopupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 手动充值记录 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-30
 */
@Service
public class TopupServiceImpl extends ServiceImpl<TopupMapper, Topup> implements ITopupService {

    @Resource
    private TopupMapper topupMapper;

    @Override
    public List<Map<String, Object>> selectTopup(String phone) {

        return topupMapper.selectTopUp(phone);
    }
}
