package com.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Problem;
import com.wxtemplate.api.service.IProblemService;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 常见问题 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-08
 */
@RestController
@RequestMapping("/api/problem")
@CrossOrigin
public class ProblemController {

    @Autowired
    private IProblemService problemService;

    /**
     * 查询常见问题描述列表
     * @return
     */
    @PostMapping(value = "/selectProductDes")
    public Result selectProductDes(){
        try {
                List<Problem> problemDesList=problemService.selectProductDes();
                return Result.success(problemDesList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectProductDes fail");
        }
    }
    /**
     * 删除一个常见问题
     */
    @PostMapping(value = "/deleteProblem")
    public Result deleteProblem(String problemid){
        try {
            if(!StringUtils.isEmpty(problemid)){
                problemService.deleteProblem(problemid);
                return Result.success("删除成功");
            }else{
                return Result.fail("删除失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("deleteProblem fail");
        }
    }
    /**
     * 新增一个常见问题
     */
    @PostMapping(value = "/addProblem")
    public Result addProblem(Problem problem){
        try {
            if(problem!=null){
                problemService.addProblem(problem);
                return Result.success("添加成功");
            }else{
                return Result.fail("添加失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("addProblem fail");
        }
    }
    /**
     * 查询单个常见问题
     */
    @PostMapping(value = "/selectProblemByProblemId")
    public Result selectProblemByProblemId(String problemid){
        try {
            if(!StringUtils.isEmpty(problemid)){
                Problem problem=problemService.selectProblemByProblemId(problemid);
                if(problem!=null){
                    return Result.success(problem);
                }else{
                    return Result.fail("检索失败");
                }
            }else{
                return Result.fail("条件不足");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectProblemByProblemId fail");
        }
    }
    /**
     * 修改单个常见问题
     */
    @PostMapping(value = "/updateProblem")
    public Result updateProblem(Problem problem){
        try {
            if(problem!=null){
                problemService.updateProblem(problem);
                return Result.success("修改成功");
            }else{
                return Result.fail("修改失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateProblem fail");
        }
    }
}
