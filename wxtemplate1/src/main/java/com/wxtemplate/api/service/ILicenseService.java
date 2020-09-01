package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.License;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 企业认证 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
public interface ILicenseService extends IService<License> {



    List<Map<String,Object>> selectAllLicense(String licensenumber);

    Map<String, Object> selectLicenseByLiceseId(String liceseid);

    void licenseAudit(Map<String, Object> result);
}
