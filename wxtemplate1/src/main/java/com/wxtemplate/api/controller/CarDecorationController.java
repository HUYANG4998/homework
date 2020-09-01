package com.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.CarDecoration;
import com.wxtemplate.api.service.ICarDecorationService;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-24
 */
@RestController
@RequestMapping("/api/car-decoration")
@CrossOrigin
public class CarDecorationController {

    @Autowired
    private ICarDecorationService carDecorationService;

    @PostMapping(value = "/selectDecoration")
    public Result selectMyCar(HttpServletRequest request){
        try {
            List<CarDecoration> Decoration= carDecorationService.findDecoration();

            return Result.success(Decoration);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectDecoration fail");
        }
    }
    @PostMapping(value = "/selectDecorationOne")
    public Result selectMyCar(String carDecorationId){
        try {
            if(!StringUtils.isEmpty(carDecorationId)){
                CarDecoration Decoration= carDecorationService.selectDecorationOne(carDecorationId);

                return Result.success(Decoration);
            }else{
                return Result.fail("检索失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectDecorationOne fail");
        }
    }
    @PostMapping(value = "/updateDecoration")
    public Result updateDecoration(@RequestBody CarDecoration carDecoration){
        try {
            if(carDecoration!=null){
                carDecorationService.updateDecoration(carDecoration);
                return Result.success("修改成功");
            }else{
                return Result.fail("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateDecoration fail");
        }
    }
    @PostMapping(value = "/addDecoration")
    public Result addDecoration(@RequestBody CarDecoration carDecoration){
        try {
            if(carDecoration!=null){
                carDecorationService.addDecoration(carDecoration);
                return Result.success("添加成功");
            }else{
                return Result.fail("添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("addDecoration fail");
        }
    }
    @PostMapping(value = "/deleteDecoration")
    public Result deleteDecoration( String  carDecorationId){
        try {
            if(!StringUtils.isEmpty(carDecorationId)){
                carDecorationService.deleteDecoration(carDecorationId);
                return Result.success("删除成功");
            }else{
                return Result.fail("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("deleteDecoration fail");
        }
    }


}
