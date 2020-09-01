package com.wxtemplate.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.util.StringUtils;
import com.wxtemplate.wxtemplate.api.entity.Earn;
import com.wxtemplate.wxtemplate.api.entity.MoneyRecord;
import com.wxtemplate.wxtemplate.api.entity.User;
import com.wxtemplate.wxtemplate.api.mapper.EarnMapper;
import com.wxtemplate.wxtemplate.api.mapper.MoneyRecordMapper;
import com.wxtemplate.wxtemplate.api.mapper.UserMapper;
import com.wxtemplate.wxtemplate.api.service.IMoneyRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxtemplate.wxtemplate.api.util.BigDecimalUtils;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资金记录 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-27
 */
@Service
public class MoneyRecordServiceImpl extends ServiceImpl<MoneyRecordMapper, MoneyRecord> implements IMoneyRecordService {

    @Resource
    private MoneyRecordMapper moneyRecordMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private EarnMapper earnMapper;

    @Override
    public Result selectMoneyRecordByType(String type,String audit1) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        listMap=moneyRecordMapper.selectMoneyRecordByType(type,audit1);
        if(listMap.size()>0){
            for (Map<String,Object> map:listMap){
                String audit = (String)map.get("audit");
                String withdrawal = (String)map.get("withdrawal");
                if("0".equals(withdrawal)){
                    //充值
                    if("1".equals(audit)){
                        map.put("audit","充值成功");
                    }else{
                        map.put("audit","充值失败");
                    }
                }else{
                    //提现
                    if("0".equals(audit)){
                        map.put("audit","审核中");
                    }else if("1".equals(audit)){
                        map.put("audit","通过");
                    }else{
                        map.put("audit","不通过");
                    }
                }
            }
        }
        return Result.success(listMap);
    }

    @Override
    public Result selctMoneyRecordByMoneyRecordId(String moneyRecordId) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(moneyRecordId)){
            map=moneyRecordMapper.selctMoneyRecordByMoneyRecordId(moneyRecordId);
        }
        return Result.success(map);
    }

    @Override
    public Result withdrawalAudit(MoneyRecord moneyRecord) {
        if(moneyRecord!=null){
            String audit = moneyRecord.getAudit();
            MoneyRecord moneyRecord1 = moneyRecordMapper.selectById(moneyRecord.getMoneyRecordId());
            if(moneyRecord1!=null){
                User user = userMapper.selectById(moneyRecord1.getUserId());
                if("1".equals(audit)){
                    //通过
                }else if("2".equals(audit)){
                    //不通过
                    user.setMoney(BigDecimalUtils.add(user.getMoney(),moneyRecord1.getMoney()));
                    userMapper.updateById(user);
                    Earn earn=new Earn(user.getUserId(),"提现退回",moneyRecord1.getMoney(),"0", DateUtil.now());
                    earnMapper.insert(earn);
                }
                moneyRecordMapper.updateById(moneyRecord);
            }

        }
        return Result.success();
    }

    @Override
    public void changeStatus(String out_trade_no) {
        MoneyRecord moneyRecord = moneyRecordMapper.selectById(out_trade_no);
        if("0".equals(moneyRecord.getAudit())){
            moneyRecord.setAudit("1");
            moneyRecordMapper.updateById(moneyRecord);
            User user =userMapper.selectById(moneyRecord.getUserId());
            user.setMoney(BigDecimalUtils.add(user.getMoney(),moneyRecord.getMoney()));
            userMapper.updateById(user);
            Earn earn=new Earn(user.getUserId(),"充值",moneyRecord.getMoney(),"1",DateUtil.now());
            earnMapper.insert(earn);
        }
    }

    @Override
    public Result selectMoneyRecord(MoneyRecord moneyRecord) {
        User user = userMapper.selectById(moneyRecord.getUserId());
        if(user !=null){
            if(user.getMoney().compareTo(moneyRecord.getMoney()) >-1){
                if(user.getLimits() > 0){
                    moneyRecord.setAudit("0");
                    moneyRecord.setWithdrawal("1");
                    moneyRecord.setAddtime(DateUtil.now());
                    user.setMoney(BigDecimalUtils.subtract(user.getMoney(),moneyRecord.getMoney()));
                    user.setLimits(0);
                    userMapper.updateById(user);
                    Earn earn=new Earn(user.getUserId(),"提现",moneyRecord.getMoney(),"0",DateUtil.now());
                    earnMapper.insert(earn);
                    moneyRecordMapper.insert(moneyRecord);
                }else{
                    return Result.fail("每日限制提现一次");
                }


            }else{
                return Result.fail("余额不足");
            }
        }else{
            return Result.fail("用户未登录");
        }
        return null;
    }
}
