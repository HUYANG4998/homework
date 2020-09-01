package com.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Notice;
import com.wxtemplate.api.entity.Problem;
import com.wxtemplate.api.service.INoticeService;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 系统通知 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-08
 */
@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Autowired
    private INoticeService noticeService;
    /**
     * 查询个人通知描述列表
     * @return
     */
    @PostMapping(value = "/selectNotice")
    public Result selectNotice(HttpServletRequest request){
        String userid=(String)request.getAttribute("userid");
        try {
            if(!StringUtils.isEmpty(userid)){
                List<Notice> noticeList=noticeService.selectNotice(userid);
                return Result.success(noticeList);
            }else{
                return Result.fail("用户未登录");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectNotice fail");
        }
    }
    /**查询系统通知*/
    @PostMapping(value = "/selectNoticeSys")
    public Result selectNoticeSys(){
        try {
                List<Notice> noticeList=noticeService.selectNoticeSys();
                return Result.success(noticeList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectNoticeSys fail");
        }
    }
    /**
     * 删除一条系统通知
     * @return
     */
    @PostMapping(value = "/deleteNotice")
    public Result deleteNotice(String noticeid){
        try {
            if(!StringUtils.isEmpty(noticeid)){
                noticeService.deleteNotice(noticeid);
                return Result.success("删除成功");
            }else{
                return Result.fail("删除失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("deleteNotice fail");
        }
    }


    /**
     * 修改一条系统通知
     */
    @PostMapping(value = "/updateNotice")
    public Result updateNotice(Notice notice){
        try {
            if(notice!=null){
                noticeService.updateNotice(notice);
                return Result.success("修改成功");
            }else{
                return Result.fail("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateNotice fail");
        }
    }

    /**
     * 查询通知数量
     */
    @PostMapping(value = "/selectNoticeCount")
    public Result selectNoticeCount(HttpServletRequest request){
        String userid=(String)request.getAttribute("userid");
        try {
            if(!StringUtils.isEmpty(userid)){
                Integer count=noticeService.selectNoticeCount(userid);
                return Result.success(count);
            }else{
                return Result.fail("用户未登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateNotice fail");
        }
    }

}
