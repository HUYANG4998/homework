package com.wxtemplate.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Promotebasic;
import com.wxtemplate.api.mapper.PromotebasicMapper;
import com.wxtemplate.api.service.IPromotebasicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 推广网站基本信息 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-08
 */
@Service
public class PromotebasicServiceImpl extends ServiceImpl<PromotebasicMapper, Promotebasic> implements IPromotebasicService {

    @Resource
    private PromotebasicMapper promotebasicMapper;

    @Override
    public Promotebasic selectBasic() {

        return promotebasicMapper.selectBasic();
    }

    @Override
    public void updateBasic(Promotebasic promotebasic) {
        if(promotebasic!=null){
            promotebasicMapper.updateById(promotebasic);
        }
    }

    @Override
    public void deleteBasic(String promotebasicid) {
        if(!StringUtils.isEmpty(promotebasicid)){
            promotebasicMapper.deleteById(promotebasicid);
        }
    }

    @Override
    public void addBasic(Promotebasic promotebasic) {
        if(promotebasic!=null){
            promotebasicMapper.insert(promotebasic);
        }
    }
}
