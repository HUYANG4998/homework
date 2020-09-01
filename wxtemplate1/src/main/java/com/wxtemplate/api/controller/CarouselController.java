package com.wxtemplate.api.controller;


import com.wxtemplate.api.service.ICarouselService;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 轮播图 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@RestController
@RequestMapping("/api/carousel")
@CrossOrigin
public class CarouselController {
    @Autowired
    private ICarouselService carouselService;
    /**查询轮播图*/
    @PostMapping(value = "/selectCarousel")
    public Result selectMyCar(HttpServletRequest request){
        try {
                List<String> carouselList= carouselService.findCarousel();

                return Result.success(carouselList);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectCarousel fail");
        }
    }
    /**添加*/
    @PostMapping(value = "/addCarousel")
    @Transactional(rollbackFor=Exception.class)
    public Result addCarousel(@RequestBody List<String> url){
        try {
            if(url.size()>0){
                carouselService.addCarousel(url);
                return Result.success("添加成功");
            }else{
                return Result.fail("请上传图片");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("addCarousel fail");
        }
    }

}
