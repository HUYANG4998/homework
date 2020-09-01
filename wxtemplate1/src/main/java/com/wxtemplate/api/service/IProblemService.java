package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Problem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 常见问题 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-08
 */
public interface IProblemService extends IService<Problem> {

    List<Problem> selectProductDes();

    void deleteProblem(String problemid);

    void addProblem(Problem problem);

    Problem selectProblemByProblemId(String problemid);

    void updateProblem(Problem problem);
}
