package com.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wxtemplate.api.service.IOfficialcarService;
import com.wxtemplate.tools.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 平台车 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@RestController
@RequestMapping("/api/officialcar")
@CrossOrigin
public class OfficialcarController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IOfficialcarService officialcarService;

    /**
     * 查询所有平台车  筛选 type 类型 order顺序 brand品牌
     * @param map
     */
    @PostMapping(value = "/selectofficialcar")
    public Result authentication(@RequestBody Map<String,Object> map){
        //分页参数
        int pagesize = Integer
                .parseInt(map.get("pageSize") == null ? "10" : map.get("pageSize").toString());
        int pagenum = Integer
                .parseInt(map.get("pageNum") == null ? "1" : map.get("pageNum").toString());
        try {
            PageHelper.startPage(pagenum, pagesize, true, false, true);
            List<Map<String,Object>> listMap= officialcarService.selectofficialcar(map);
            PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(listMap);
            return Result.success(pageInfo);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectofficialcar fail");
        }
    }
    /**
     * 查询好车推荐或热门车型   status 0 1
     * @param status
     */
    @PostMapping(value = "/selectofficialcarByStatus")
    public Result selectofficialcarByStatus(String status){
        try {
            if(!StringUtils.isEmpty(status)){
                List<Map<String,Object>> listMap= officialcarService.selectofficialcarByStatus(status);

                return Result.success(listMap);

            }else{
                return Result.fail("请增加条件");
            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectofficialcarByStatus fail");
        }
    }

    /**
     * 查询单个官网车辆 carid
     * @param carid
     */
    @PostMapping(value = "/selectofficialcarByCarId")
    public Result selectofficialcarByCarId(String carid){
        try {
            if(!StringUtils.isEmpty(carid)){
               Map<String,Object> listMap= officialcarService.selectofficialcarByCarId(carid);
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
            return Result.fail("selectofficialcarByCarId fail");
        }
    }
    /**
     * 添加平台车
     */
    @Transactional(rollbackFor=Exception.class)
    @PostMapping(value = "/addOfficialcar")
    public Result addOfficialcar(@RequestBody Map<String,Object> result){
        try {
            if(!result.isEmpty()){
               officialcarService.addOfficialcar(result);
                return Result.success("添加成功");
            }else{
                return Result.fail("请增加条件");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("addOfficialcar fail");
        }
    }
    /**
     * 修改平台车
     */
    @Transactional(rollbackFor=Exception.class)
    @PostMapping(value = "/updateOfficialcar")
    public Result updateOfficialcar(@RequestBody Map<String,Object> result){
        try {
            if(!result.isEmpty()){
                officialcarService.updateOfficialcar(result);
                return Result.success("修改成功");
            }else{
                return Result.fail("请增加条件");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("updateOfficialcar fail");
        }
    }
    /**
     * 删除平台车
     */
    @PostMapping(value = "/deleteOfficialcar")
    @Transactional(rollbackFor=Exception.class)
    public Result deleteOfficialcar(String carid){
        try {
            if(!StringUtils.isEmpty(carid)){
                officialcarService.deleteOfficialcar(carid);
                return Result.success("删除成功");
            }else{
                return Result.fail("请增加条件");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("deleteOfficialcar fail");
        }
    }
    /**
     * 查询全部平台车
     */
    @PostMapping(value = "/selectAllofficialcar")
    public Result selectAllofficialcar(String cartype){

        try {
            List<Map<String,Object>> listMap= officialcarService.selectAllofficialcar(cartype);
            return Result.success(listMap);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectAllofficialcar fail");
        }
    }
    /**
     * 查询平台车的所有评论
     */
    @PostMapping(value = "/selectofficialcarComment")
    public Result selectofficialcarComment(){
        try {
            List<Map<String,Object>> listMap= officialcarService.selectofficialcarComment();
            return Result.success(listMap);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectofficialcarComment fail");
        }
    }
}
