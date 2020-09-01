package com.wxtemplate.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxtemplate.wxtemplate.api.entity.Dynamic;
import com.wxtemplate.wxtemplate.api.entity.Earn;
import com.wxtemplate.wxtemplate.api.entity.MoneyTemplate;
import com.wxtemplate.wxtemplate.api.entity.User;
import com.wxtemplate.wxtemplate.api.mapper.*;
import com.wxtemplate.wxtemplate.api.service.IDynamicService;
import com.wxtemplate.wxtemplate.api.util.*;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 动态表
 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Service
public class DynamicServiceImpl extends ServiceImpl<DynamicMapper, Dynamic> implements IDynamicService {

    @Resource
    private DynamicMapper dynamicMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private MoneyTemplateMapper moneyTemplateMapper;
    @Resource
    private EarnMapper earnMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private DynamicCollectionMapper dynamicCollectionMapper;

    @Override
    public List<Map<String,Object>> selectDynamicByCategoryId(String categoryId,String userId) {
        List<Map<String,Object>> dynamicList=new ArrayList<>();
        if(!StringUtils.isEmpty(categoryId)){
            dynamicList=dynamicMapper.selectDynamicByCategoryId(categoryId);
            if(dynamicList.size()>0){
                for (Map<String,Object> map:dynamicList){
                    String dynamicId = (String)map.get("dynamicId");
                    String hotTime = (String)map.get("hotTime");
                    String stickTime = (String)map.get("stickTime");
                    map.put("isHot","0");
                    map.put("isStick","0");
                    if(hotTime!=null){
                        if(DateUtil.now().compareTo(hotTime)<1){
                            map.put("isHot","1");
                        }
                    }
                    if(!StringUtils.isEmpty(stickTime)){
                        if(DateUtil.now().compareTo(stickTime)<1){
                            map.put("isStick","1");
                        }
                    }

                    Integer count=commentMapper.selectCommentPeopleByDynamicId(dynamicId);
                    map.put("commentPeople",count);
                    Map<String,Object> result=new HashMap<>();
                    result.put("dynamicId",dynamicId);
                    result.put("userId",userId);
                    result.put("type","0");
                    Integer giveLikeNumber=dynamicCollectionMapper.selectDynamicCollectionByUserIdAndDynamicIdAndType(result);
                    /**0未赞1已赞*/
                    map.put("isGiveLikeNumber",giveLikeNumber);
                    result.put("type","1");
                    Integer collectionNumber=dynamicCollectionMapper.selectDynamicCollectionByUserIdAndDynamicIdAndType(result);
                    /**0未收藏1已收藏*/
                    map.put("isCollectionNumber",collectionNumber);
                }
            }

        }
        return dynamicList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insertDynamic(Dynamic dynamic, String userId) {
        try {
            if(dynamic!=null&&!StringUtils.isEmpty(userId)){


                User user = userMapper.selectById(userId);
                if(user.getReleaseNumber()<1){
                    return Result.fail("发布次数已使用完");
                }
                Integer hotDay = dynamic.getHotDay();
                Integer stickDay = dynamic.getStickDay();
                boolean hotFlag=false;
                if(hotDay!=null){
                    hotFlag=hotDay>0;
                }
                boolean stickFlag=false;
                if(stickDay!=null){
                    stickFlag=stickDay>0;
                }
                MoneyTemplate moneyTemplate = moneyTemplateMapper.selectMoneyTemplate();
                if(hotFlag){
                    Integer count = dynamicMapper.selectDynamicCount("1");
                    if(count == 2){
                        return Result.fail("热门名额仅限2名");
                    }
                    BigDecimal multiply = new BigDecimal(hotDay).multiply(moneyTemplate.getHot());

                    dynamic.setHotTime(DateUtil.format(DateUtil.offsetDay(DateUtil.parse(DateUtil.now()), hotDay), TimeUtils.yyyyMMddHHmmss));
                    if(user.getMoney().compareTo(multiply)>-1){
                        user.setMoney(BigDecimalUtils.subtract(user.getMoney(),multiply));
                        Earn earn=new Earn(userId, EarnUtils.HOT,multiply,"0",DateUtil.now());
                        earnMapper.insert(earn);
                    }else{
                        throw new MyException("余额不足");
                    }
                }else{
                    dynamic.setHotTime(DateUtil.now());
                }
                if(stickFlag){
                    Integer count = dynamicMapper.selectDynamicCount("2");
                    if(count == 48){
                        return Result.fail("置顶名额仅限48名");
                    }
                    BigDecimal multiply = new BigDecimal(stickDay).multiply(moneyTemplate.getStick());
                    dynamic.setStickTime(DateUtil.format(DateUtil.offsetDay(DateUtil.parse(DateUtil.now()), stickDay),TimeUtils.yyyyMMddHHmmss));
                    if(user.getMoney().compareTo(multiply)>-1){
                        user.setMoney(BigDecimalUtils.subtract(user.getMoney(),multiply));
                        Earn earn=new Earn(userId, EarnUtils.STICK,multiply,"0",DateUtil.now());
                        earnMapper.insert(earn);
                    }else{
                        throw new MyException("余额不足");
                    }
                }else{
                    dynamic.setStickTime(DateUtil.now());
                }
                dynamic.setStick(Utils.getStickAndHot(hotFlag,stickFlag));
                /**红包钱数是否大于余额*/
                if(dynamic.getRedPackageMoney()!=null){
                    if(!(dynamic.getRedPackageMoney().compareTo(BigDecimal.ZERO)==0)){
                        if(user.getMoney().compareTo(dynamic.getRedPackageMoney())>-1){
                            user.setMoney(BigDecimalUtils.subtract(user.getMoney(),dynamic.getRedPackageMoney()));
                            Earn earn=new Earn(userId, EarnUtils.SENDREDPACKAGE,dynamic.getRedPackageMoney(),"0",DateUtil.now());
                            earnMapper.insert(earn);
                        }else{
                            throw new MyException("余额不足");
                        }
                    }
                }
                user.setReleaseNumber(user.getReleaseNumber()-1);
                userMapper.updateById(user);
                //访问人数
                dynamic.setVisitNumber(0);
                //点赞人数
                dynamic.setGiveLikeNumber(0);
                //收藏人数
                dynamic.setCollectNumber(0);
                /**时间*/
                dynamic.setRefreshTime(DateUtil.now());
                dynamic.setAddtime(DateUtil.now());
                dynamic.setUpdatetime(DateUtil.now());
                dynamicMapper.insert(dynamic);
            }else{
                return Result.fail("参数异常");
            }
        } catch (MyException e) {
            e.printStackTrace();
            return Result.fail("余额不足");
        } catch (Exception e){
            e.printStackTrace();
        }
        return Result.success("添加成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateDynamic(Dynamic dynamic, String userId) {
        try {
            if(dynamic!=null&&!StringUtils.isEmpty(userId)){
                User user = userMapper.selectById(userId);
                Integer hotDay = dynamic.getHotDay();
                Integer stickDay = dynamic.getStickDay();
                boolean hotSuccess=false;
                boolean stickSuccess=false;
                Dynamic dynamic1 = dynamicMapper.selectById(dynamic.getDynamicId());
                MoneyTemplate moneyTemplate = moneyTemplateMapper.selectMoneyTemplate();
                if(hotDay!=null){
                    Integer count = dynamicMapper.selectDynamicCount("1");
                    if(count == 2){
                        return Result.fail("热门名额仅限2名");
                    }
                    boolean hotFlag=hotDay>0;
                    int hot = DateUtil.now().compareTo(dynamic1.getHotTime());
                    hotSuccess= ExpirationTime.expirationTimeJudge(hot,hotFlag);
                    dynamic1.setHotTime(ExpirationTime.getExpirationTime(hot,hotFlag,dynamic1.getHotTime(),hotDay));
                    BigDecimal price=BigDecimalUtils.multiply(new BigDecimal(hotDay),moneyTemplate.getHot());
                    if(user.getMoney().compareTo(price)>-1){
                        user.setMoney(BigDecimalUtils.add(user.getMoney(),price));
                        Earn earn=new Earn(userId, EarnUtils.HOT,price,"0",DateUtil.now());
                        earnMapper.insert(earn);
                    }else{
                        throw new MyException("余额不足");
                    }
                }
                if(stickDay!=null){
                    Integer count = dynamicMapper.selectDynamicCount("2");
                    if(count == 48){
                        return Result.fail("置顶名额仅限48名");
                    }
                    boolean stickFlag=stickDay>0;
                    int stick = DateUtil.now().compareTo(dynamic1.getStick());

                    stickSuccess=ExpirationTime.expirationTimeJudge(stick,stickFlag);
                    dynamic1.setStickTime(ExpirationTime.getExpirationTime(stick,stickFlag,dynamic1.getStickTime(),stickDay));
                    BigDecimal price=BigDecimalUtils.multiply(new BigDecimal(stickDay),moneyTemplate.getStick());
                    if(user.getMoney().compareTo(price)>-1){
                        user.setMoney(BigDecimalUtils.add(user.getMoney(),price));
                        Earn earn=new Earn(userId, EarnUtils.STICK,price,"0",DateUtil.now());
                        earnMapper.insert(earn);
                    }else{
                        throw new MyException("余额不足");
                    }
                }
                dynamic1.setStick(Utils.getStickAndHot(hotSuccess, stickSuccess));
                dynamic1.setRefreshTime(DateUtil.now());
                dynamic1.setUpdatetime(DateUtil.now());
                userMapper.updateById(user);
                dynamicMapper.updateById(dynamic1);


            }
        } catch (MyException e) {
            e.printStackTrace();
            return Result.fail("余额不足");
        } catch (Exception e){
            e.printStackTrace();
            return Result.success("修改失败");
        }
        return Result.success("修改成功");
    }

    @Override
    public Result selectRedPackageDetails(String dynamicId, String userId) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(dynamicId)&&!StringUtils.isEmpty(userId)){
            Map<String,Object> earn = earnMapper.selectEarnByDynamicIdAndUserId(dynamicId, userId);
            List<Map<String,Object>> list=earnMapper.selectEarnByDynamicIdAndRemoveUserId(dynamicId,userId);
            if(earn!=null&&!earn.isEmpty()){
                map.put("myRed",earn);
                map.put("otherRed",list);
            }else{
                //没有抢红包
                map.put("myRed",null);
                map.put("otherRed",list);
            }
        }
        return Result.success(map);
    }

    @Override
    public boolean judgeRedWars(String dynamicId, String userId) {
        boolean isSuccess=false;
        if(!StringUtils.isEmpty(dynamicId)&&!StringUtils.isEmpty(userId)){
            Map<String,Object> earn = earnMapper.selectEarnByDynamicIdAndUserId(dynamicId, userId);
            if(earn!=null&&!earn.isEmpty()){
                isSuccess=true;
            }
        }
        return isSuccess;
    }

    @Override
    public void deleteDynamic(String dynamicId) {
        if(!StringUtils.isEmpty(dynamicId)){
            dynamicMapper.deleteById(dynamicId);
            dynamicCollectionMapper.deleteByDynamicId(dynamicId);
        }
    }

    @Override
    public Map<String,Object> selectDynamicByDynamicId(String dynamicId,String userId) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(dynamicId)){
            Dynamic dynamic=dynamicMapper.selectById(dynamicId);

            if(dynamic!=null){
                String hotTime=dynamic.getHotTime();
                String stickTime=dynamic.getStickTime();
                map.put("isHot","0");
                map.put("isStick","0");
                if(!StringUtils.isEmpty(hotTime)){
                    if(DateUtil.now().compareTo(hotTime)<1){
                        map.put("isHot","1");
                    }
                }
                if(!StringUtils.isEmpty(stickTime)){
                    if(DateUtil.now().compareTo(stickTime)<1){
                        map.put("isStick","1");
                    }
                }
                Integer count=commentMapper.selectCommentPeopleByDynamicId(dynamicId);
                    dynamic.setCommentPeople(count);
                Map<String,Object> result=new HashMap<>();
                result.put("dynamicId",dynamicId);
                result.put("userId",userId);
                result.put("type","0");
                Integer giveLikeNumber=dynamicCollectionMapper.selectDynamicCollectionByUserIdAndDynamicIdAndType(result);
                /**0未赞1已赞*/
                map.put("isGiveLikeNumber",giveLikeNumber);
                result.put("type","1");
                Integer collectionNumber=dynamicCollectionMapper.selectDynamicCollectionByUserIdAndDynamicIdAndType(result);
                /**0未收藏1已收藏*/
                map.put("isCollectionNumber",collectionNumber);


                map.put("dynamic",dynamic);

                if(!StringUtils.isEmpty(dynamic.getUserId())){
                    User user=userMapper.selectById(dynamic.getUserId());
                    if(user!=null){
                        map.put("user",user);
                    }
                }

            }
        }
        return map;
    }

    @Override
    public List<Dynamic> selectDynamicByUserId(String userId) {
        List<Dynamic> dynamicList=new ArrayList<>();
        if(!StringUtils.isEmpty(userId)){
            dynamicList=dynamicMapper.selectDynamicByUserId(userId);
            for (Dynamic dynamic:dynamicList){
                dynamic.setCommentPeople(commentMapper.selectCommentPeopleByDynamicId(dynamic.getDynamicId()));
            }
        }
        return dynamicList;
    }

    @Override
    public Result dynamicRefresh(String dynamicId) {
        String success="刷新次数已使用完";
        if(!StringUtils.isEmpty(dynamicId)){
            Dynamic dynamic = dynamicMapper.selectById(dynamicId);
            User user=userMapper.selectById(dynamic.getUserId());
            long between = DateUtil.between(DateUtil.parse(dynamic.getRefreshTime(), "yyyy-MM-dd HH:mm:ss"),
                    DateUtil.parse(DateUtil.now(), "yyyy-MM-dd HH:mm:ss"), DateUnit.SECOND);
            if(between <200){
                return Result.fail("200秒内禁止持续刷新，请耐心等待");
            }
            if(user.getReleaseNumber()>=1){
                user.setReleaseNumber(user.getReleaseNumber()-1);
                userMapper.updateById(user);
                dynamic.setRefreshTime(DateUtil.now());
                dynamicMapper.updateById(dynamic);
                success="刷新剩余次数："+user.getReleaseNumber();
            }
        }
        return Result.success(success);
    }

    @Override
    public void deleteAllDynamic() {
        dynamicMapper.deleteAllDynamic();
    }

    @Override
    public List<Map<String, Object>> selectRedPackageDynamic() {

        return dynamicMapper.selectRedPackageDynamic();
    }

    @Override
    public List<Map<String, Object>> selectDynamicByContent(String content) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        if(!StringUtils.isEmpty(content)){
            listMap=dynamicMapper.selectDynamicByContent(content);
        }
        return listMap;
    }

    @Override
    public List<Map<String, Object>> selectAllDynamic(String name) {

        return dynamicMapper.selectDynamicByContent(name);
    }

    @Override
    public List<Map<String, Object>> selectRedMoneyDynamic() {
        return dynamicMapper.selectRedMoneyDynamic();
    }
}
