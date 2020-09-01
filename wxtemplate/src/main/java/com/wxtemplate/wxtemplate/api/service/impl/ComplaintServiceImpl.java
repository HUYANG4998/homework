package com.wxtemplate.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxtemplate.wxtemplate.api.entity.Complaint;
import com.wxtemplate.wxtemplate.api.mapper.ComplaintMapper;
import com.wxtemplate.wxtemplate.api.service.IComplaintService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-08-10
 */
@Service
public class ComplaintServiceImpl extends ServiceImpl<ComplaintMapper, Complaint> implements IComplaintService {

    @Resource
    private ComplaintMapper complaintMapper;

    @Override
    public boolean selectComplaintByIsDynamicId(String dynamicId, String userId) {
        boolean isSuccess = false;
        if (!StringUtils.isEmpty(dynamicId) && !StringUtils.isEmpty(userId)) {
            Integer count = complaintMapper.selectByDynamicIdAndUserIdCount(dynamicId, userId);
            if (count > 0) {
                isSuccess = true;
            }
        }
        return isSuccess;
    }

    @Override
    public List<Complaint> selectComplaintByDynamicId(String dynamicId) {
        List<Complaint> complaintList=new ArrayList<>();
        if(!StringUtils.isEmpty(dynamicId)){
            complaintList=complaintMapper.selectByDynamicId(dynamicId);
        }
        return complaintList;
    }

    @Override
    public void delete(String id) {
        if(!StringUtils.isEmpty(id)){
            complaintMapper.deleteById(id);
        }
    }

    @Override
    public void insertComplaint(Complaint complaint) {
        if(complaint!=null){
            complaint.setAddtime(DateUtil.now());
            complaintMapper.insert(complaint);
        }
    }
}
