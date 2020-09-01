package com.wxtemplate.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Fendback;
import com.wxtemplate.api.mapper.FendbackMapper;
import com.wxtemplate.api.service.IFendbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 反馈表 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Service
public class FendbackServiceImpl extends ServiceImpl<FendbackMapper, Fendback> implements IFendbackService {

    @Resource
    private FendbackMapper fendbackMapper;
    @Override
    public List<Fendback> selectFeedback() {
        return fendbackMapper.selectFeedback();
    }

    @Override
    public Fendback selectFeedbackByFeedbackId(String feedbackid) {
        Fendback fendback=null;
        if(!StringUtils.isEmpty(feedbackid)){
            fendback=fendbackMapper.selectById(feedbackid);
        }
        return fendback;
    }

    @Override
    public void deleteFeedback(String feedbackid) {
        if(!StringUtils.isEmpty(feedbackid)){
            fendbackMapper.deleteById(feedbackid);
        }
    }
}
