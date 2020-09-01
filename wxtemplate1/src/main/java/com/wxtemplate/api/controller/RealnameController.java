package com.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.VO.CarVo;
import com.wxtemplate.api.service.IRealnameService;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 实名 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@RestController
@RequestMapping("/api/realname")
@CrossOrigin
public class RealnameController {

    @Autowired
    private IRealnameService realnameService;

    /**后台 查询全部个人认证*/
    @PostMapping(value = "/selectRealname")
    public Result selectRealname(String realname){
        try {
                List<Map<String,Object>> realNameMap= realnameService.selectRealname(realname);
                return Result.success(realNameMap);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectRealname fail");
        }
    }
    /**
     * 后台 查询单个个人认证  通过realnameid
     */
    @PostMapping(value = "/selectRealnameByRealnameId")
    public Result selectRealnameByRealnameId(String realnameid){
        try {
            if(!StringUtils.isEmpty(realnameid)){
                Map<String,Object> realnameMap= realnameService.selectRealnameByRealnameId(realnameid);

                if(realnameMap!=null&&!realnameMap.isEmpty()){
                    return Result.success(realnameMap);
                }else{
                    return Result.fail("检索失败!");
                }
            }else{
                return Result.fail("条件不足!");
            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectRealname fail");
        }
    }
    /**
     * 后台 身份证审核
     */
    @PostMapping(value = "/idcardAudit")
    public Result idcardAudit(@RequestBody Map<String,Object> result){
        try {
            if(!result.isEmpty()){
                realnameService.idcardAudit(result);
                return Result.success("审核成功");
            }else{
                return Result.fail("审核失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("idcardAudit fail");
        }
    }
}
