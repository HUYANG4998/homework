package com.wxtemplate.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.wxtemplate.wxtemplate.api.entity.Dynamic;
import com.wxtemplate.wxtemplate.api.entity.Earn;
import com.wxtemplate.wxtemplate.api.entity.Friends;
import com.wxtemplate.wxtemplate.api.entity.User;
import com.wxtemplate.wxtemplate.api.mapper.*;
import com.wxtemplate.wxtemplate.api.service.IUserService;
import com.wxtemplate.wxtemplate.api.util.*;
import com.wxtemplate.wxtemplate.handler.MyWebSocket;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private DynamicMapper dynamicMapper;
    @Resource
    private DynamicCollectionMapper dynamicCollectionMapper;
    @Resource
    private EarnMapper earnMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Resource
    private FriendsMapper friendsMapper;
    @Resource
    private EndUserMapper endUserMapper;
    @Resource
    private CommentMapper commentMapper;
    @Value("${info.imgpath}")
    private String imgpath;
    @Value("${info.imgget}")
    private String imgget;

    @Override
    public List<Map<String, Object>> selectDynamicCollection(String userId,String type,Integer pageNo,Integer pageSize) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        if(!StringUtils.isEmpty(userId)&&!StringUtils.isEmpty(type)){
            if("0".equals(type)){
                //我的收藏
                PageHelper.startPage(pageNo, pageSize, true, false, true);
                listMap=dynamicCollectionMapper.selectDynamicCollectionByUserId(userId);
            }else{
                //收藏我的
                List<Dynamic> dynamicList=dynamicMapper.selectDynamicByUserId(userId);
                PageHelper.startPage(pageNo, pageSize, true, false, true);
                listMap=dynamicCollectionMapper.selectDynamicCollectionByMyDynamic(dynamicList);

            }
        }
        return listMap;
    }

    @Override
    public Result register(String phone, String password,String code) {
        if(!StringUtils.isEmpty(phone)&&!StringUtils.isEmpty(password)&&!StringUtils.isEmpty(code)){
            User user1=userMapper.selectbyPhone(phone);
            String endUserId=endUserMapper.selectRandomOne();
            if(user1==null){
                if(redisUtils.hasKey(phone+Constant.CODE)){
                    String redisCode = redisUtils.get(phone + Constant.CODE);
                    if(code.equals(redisCode)){
                        //可以注册
                        User user=new User();
                        user.setSerNumber(RandomSix.Num());
                        user.setPhone(phone);
                        user.setPassword(password);
                        user.setHeadImage("默认头像");
                        user.setNickname("用户"+RandomSix.Num());
                        user.setAddtime(DateUtil.now());
                        user.setUpdatetime(DateUtil.now());
                        user.setMoney(new BigDecimal(0));
                        user.setReleaseNumber(1);
                        user.setVip("0");
                        user.setAdvertisingNumber(10);
                        user.setPermission("1");
                        user.setHeadImage("http://119.45.187.154:8088/image/register.jpg");
                        String uuid = Utils.getUUID();
                        user.setQrCode(imgget+uuid+".png");
                        QRCodeGenerator.getBarCode(user.getUserId()+",1",imgpath+uuid+".png");
                        userMapper.insert(user);
                        Friends friends=new Friends();
                        friends.setFromUserId(user.getUserId());
                        friends.setToUserId(endUserId);
                        friends.setAddtime(DateUtil.now());
                        friendsMapper.insert(friends);
                        redisUtils.delete(phone + Constant.CODE);

                    }else{
                        return Result.fail("验证码错误");
                    }
                }else{
                    return Result.fail("验证码已失效！");
                }
            }else{
                return Result.fail("手机号重复");
            }
        }
        return Result.success("注册成功");
    }

    @Override
    public Result SignIn(String phone, String password) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(phone)&&!StringUtils.isEmpty(password)){
            User user = userMapper.selectbyPhone(phone);
            if(user!=null){
                if(password.equals(user.getPassword())){
                    String token = JwtHelper.createJWT(user.getPhone(), user.getUserId(), 365 * 24 * 60 * (60 * 1000), "base64security");
                    String tokenRedis = user.getUserId() + "_" + user.getPhone() + Constant.TOKENSUFFIX;
                    redisUtils.set(tokenRedis, token,3600*24*30);
                    map.put("user",userMapper.selectById(user.getUserId()));
                    map.put("token",token);
                }else{
                    return Result.fail("密码错误");
                }
            }else{
                return Result.fail("登录名未注册");
            }
        }
        return Result.success(map);
    }

    @Override
    public Result forgetPassword(String phone, String code, String newPassword) {
        if(!StringUtils.isEmpty(phone)&&!StringUtils.isEmpty(code)&&!StringUtils.isEmpty(newPassword)){
            User user = userMapper.selectbyPhone(phone);
            if(user!=null){
                if(redisUtils.hasKey(user.getPhone()+Constant.CODE)){
                    String redisCode = redisUtils.get(user.getPhone() + Constant.CODE);
                    if(code.equals(redisCode)){
                        user.setPassword(newPassword);
                        userMapper.updateById(user);
                        redisUtils.delete(phone + Constant.CODE);
                    }else{
                        return Result.fail("验证码错误");
                    }
                }else{
                    return Result.fail("验证码已失效！");
                }
            }else{
                return Result.fail("手机号未注册");
            }
        }
        return Result.fail("修改密码成功");
    }

    @Override
    public List<User> selectAllUser(String phone,String lastUserId) {
        return userMapper.selectAllUser(phone,lastUserId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result renewalVip(String userId) {
        if(!StringUtils.isEmpty(userId)){
            User user = userMapper.selectById(userId);
            if(user.getMoney().compareTo(new BigDecimal(30))>-1){
                user.setMoney(BigDecimalUtils.subtract(user.getMoney(),new BigDecimal(30)));

                if(!StringUtils.isEmpty(user.getVipExpirationTime())){
                    if(DateUtil.now().compareTo(user.getVipExpirationTime())<1){
                        user.setVipExpirationTime(DateUtil.format(DateUtil.offset(DateUtil.parse(user.getVipExpirationTime()), DateField.MONTH, 1),TimeUtils.yyyyMMddHHmmss));
                    }else{
                        user.setVipExpirationTime(DateUtil.format(DateUtil.offset(new Date(), DateField.MONTH, 1),TimeUtils.yyyyMMddHHmmss));
                        user.setAdvertisingNumber(30);
                        user.setReleaseNumber(10);
                    }
                }else{
                    user.setVipExpirationTime(DateUtil.format(DateUtil.offset(new Date(), DateField.MONTH, 1),TimeUtils.yyyyMMddHHmmss));
                    user.setAdvertisingNumber(30);
                    user.setReleaseNumber(10);
                }
                user.setVip("1");
                userMapper.updateById(user);
                Earn earn=new Earn(user.getUserId(),"续费会员",new BigDecimal(30),"0",DateUtil.now());
                earnMapper.insert(earn);

            }else{
                return Result.fail("余额不足");
            }
        }
        return Result.success("续费成功");
    }

    @Override
    public Map<String, Object> selectIsFriendsUser(String userId, String myUserId) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(userId)&&!StringUtils.isEmpty(myUserId)){
            Integer count = friendsMapper.selectFriendsByUserIdAndFriendsId(userId, myUserId);
            map.put("isFriends",count);
            User user = userMapper.selectById(userId);
            map.put("user",user);
        }
        return map;
    }

    @Override
    public void deleteUser(String userId) {
        if(!StringUtils.isEmpty(userId)){
            userMapper.deleteById(userId);
        }
    }

    @Override
    public void updateJurisdiction(User user) {
        if(user!=null){
            User user1 = userMapper.selectById(user.getUserId());
            String permission = user.getPermission();
            if("0".equals(permission)||"2".equals(permission)){

                redisUtils.delete(user1.getUserId() + "_" + user1.getPhone() + Constant.TOKENSUFFIX);

            }
            userMapper.updateById(user);
        }
    }

    @Override
    public Map<String, Object> selectSevenDay(Integer number) {
        Map<String,Object> map=new HashMap<>();
        List<Integer> userNumber=new LinkedList<>();
        List<String> day=new LinkedList<>();
        for(int i=number;i>0;i--){
            String format = DateUtil.format(DateUtil.offset(new Date(), DateField.DAY_OF_MONTH, -i), "yyyy-MM-dd");
            userNumber.add(userMapper.selectLikeAddTime(format,null));
            day.add(DateUtil.format(DateUtil.parse(format),"MM-dd"));
        }
        map.put("day",day);
        map.put("userNumber",userNumber);
        return map;
    }

    @Override
    public Map<String, Object> selectCountUser() {
        Map<String,Object> map=new HashMap<>();
        //今日新增用户
        map.put("today",userMapper.selectLikeAddTime(DateUtil.format(new Date(), "yyyy-MM-dd"),null));
        //昨日新增用户
        map.put("lastday",userMapper.selectLikeAddTime(DateUtil.format(DateUtil.offset(new Date(), DateField.DAY_OF_MONTH, -1), "yyyy-MM-dd"),null));
        //累计用户总数
        map.put("sum",userMapper.selectLikeAddTime(null,null));
        //当前人数上线
        map.put("online",MyWebSocket.userInfo.size());
        //当月开通Vip
        map.put("vip",userMapper.selectLikeAddTime(null,"1"));
        return map;
    }

    @Override
    public Result selectSubordinate(String userId,String index) {
        List<User> userList=new ArrayList<>();
        if(!StringUtils.isEmpty(userId)&&!StringUtils.isEmpty(index)){
            if("1".equals(index)){
                userList=userMapper.selectSubordinate(userId);
            }else{
                List<User> coutList=userMapper.selectSubordinate(userId);
                for (User user:coutList)
                    digui(user.getUserId(),7,userList);
                }
        }
        return Result.success(userList);
    }

    @Override
    public Result selectMyReferees(String userId) {
        User user=null;
        if(!StringUtils.isEmpty(userId)){
            User user1 = userMapper.selectById(userId);
            if(user1!=null){
                if(!StringUtils.isEmpty(user1.getLastUserId())){
                    user=userMapper.selectById(user1.getLastUserId());
                }
            }
        }
        return Result.success(user);
    }

    @Override
    public List<Map<String, Object>> selectComment(String userId, Integer pageNo, Integer pageSize) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        if(!StringUtils.isEmpty(userId)){
            List<Dynamic> dynamicList=dynamicMapper.selectDynamicByUserId(userId);
            List<String> list=new ArrayList<>();

            for (Dynamic dynamic : dynamicList){
                list.add(dynamic.getDynamicId());
            }
            if(list.size()>0){
                listMap=commentMapper.selectCommentByListDynamicId(list);
            }


        }
        return listMap;
    }

    public void digui(String userId, Integer index, List<User> userList){
        if(!StringUtils.isEmpty(userId)&&index>0){


                List<User> users = userMapper.selectSubordinate(userId);


                if(users.size()>0){
                    List<Integer> list=new ArrayList<>();
                    for (int j=0;j<users.size();j++){
                        list.add(index);
                    }
                    Integer[] l=list.toArray(new Integer[0]);
                    for (int i=0;i< users.size();i++) {
                        User user = users.get(i);
                        //通过userId查询用户订单
                        if(!StringUtils.isEmpty(user.getUserId())){
                            userList.add(user);
                            digui(user.getUserId(),--l[i],userList);
                        }
                    }
                }else{
                    return;
                }


        }else{
            return;
        }
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startTime() {
        CronUtil.schedule("*/1 * * * *", new Task() {

                @Override
                public void execute() {
                    //VIP到期时间处理
                    List<User> userList = userMapper.selectUser();
                    String now=DateUtil.now();
                    if(userList.size()>0){
                        for (User user:userList){
                            if("1".equals(user.getVip())){
                                if(now.compareTo(user.getVipExpirationTime())>-1){
                                    user.setVip("0");
                                    userMapper.updateById(user);
                                }
                            }


                        }
                    }
                    //动态置顶和热门时间处理
                    List<Dynamic> dynamicList = dynamicMapper.selectDynamic();
                    if(dynamicList.size()>0){
                        for (Dynamic dynamic:dynamicList){
                            boolean hot=false;
                            boolean stick=false;
                            if(!StringUtils.isEmpty(dynamic.getHotTime())){
                                if(now.compareTo(dynamic.getHotTime())<1){
                                    hot=true;
                                }
                            }
                            if(!StringUtils.isEmpty(dynamic.getStickTime())){
                                if(now.compareTo(dynamic.getStickTime())<1){
                                    stick=true;
                                }
                            }

                            if(!(hot&&stick)){
                                dynamic.setStick(Utils.getStickAndHot(hot,stick));
                                dynamicMapper.updateById(dynamic);
                            }

                        }
                    }


                }
        });
        CronUtil.schedule("01 00 * * *", new Task() {
            @Override
            public void execute() {
                //刷新发布次数、刷新次数、广告次数
                List<User> userList = userMapper.selectUser();
                if(userList.size()>0){
                    for (User user:userList){
                        if("0".equals(user.getVip())){
                            user.setReleaseNumber(1);
                        }else if("1".equals(user.getVip())){
                            user.setReleaseNumber(10);
                        }
                        user.setAdvertisingNumber(10);
                        user.setLimits(0);
                        userMapper.updateById(user);
                    }
                }

            }
        });
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }
}
