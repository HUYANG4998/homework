package com.wxtemplate.wxtemplate.api.controller;


import com.alibaba.druid.util.StringUtils;
import com.wxtemplate.wxtemplate.api.entity.Complaint;
import com.wxtemplate.wxtemplate.api.service.IComplaintService;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-08-10
 */
@RestController
@RequestMapping("/api/complaint")
public class ComplaintController {

    @Autowired
    private IComplaintService complaintService;

    /**
     * 查询是否投诉过
     * @param dynamicId
     * @param request
     * @return
     */
    @PostMapping("/selectComplaintByIsDynamicId")
    public Result selectComplaintByIsDynamicId(String dynamicId, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if(!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(dynamicId)){
            boolean isSuccess=complaintService.selectComplaintByIsDynamicId(dynamicId,userId);
            return Result.success(isSuccess);
        }else{
            return Result.fail("参数异常");
        }
    }
    /**
     * 查询投诉信息
     * @param dynamicId
     * @param request
     * @return
     */
    @PostMapping("/selectComplaintByDynamicId")
    public Result selectComplaintByDynamicId(String dynamicId, HttpServletRequest request) {
        if(!StringUtils.isEmpty(dynamicId)){
            List<Complaint> complaintList= complaintService.selectComplaintByDynamicId(dynamicId);
            return Result.success(complaintList);
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 删除投诉信息
     * @param id,
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public Result delete(String id, HttpServletRequest request) {
        if(!StringUtils.isEmpty(id)){
            complaintService.delete(id);
            return Result.success("删除成功");
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 投诉
     * @param complaint,
     * @param request
     * @return
     */
    @PostMapping("/insertComplaint")
    public Result insertComplaint(Complaint complaint, HttpServletRequest request) {
        if(complaint!=null){
            complaintService.insertComplaint(complaint);
            return Result.success("投诉成功");
        }else{
            return Result.fail("参数异常");
        }
    }
}
