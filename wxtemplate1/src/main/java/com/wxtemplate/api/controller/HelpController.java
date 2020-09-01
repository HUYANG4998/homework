package com.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Help;
import com.wxtemplate.api.entity.SonHelp;
import com.wxtemplate.api.entity.VO.OwnerCertVo;
import com.wxtemplate.api.service.IHelpService;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 帮助中心 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-18
 */
@RestController
@RequestMapping("/api/help")
@CrossOrigin
public class HelpController {

    @Autowired
    private IHelpService helpService;


    /**
     * 获取全部主题
     */
    @PostMapping(value = "/selectHelpStyle")
    public Result selectHelpStyle() {

        try {

            List<Help> helpList = helpService.selectHelpStyle();
            return Result.success(helpList);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectHelpStyle fail");
        }
    }

    /**
     * 删除主题
     */
    @PostMapping(value = "/deleteHelp")
    public Result deleteHelp(String helpId) {

        try {
            if (!StringUtils.isEmpty(helpId)) {
                helpService.deletHelp(helpId);
                return Result.success("删除成功");
            } else {
                return Result.fail("删除失败");
            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("deleteHelp fail");
        }
    }

    /**
     * 获取主题
     */
    @PostMapping(value = "/selectHelpById")
    public Result selectHelpById(String helpId) {

        try {
            if (!StringUtils.isEmpty(helpId)) {
                Help help = helpService.selectHelpById(helpId);
                if (help != null) {
                    return Result.success(help);
                } else {
                    return Result.fail("主题不存在");
                }

            } else {
                return Result.fail("获取失败");
            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectHelpById fail");
        }
    }

    /**
     * 获取副主题
     */
    @PostMapping(value = "/selectViceHelp")
    public Result selectViceHelpById() {

        try {

                List<Map<String,Object>> helpList = helpService.selectViceHelp();

                return Result.success(helpList);


        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectViceHelpById fail");
        }
    }
    /**
     * 获取问题
     */
    @PostMapping(value = "/selectProHelp")
    public Result selectProHelpById() {

        try {

                List<Map<String,Object>> helpList = helpService.selectProHelp();

                return Result.success(helpList);



        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectProHelpById fail");
        }
    }


    /**
     * 添加主题
     */
    @PostMapping(value = "/addHelp")
    public Result addStyle(@RequestBody Help help) {


        try {
            if (help != null) {
                helpService.addStyle(help);
                return Result.success("添加成功");
            } else {
                return Result.fail("添加失败");
            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("addHelp fail");
        }
    }

    /**
     * 修改主题
     */
    @PostMapping(value = "/updateHelp")
    public Result updateHelp(@RequestBody Help help) {

        try {
            if (help != null) {
                helpService.updateHelp(help);
                return Result.success("修改成功");
            } else {
                return Result.fail("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateHelp fail");
        }
    }
    /**
     * 获取全部主题和副主题和第一个副主题的所有问题
     */
    @PostMapping(value = "/selectStyleAllAndOne")
    public Result selectStyleAllAndOne() {

        try {

            List<Map<String,Object>> list=helpService.selectStyleAllAndOne();
            return Result.success(list);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectStyleAllAndOne fail");
        }
    }
    /**
     * 获取副主题旗下所有问题
     */
    @PostMapping(value = "/selectViceProblems")
    public Result selectViceProblems(String firstId) {

        try {

            List<Help> list=helpService.selectViceProblems(firstId);
            return Result.success(list);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectViceProblems fail");
        }
    }

}
