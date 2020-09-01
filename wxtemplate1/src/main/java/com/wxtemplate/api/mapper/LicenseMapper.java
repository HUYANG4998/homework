package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.License;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 企业认证 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
public interface LicenseMapper extends BaseMapper<License> {

    /**获取企业认证信息*/
    Map<String,Object> selectLicenseByUserId(String userid);

    List<Map<String, Object>> selectAllLicense(@Param("licensenumber") String licensenumber);

    Map<String, Object> selectLicenseByLiceseId(String liceseid);

    License selectLicByUserId(@Param("userid") String userid);
}
