package com.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.License;
import com.wxtemplate.api.entity.VO.CarVo;
import com.wxtemplate.api.service.ILicenseService;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 企业认证 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@RestController
@RequestMapping("/api/license")
@CrossOrigin
public class LicenseController {

    @Autowired
    private ILicenseService licenseService;
    /**后台 查询全部企业认证*/
    @PostMapping(value = "/selectAllLicense")
    public Result selectMyCar(String licensenumber){
        try {

                List<Map<String,Object>> licenseList= licenseService.selectAllLicense(licensenumber);

                return Result.success(licenseList);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectAllLicense fail");
        }
    }
    /**
     * 后台 查询单个企业认证
     */
    @PostMapping(value = "/selectLicenseByLinceseId")
    public Result selectLicenseByLinceseId(String liceseid){
        try {

            Map<String,Object> license= licenseService.selectLicenseByLiceseId(liceseid);
            if(license!=null&&!license.isEmpty()){
                return Result.success(license);
            }else{
                return Result.fail("检索失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectLicenseByLinceseId fail");
        }
    }
    /**
     * 后台 企业认证审核
     */
    @PostMapping(value = "/licenseAudit")
    public Result licenseAudit(@RequestBody Map<String,Object> result){
        try {
            if(!result.isEmpty()){
                licenseService.licenseAudit(result);
                return Result.success("审核成功");
            }else{
                return Result.fail("审核失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectLicenseByLinceseId fail");
        }
    }
}
