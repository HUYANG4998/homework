package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.Complaint;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-08-10
 */
public interface IComplaintService extends IService<Complaint> {

    boolean selectComplaintByIsDynamicId(String dynamicId, String userId);

    List<Complaint> selectComplaintByDynamicId(String dynamicId);

    void delete(String id);

    void insertComplaint(Complaint complaint);
}
