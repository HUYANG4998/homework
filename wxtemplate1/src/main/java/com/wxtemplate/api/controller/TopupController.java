package com.wxtemplate.api.controller;


import com.wxtemplate.api.entity.Violation;
import com.wxtemplate.api.service.ITopupService;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 手动充值记录 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-30
 */
@RestController
@RequestMapping("/api/topup")
@CrossOrigin
public class TopupController {

    @Autowired
    private ITopupService topupService;

    @PostMapping(value = "/selectTopup")
    public Result selectTopup(String phone){
        try {
            List<Map<String,Object>> topupList= topupService.selectTopup(phone);
            return Result.success(topupList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectTopup fail");
        }
    }
}
