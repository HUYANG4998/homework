package com.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.VO.CarVo;
import com.wxtemplate.api.service.ICarService;
import com.wxtemplate.tools.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 个人车 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@RestController
@RequestMapping("/api/car")
@CrossOrigin
public class CarController {

    @Autowired
    private ICarService carService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    /**展示车辆*/
    @PostMapping(value = "/selectMyCar")
    public Result selectMyCar(@RequestBody Map<String,Object> map,HttpServletRequest request){
       /* String userid=(String)request.getAttribute("userid");*/
        /*String userid="234";*/
        try {
            if(map!=null&&!map.isEmpty()){
               List<Map<String,Object>> carList= carService.selectMyCar(map);
               return Result.success(carList);
            }else{
                return Result.fail("用户id为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectMyCar fail");
        }
    }
    /**
     * 后台 查询全部车辆信息
     */
    @PostMapping(value = "/selectAllMyCar")
    public Result selectAllMyCar(String carnumber){
        try {
                List<Map<String,Object>> carList= carService.selectAllMyCar(carnumber);
                return Result.success(carList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectAllMyCar fail");
        }
    }
    /**
     * 后台 查询单个车辆信息
     */
    @PostMapping(value = "/selectMyCarByCarid")
    public Result selectMyCarByCarid(String carid){
        try {
            Map<String,Object> carMap= carService.selectMyCarByCarid(carid);
            if(carMap!=null&&!carMap.isEmpty()){
                return Result.success(carMap);
            }else{
                return Result.fail("检索失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectMyCarByCarid fail");
        }
    }
    /**
     * 后台 审核
     */
    @PostMapping(value = "/carAudit")
    public Result carAudit(@RequestBody Map<String,Object> map){
        try {
            if(!map.isEmpty()){
                carService.carAudit(map);
                return Result.success("审核成功");
            }else{
                return Result.fail("条件不足");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectMyCarByCarid fail");
        }
    }
}
