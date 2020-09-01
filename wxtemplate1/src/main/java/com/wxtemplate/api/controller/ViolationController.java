package com.wxtemplate.api.controller;


import com.wxtemplate.api.entity.Violation;
import com.wxtemplate.api.service.IViolationService;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-21
 */
@RestController
@RequestMapping("/api/violation")
@CrossOrigin
public class ViolationController {

    @Autowired
    private IViolationService violationService;

    /**后台 查询违规条例*/
    @PostMapping(value = "/selectViolation")
    public Result selectViolation(){
        try {
            Violation violation= violationService.selectViolation();
            return Result.success(violation);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectViolation fail");
        }
    }
    /**后台 修改违规条例*/
    @PostMapping(value = "/updateViolation")
    public Result updateViolation(@RequestBody Violation violation){
        try {
            violationService.updateViolation(violation);
            return Result.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateViolation fail");
        }
    }
    /**后台 添加违规条例*/
    @PostMapping(value = "/addViolation")
    public Result addViolation(Violation violation){
        try {
            violationService.addViolation(violation);
            return Result.success("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("addViolation fail");
        }
    }
}
