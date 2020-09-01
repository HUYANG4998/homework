package com.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wxtemplate.api.entity.Carcolor;
import com.wxtemplate.api.entity.Problem;
import com.wxtemplate.api.service.IPackageService;
import com.wxtemplate.api.service.IProblemService;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-16
 */
@RestController
@RequestMapping("/api/package")
@CrossOrigin
public class PackageController {

    @Autowired
    private IPackageService packageService;

    /**查询全部套餐*/
    @PostMapping(value = "/selectPackage")
    public Result selectPackage(){
        try {
            List<Map<String,Object>> packageList=packageService.selectPackage();
            return Result.success(packageList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectPackage fail");
        }
    }

    /**
     * 查询单个套餐 packageid
     * @param packageid
     */
    @PostMapping(value = "/selectPackageById")
    public Result selectPackageById(String packageid){
        try {
            if(!StringUtils.isEmpty(packageid)){
                Map<String,Object> listMap= packageService.selectPackageById(packageid);
                if(listMap!=null&&!listMap.isEmpty()){
                    return Result.success(listMap);
                }else{
                    return Result.fail("检索失败");
                }
            }else{
                return Result.fail("请增加条件");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectPackageById fail");
        }
    }
    /**
     * 添加套餐
     */
    @Transactional(rollbackFor=Exception.class)
    @PostMapping(value = "/addPackage")
    public Result addPackage(@RequestBody Map<String,Object> result){
        try {
            if(!result.isEmpty()){
                packageService.addPackage(result);
                return Result.success("添加成功");
            }else{
                return Result.fail("请增加条件");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("addPackage fail");
        }
    }
    /**
     * 修改套餐
     */
    @Transactional(rollbackFor=Exception.class)
    @PostMapping(value = "/updatePackage")
    public Result updatePackage(@RequestBody Map<String,Object> result){
        try {
            if(!result.isEmpty()){
                packageService.updatePackage(result);
                return Result.success("修改成功");
            }else{
                return Result.fail("请增加条件");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("updatePackage fail");
        }
    }
    /**
     * 删除套餐
     */
    @PostMapping(value = "/deletePackage")
    @Transactional(rollbackFor=Exception.class)
    public Result deletePackage(String packageid){
        try {
            if(!StringUtils.isEmpty(packageid)){
                packageService.deletePackage(packageid);
                return Result.success("删除成功");
            }else{
                return Result.fail("请增加条件");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("deletePackage fail");
        }
    }
    /**
     * 查询所有车的颜色
     */
    @PostMapping(value = "/selectCarColor")
    public Result selectCarColor(String carid){
        try {
            if(!StringUtils.isEmpty(carid)){
                List<Carcolor> carcolorList=packageService.selectCarColor(carid);
                return Result.success(carcolorList);
            }else{
                return Result.fail("请增加条件");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectCarColor fail");
        }
    }
    /**
     * 随机4个婚车套餐
     */
    @PostMapping(value = "/selectPacckageFour")
    public Result selectPacckageFour(){
        try {
                List<Map<String,Object>> packageList=packageService.selectPacckageFour();
                return Result.success(packageList);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectPacckageFour fail");
        }
    }
    /**
     * 前台查询全部婚车套餐
     */
    @PostMapping(value = "/selectWebPackage")
    public Result selectWebPackage(@RequestBody Map<String,Object> map){
        int pagesize = Integer
                .parseInt(map.get("pageSize") == null ? "10" : map.get("pageSize").toString());
        int pagenum = Integer
                .parseInt(map.get("pageNum") == null ? "1" : map.get("pageNum").toString());
        try {
            PageHelper.startPage(pagenum, pagesize, true, false, true);
            List<Map<String,Object>> packageList=packageService.selectWebPackage(map);
            PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(packageList);
            return Result.success(pageInfo);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectWebPackage fail");
        }
    }
}
