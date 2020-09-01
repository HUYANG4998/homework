package com.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Problem;
import com.wxtemplate.api.entity.Promotebasic;
import com.wxtemplate.api.service.IProblemService;
import com.wxtemplate.api.service.IPromotebasicService;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 推广网站基本信息 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-08
 */
@RestController
@RequestMapping("/api/promotebasic")
@CrossOrigin
public class PromotebasicController {

    @Autowired
    private IPromotebasicService promotebasicService;

    /**查询网站基本信息*/
    @PostMapping(value = "/selectBasic")
    public Result selectBasic(){
        try {
            Promotebasic promotebasic=promotebasicService.selectBasic();
            return Result.success(promotebasic);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectBasic fail");
        }
    }
    /**修改网站基本信息*/
    @PostMapping(value = "/updateBasic")
    public Result updateBasic(Promotebasic promotebasic){
        try {
            if(promotebasic!=null){
                promotebasicService.updateBasic(promotebasic);
                return Result.success("修改成功");
            }else{
                return Result.fail("参数错误");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateBasic fail");
        }
    }
    /**添加网站基本信息*/
    @PostMapping(value = "/addBasic")
    public Result addBasic(Promotebasic promotebasic){
        try {
            if(promotebasic!=null){
                promotebasicService.addBasic(promotebasic);
                return Result.success("添加成功");
            }else{
                return Result.fail("参数错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("addBasic fail");
        }
    }
    /**删除网站基本信息*/
    @PostMapping(value = "/deleteBasic")
    public Result deleteBasic(String promotebasicid){
        try {
            if(!StringUtils.isEmpty(promotebasicid)){
                promotebasicService.deleteBasic(promotebasicid);
                return Result.success("删除成功");
            }else{
                return Result.fail("参数错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("deleteBasic fail");
        }
    }
}
