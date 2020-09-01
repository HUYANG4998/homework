package com.wxtemplate.api.controller;


import com.wxtemplate.api.entity.Violation;
import com.wxtemplate.api.entity.Webpic;
import com.wxtemplate.api.service.IWebpicService;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 网站三个大图 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-23
 */
@RestController
@RequestMapping("/api/webpic")
@CrossOrigin
public class WebpicController {

    @Autowired
    private IWebpicService webpicService;

    /**查询网站图片信息*/
    @PostMapping(value = "/selectWebPic")
    public Result selectWebPic(){
        try {
            Webpic webpic= webpicService.selectWebPic();
            return Result.success(webpic);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectWebPic fail");
        }
    }
    /**修改网站图片*/
    @PostMapping(value = "/updateWebPic")
    public Result updateWebPic(Webpic webpic){
        try {
            if(webpic!=null){
                webpicService.updateWebPic(webpic);
                return Result.success("修改成功");
            }else{
                return Result.fail("修改失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateWebPic fail");
        }
    }
    /**添加网站图片*/
    @PostMapping(value = "/addWebPic")
    public Result selectViolation(Webpic webpic){
        try {
            if(webpic!=null){
                webpicService.addWebPic(webpic);
                return Result.success("添加成功");
            }else{
                return Result.fail("添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("addWebPic fail");
        }
    }

}
