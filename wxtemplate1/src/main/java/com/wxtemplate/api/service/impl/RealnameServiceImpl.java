package com.wxtemplate.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Car;
import com.wxtemplate.api.entity.Realname;
import com.wxtemplate.api.entity.User;
import com.wxtemplate.api.mapper.RealnameMapper;
import com.wxtemplate.api.mapper.UserMapper;
import com.wxtemplate.api.service.IRealnameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 实名 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Service
public class RealnameServiceImpl extends ServiceImpl<RealnameMapper, Realname> implements IRealnameService {

    @Resource
    private RealnameMapper realnameMapper;
    @Resource
    private UserMapper userMapper;
    @Override
    public List<Map<String, Object>> selectRealname(String realname) {
        List<Map<String,Object>> listMap=realnameMapper.selectAllRealName(realname);
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
    public Map<String, Object> selectRealnameByRealnameId(String realnameid) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(realnameid)){
            map=realnameMapper.selectRealnameByRealnameId(realnameid);
            if(map.containsKey("imgurl")){
                String imgurl= map.get("imgurl")==null?null:map.get("imgurl").toString();
                if(!StringUtils.isEmpty(imgurl)){
                    List<String> imgurlList = Arrays.asList(imgurl.split(";"));
                    if(imgurlList!=null){
                        map.put("imgurl",imgurlList);
                    }
                }
            }
        }
        return map;
    }

    @Override
    public void idcardAudit(Map<String, Object> result) {
        if(!result.isEmpty()){
            String status=result.get("status") == null?null:result.get("status").toString();
            String realnameid=result.get("realnameid") == null?null:result.get("realnameid").toString();
            if(!StringUtils.isEmpty(status)){
                Realname realname=new Realname();
                if("2".equals(status)){
                    String cause=result.get("cause") == null?null:result.get("cause").toString();
                    realname.setCause(cause);
                }
                realname.setRealnameid(realnameid);
                realname.setRealstatus(status);
                realnameMapper.updateById(realname);
                if("1".equals(status)){
                    Realname realnam1=realnameMapper.selectById(realnameid);
                    User user=userMapper.selectById(realnam1.getUserid());
                    user.setRealname("1");
                    userMapper.updateById(user);
                }


            }
        }
    }
}
