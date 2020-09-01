package com.wxtemplate.wxtemplate.api.controller;


import com.wxtemplate.wxtemplate.api.entity.DynamicCollection;
import com.wxtemplate.wxtemplate.api.service.IDynamicCollectionService;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 动态点赞/收藏 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@RestController
@RequestMapping("/api/dynamic-collection")
@CrossOrigin
public class DynamicCollectionController {

    @Autowired
    private IDynamicCollectionService dynamicCollectionService;

    /**
     * 添加点赞/收藏
     *
     * @param dynamicCollection
     * @return
     */
    @PostMapping("/insertDynamicCollection")
    public Result insertCommentDynamic(DynamicCollection dynamicCollection) {
        if (dynamicCollection != null) {
            dynamicCollectionService.insertDynamicCollection(dynamicCollection);
            if ("0".equals(dynamicCollection.getType())) {
                return Result.success("点赞成功");
            } else {
                return Result.success("收藏成功");
            }

        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 取消点赞/收藏--点赞/收藏id
     *
     * @param dynamicCollection
     * @return
     */
    @PostMapping("/deleteDynamicCollection")
    public Result deleteDynamicCollection(DynamicCollection dynamicCollection) {
        if (dynamicCollection != null) {
            dynamicCollectionService.deleteDynamicCollection(dynamicCollection);
            return Result.success("取消成功");
        } else {
            return Result.fail("参数异常");
        }
    }


}
