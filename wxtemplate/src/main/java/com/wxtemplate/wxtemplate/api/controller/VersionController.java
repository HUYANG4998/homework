package com.wxtemplate.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.wxtemplate.api.entity.Version;
import com.wxtemplate.wxtemplate.api.service.IVersionService;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  版本 控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-25
 */
@RestController
@RequestMapping("/api/version")
@CrossOrigin
public class VersionController {

    @Autowired
    private IVersionService versionService;

    /**查询单个版本*/
    @PostMapping(value = "/selectVersion")
    public Result selectViolation(){
        try {
            Version version= versionService.selectVersion();
            return Result.success(version);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectVersion fail");
        }
    }
    /**修改单个版本*/
    @PostMapping(value = "/updateVersion")
    public Result updateVersion(Version version){
        try {
            if(version!=null){
                versionService.updateVersion(version);
                return Result.success("修改失败");
            }else{
                return Result.fail("修改失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateVersion fail");
        }
    }
    /**添加单个版本*/
    @PostMapping(value = "/addVersion")
    public Result addVersion(Version version){
        try {
            if(version!=null){
                versionService.addVersion(version);
                return Result.success("添加成功");
            }else{
                return Result.fail("添加失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("addVersion fail");
        }
    }
    /**修改用户版本*/
    @PostMapping(value = "/contrastVersion")
    public Result contrastVersion(String version){
        try {
            if(!StringUtils.isEmpty(version)){
                return versionService.contrastVersion(version);

            }else{
                return Result.fail("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("contrastVersion fail");
        }
    }
}
