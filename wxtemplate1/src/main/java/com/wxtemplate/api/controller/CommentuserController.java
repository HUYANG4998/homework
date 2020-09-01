package com.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Commentuser;
import com.wxtemplate.api.service.ICommentuserService;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论用户 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@RestController
@RequestMapping("/api/commentuser")
public class CommentuserController {

    @Autowired
    private ICommentuserService commentuserService;
    /**
     * 后台 根据订单id查询订单评价
     */
    @PostMapping(value = "/selectCommentUserByOrderid")
    public Result selectCommentUserByOrderid(String orderid){
        try {
            if(!StringUtils.isEmpty(orderid)){
                Map<String,Object> map= commentuserService.selectCommentUserByOrderid(orderid);
                return Result.success(map);

            }else{
                return Result.fail("条件不足");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectCommentUserByOrderid fail");
        }
    }
    /**
     * 后台 查询全部评价
     */
    @PostMapping(value = "/selectCommentUser")
    public Result selectCommentUser(String orderid){
        try {
                List<Map<String,Object>> commentMap= commentuserService.selectCommentUser();

                return Result.success(commentMap);


        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectCommentUser fail");
        }
    }
    /**
     * 后台 查询单个评价
     */
    @PostMapping(value = "/selectCommentUserByCommentid")
    public Result selectCommentUserByCommentid(String commentid){
        try {
            if(!StringUtils.isEmpty(commentid)){
               Map<String,Object> commentMap= commentuserService.selectCommentUserByCommentid(commentid);
               if(commentMap!=null&&!commentMap.isEmpty()){
                   return Result.success(commentMap);
               }else{
                   return Result.fail("检索失败");
               }
            }else{
                return Result.fail("条件不足");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectCommentUser fail");
        }
    }
    /**
     * 后台 修改评价
     */
    @PostMapping(value = "/updateCommentUserByCommentid")
    public Result updateCommentUserByCommentid(Commentuser commentuser){
        try {
            if(commentuser!=null){
                commentuserService.updateCommentUserByCommentid(commentuser);
                return Result.success("修改成功");
            }else{
                return Result.fail("修改失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectCommentUser fail");
        }
    }
}
