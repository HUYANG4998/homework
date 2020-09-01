package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.Earn;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wxtemplate.wxtemplate.tools.Result;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 收益记录 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface IEarnService extends IService<Earn> {

    Result sendRedPackage(String dynamicId, String userId);

    List<Map<String,Object>> selectBillRecords(String userId);
}
