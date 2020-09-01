package com.wxtemplate.wxtemplate.api.controller;


import com.wxtemplate.wxtemplate.api.entity.Notice;
import com.wxtemplate.wxtemplate.api.service.INoticeService;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-07-12
 */
@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Resource
    private INoticeService noticeService;
    /**
     * 新增公告
     * @return
     */
    @PostMapping("/insertNotice")
    public Result insertNotice(Notice notice) {
        if(notice!=null){
            noticeService.insert(notice);
            return Result.success("添加成功");
        }else{
            return Result.fail("添加失败");
        }
    }
    /**
     * 查询全部公告
     * @return
     */
    @PostMapping("/selectNoticeAll")
    public Result selectNoticeAll() {
            List<Notice> noticeList=noticeService.selectNoticeAll();
            return Result.success(noticeList);
    }

    /**
     * 查询一个公告
     * @return
     */
    @PostMapping("/selectNoticeById")
    public Result selectNoticeById(String noticeId) {
        Notice notice=noticeService.selectNoticeById(noticeId);
        if(notice != null){
            return Result.success(notice);
        }else{
            return Result.fail("查询失败");
        }
    }

    /**
     * 删除一个公告
     * @return
     */
    @PostMapping("/deleteNotice")
    public Result deleteNotice(String noticeId) {
        noticeService.deleteNotice(noticeId);
        return Result.success("删除成功");
    }

    /**
     * 修改一个公告
     * @return
     */
    @PostMapping("/updateNotice")
    public Result updateNotice(Notice notice) {
        noticeService.updateNotice(notice);
        return Result.success("修改成功");
    }


}
