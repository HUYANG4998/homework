package com.wxtemplate.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.util.StringUtils;
import com.wxtemplate.wxtemplate.api.entity.Dynamic;
import com.wxtemplate.wxtemplate.api.entity.Earn;
import com.wxtemplate.wxtemplate.api.entity.User;
import com.wxtemplate.wxtemplate.api.mapper.DynamicMapper;
import com.wxtemplate.wxtemplate.api.mapper.EarnMapper;
import com.wxtemplate.wxtemplate.api.mapper.UserMapper;
import com.wxtemplate.wxtemplate.api.service.IEarnService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxtemplate.wxtemplate.api.util.BigDecimalUtils;
import com.wxtemplate.wxtemplate.api.util.EarnUtils;
import com.wxtemplate.wxtemplate.api.util.RedPackageUtils;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 收益记录 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Service
public class EarnServiceImpl extends ServiceImpl<EarnMapper, Earn> implements IEarnService {

    @Resource
    private EarnMapper earnMapper;
    @Resource
    private DynamicMapper dynamicMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public Result sendRedPackage(String dynamicId,String userId) {
        if(!StringUtils.isEmpty(dynamicId)){
            Dynamic dynamic = dynamicMapper.selectById(dynamicId);
            if(dynamic!=null){
                if(dynamic.getRedPackageNumber()==0){
                    return Result.fail("红包抢完了");
                }
                BigDecimal randomMoney = RedPackageUtils.getRandomMoney(dynamic);
                User user=userMapper.selectById(userId);
                user.setMoney(BigDecimalUtils.add(user.getMoney(),randomMoney));
                userMapper.updateById(user);
                Earn earn=new Earn(userId, EarnUtils.REDWARS,randomMoney,"1", DateUtil.now(),dynamicId);
                earnMapper.insert(earn);
                dynamicMapper.updateById(dynamic);
            }
        }
        return Result.success("抢红包成功");
    }

    @Override
    public List<Map<String,Object>> selectBillRecords(String userId) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        if(!StringUtils.isEmpty(userId)){
            listMap=earnMapper.selectBillRecords(userId);
        }
        return listMap;
    }
}
