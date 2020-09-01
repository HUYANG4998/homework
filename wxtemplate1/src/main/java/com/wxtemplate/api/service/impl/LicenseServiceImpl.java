package com.wxtemplate.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.License;
import com.wxtemplate.api.entity.Realname;
import com.wxtemplate.api.mapper.LicenseMapper;
import com.wxtemplate.api.service.ILicenseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 企业认证 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Service
public class LicenseServiceImpl extends ServiceImpl<LicenseMapper, License> implements ILicenseService {

    @Resource
    private LicenseMapper licenseMapper;
    @Override
    public List<Map<String,Object>> selectAllLicense(String licensenumber) {
        List<Map<String,Object>> listMap=licenseMapper.selectAllLicense(licensenumber);
        listMap.parallelStream().forEach((map)->{
            if(map.containsKey("status")){
                String status=map.get("status").toString();
                if("0".equals(status)){
                    map.put("status","待审核");
                }else if("1".equals(status)){
                    map.put("status","已通过");
                }else{
                    map.put("status","已驳回");
                }
            }
        });
        return listMap;
    }

    @Override
    public Map<String, Object> selectLicenseByLiceseId(String liceseid) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(liceseid)){
            map=licenseMapper.selectLicenseByLiceseId(liceseid);
        }
        return map;
    }

    @Override
    public void licenseAudit(Map<String, Object> result) {
        if(!result.isEmpty()){
            String status=result.get("status") == null?null:result.get("status").toString();
            String licenseid=result.get("licenseid") == null?null:result.get("licenseid").toString();
            if(!StringUtils.isEmpty(status)){
                License license=new License();
                if("2".equals(status)){
                    String cause=result.get("cause") == null?null:result.get("cause").toString();
                    license.setCause(cause);
                }
                license.setLicenseid(licenseid);
                license.setLicensestatus(status);
                licenseMapper.updateById(license);
            }
        }
    }
}
