package com.wxtemplate.api.controller;


import com.wxtemplate.api.entity.Fendback;
import com.wxtemplate.api.service.IFendbackService;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 反馈表 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@RestController
@RequestMapping("/api/fendback")
@CrossOrigin
public class FendbackController {

    @Autowired
    private IFendbackService fendbackService;

    /**查询全部反馈信息*/
    @PostMapping(value = "/selectFeedback")
    public Result selectFeedback(){
        try {
                List<Fendback> fendbackList= fendbackService.selectFeedback();
                return Result.success(fendbackList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectFeedback fail");
        }
    }
    /**查询单个反馈信息*/
    @PostMapping(value = "/selectFeedbackByFeedbackId")
    public Result selectFeedbackByFeedbackId(String feedbackid){
        try {
            Fendback fendback= fendbackService.selectFeedbackByFeedbackId(feedbackid);
            if(fendback!=null){
                return Result.success(fendback);
            }else{
                return Result.fail("检索失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectFeedback fail");
        }
    }
    /**删除单个反馈信息*/
    @PostMapping(value = "/deleteFeedback")
    public Result deleteFeedback(String feedbackid){
        try {
            Fendback fendback = fendbackService.selectFeedbackByFeedbackId(feedbackid);
            if(fendback!=null){
                fendbackService.deleteFeedback(feedbackid);

                return Result.success(fendback);
            }else{
                return Result.fail("反馈已删除");
            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("deleteFeedback fail");
        }
    }

}
