package com.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Asset;
import com.wxtemplate.api.entity.VO.CarVo;
import com.wxtemplate.api.service.IAssetService;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资产表 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-06
 */
@RestController
@RequestMapping("/api/asset")
@CrossOrigin
public class AssetController {

    @Autowired
    private IAssetService assetService;

    /**
     * 后台  充值/提现审核 记录
     */
    @PostMapping(value = "/selectAssetToAudit")
    public Result selectAssetToAudit(String cate){
        try {
                List<Map<String,Object>> assetMap=assetService.selectAssetToAudit(cate);

                return Result.success(assetMap);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectAssetToAudit fail");
        }
    }
    /**
     * 后台  充值/提现记录
     */
    @PostMapping(value = "/selectAssetRecord")
    public Result selectAssetRecord(String cate){
        try {
            List<Map<String,Object>> assetMap=assetService.selectAssetRecord(cate);

            return Result.success(assetMap);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectAssetRecord fail");
        }
    }
    /**
     * 后台  查询单个资产信息
     */
    @PostMapping(value = "/selectAssetByAssetId")
    public Result selectAssetByAssetId(String assetid){
        try {
            if(!StringUtils.isEmpty(assetid)){
                Map<String,Object> assetMap=assetService.selectAssetByAssetId(assetid);
                if(assetMap!=null&&!assetMap.isEmpty()){
                    return Result.success(assetMap);
                }else{
                    return Result.fail("检索失败");
                }
            }else{
                return Result.fail("条件不足");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectAssetByAssetId fail");
        }
    }
    /**
     * 充值审核  通过不通过
     */
    @PostMapping(value = "/topUpAudit")
    @Transactional(rollbackFor=Exception.class)
    public Result topUpAudit(@RequestBody Map<String,Object> result){
        try {
            if(!result.isEmpty()){
                assetService.topUpAudit(result);
                return Result.success("审核成功");
            }else{
                return Result.fail("审核失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("topUpAudit fail");
        }
    }
    /**
     * 提现审核  通过不通过
     */
    @PostMapping(value = "/withdrawalAudit")
    @Transactional(rollbackFor=Exception.class)
    public Result withdrawalAudit(@RequestBody Map<String,Object> result){
        try {
            if(!result.isEmpty()){
                assetService.withdrawalAudit(result);
                return Result.success("审核成功");
            }else{
                return Result.fail("审核失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("withdrawalAudit fail");
        }
    }
}
