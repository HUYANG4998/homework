package com.wxtemplate.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxtemplate.wxtemplate.api.entity.Advertising;
import com.wxtemplate.wxtemplate.api.entity.Earn;
import com.wxtemplate.wxtemplate.api.entity.MoneyTemplate;
import com.wxtemplate.wxtemplate.api.entity.User;
import com.wxtemplate.wxtemplate.api.mapper.AdvertisingMapper;
import com.wxtemplate.wxtemplate.api.mapper.EarnMapper;
import com.wxtemplate.wxtemplate.api.mapper.MoneyTemplateMapper;
import com.wxtemplate.wxtemplate.api.mapper.UserMapper;
import com.wxtemplate.wxtemplate.api.service.IAdvertisingService;
import com.wxtemplate.wxtemplate.api.util.BigDecimalUtils;
import com.wxtemplate.wxtemplate.api.util.TimeUtils;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 广告表 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Service
public class AdvertisingServiceImpl extends ServiceImpl<AdvertisingMapper, Advertising> implements IAdvertisingService {

    @Resource
    private AdvertisingMapper advertisingMapper;
    @Resource
    private MoneyTemplateMapper moneyTemplateMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private EarnMapper earnMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insertAdvertising(Advertising advertising) {
        if(advertising!=null){

            MoneyTemplate moneyTemplate = moneyTemplateMapper.selectMoneyTemplate();
            if(moneyTemplate!=null){
                String status=advertising.getStatus();
                String type=advertising.getType();
                if("1".equals(status)&&!StringUtils.isEmpty(type)&&!StringUtils.isEmpty(status)){
                    if(!"3".equals(type)){
                        QueryWrapper qw = new QueryWrapper<Advertising>();
                        qw.eq("type",type);
                        qw.eq("status",status);
                        qw.eq("is_validity","1");
                        Optional<Integer> count = Optional.ofNullable(advertisingMapper.selectCount(qw));
                        if(count.get()==5){
                            return Result.fail("广告位不足");
                        }
                    }
                    BigDecimal price=null;
                    if("1".equals(type)){
                        price=moneyTemplate.getHomePage();
                        advertising.setStartTime(DateUtil.now());
                        advertising.setEndTime(DateUtil.format(DateUtil.offset(new Date(), DateField.MONTH, 1),TimeUtils.yyyyMMddHHmmss));
                    }else if("2".equals(type)){
                        price=moneyTemplate.getRedPackage();
                        advertising.setStartTime(DateUtil.now());
                        advertising.setEndTime(DateUtil.format(DateUtil.offset(new Date(), DateField.MONTH, 1),TimeUtils.yyyyMMddHHmmss));
                    }else if("3".equals(type)){
                        price=BigDecimalUtils.multiply(moneyTemplate.getFriendsCircle(),new BigDecimal(advertising.getNumberDays()));
                        advertising.setStartTime(DateUtil.now());
                        advertising.setEndTime(DateUtil.format(DateUtil.offset(new Date(), DateField.DAY_OF_MONTH, advertising.getNumberDays()),TimeUtils.yyyyMMddHHmmss));
                    }
                    User user=userMapper.selectById(advertising.getUserId());
                    if(user!=null){
                        if(user.getMoney().compareTo(price)>-1&&user.getAdvertisingNumber()>0){
                            user.setMoney(BigDecimalUtils.subtract(user.getMoney(),price));
                            user.setAdvertisingNumber(user.getAdvertisingNumber()-1);
                            userMapper.updateById(user);
                            Earn earn=new Earn(user.getUserId(),"广告费",price,"0",DateUtil.now());
                            earnMapper.insert(earn);
                        }else{

                            return Result.fail("余额不足");
                        }
                    }
                }else{
                    advertising.setStartTime(DateUtil.now());
                    advertising.setEndTime("2090-01-01 00:00:00");
                }
                advertising.setAddtime(DateUtil.now());
                advertising.setIsValidity("1");
                advertisingMapper.insert(advertising);
            }

        }
        return Result.success("发布成功");
    }

    @Override
    public void deleteAdvertising(List<String> advertisingIdList) {
        if(advertisingIdList!=null&&advertisingIdList.size()>0){
            for (String advertisingId:advertisingIdList){
                Advertising advertising = advertisingMapper.selectById(advertisingId);
                if(advertising!=null){
                    advertisingMapper.deleteById(advertising);
                }
            }
        }
    }

    @Override
    public void updateAdvertising(Advertising advertising) {
        if(advertising!=null){
            Advertising advertising1 = advertisingMapper.selectById(advertising.getAdvertisingId());
            if(advertising1!=null){
                advertisingMapper.updateById(advertising);
            }
        }
    }

    @Override
    public Advertising selectAdvertisingById(String advertisingId) {
        Advertising advertising=null;
        if(!StringUtils.isEmpty(advertisingId)){
            advertising=advertisingMapper.selectById(advertisingId);
        }
        return advertising;
    }

    @Override
    public List<Advertising> selectAllAdvertising(String type) {
        if("主页".equals(type)){
            type="1";
        }else if("红包".equals(type)){
            type="2";
        }else if("朋友圈".equals(type)){
            type="3";
        }else if("我的".equals(type)){
            type="4";
        }
        return advertisingMapper.selectAllAdvertising(type);
    }

    @Override
    public List<Advertising> selectAdvertisingFive(String type) {
        List<Advertising> advertisingList=new ArrayList<>();
        if(!StringUtils.isEmpty(type)){
            advertisingList=advertisingMapper.selectAdvertisingFive(type);
        }
        return advertisingList;
    }

    @Override
    public List<Advertising> selectAdvertingByUserId(String userId) {
        List<Advertising> list=new ArrayList<>();
        if(!StringUtils.isEmpty(userId)){
            list=advertisingMapper.selectAdvertingByUserId(userId);
        }
        return list;
    }
}
