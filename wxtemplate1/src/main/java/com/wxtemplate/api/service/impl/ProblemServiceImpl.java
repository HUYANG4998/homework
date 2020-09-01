package com.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Problem;
import com.wxtemplate.api.mapper.ProblemMapper;
import com.wxtemplate.api.service.IProblemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxtemplate.api.util.Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 常见问题 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-08
 */
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements IProblemService {

    @Resource
    private ProblemMapper problemMapper;

    @Override
    public List<Problem> selectProductDes() {
        return problemMapper.selectProductDes();
    }

    @Override
    public void deleteProblem(String problemid) {
        if(!StringUtils.isEmpty(problemid)){
            problemMapper.deleteById(problemid);
        }
    }

    @Override
    public void addProblem(Problem problem) {
        if(problem!=null){
            problem.setProblemid(Utils.getUUID());
            problem.setAddtime(DateUtil.now());
            problemMapper.insert(problem);
        }
    }

    @Override
    public Problem selectProblemByProblemId(String problemid) {
        Problem problem=null;
        if(!StringUtils.isEmpty(problemid)){
            problem=problemMapper.selectById(problemid);
        }
        return problem;
    }

    @Override
    public void updateProblem(Problem problem) {
        if(problem!=null){
            problemMapper.updateById(problem);
        }
    }
}
