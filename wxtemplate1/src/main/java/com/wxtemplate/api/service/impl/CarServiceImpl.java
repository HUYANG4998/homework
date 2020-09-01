package com.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Notice;
import com.wxtemplate.api.entity.Orders;
import com.wxtemplate.api.entity.VO.CarVo;
import com.wxtemplate.api.entity.Car;
import com.wxtemplate.api.mapper.CarMapper;
import com.wxtemplate.api.mapper.NoticeMapper;
import com.wxtemplate.api.mapper.OrdersMapper;
import com.wxtemplate.api.service.ICarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 个人车 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements ICarService {

    @Resource
    private CarMapper carMapper;
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private NoticeMapper noticeMapper;
    @Override
    public List<Map<String,Object>> selectMyCar(Map<String,Object> result) {
        List<Map<String,Object>> carList=new ArrayList<>();
        String userid=result.get("userid")==null?null:result.get("userid").toString();
        if(!StringUtils.isEmpty(userid)){

            if(result.containsKey("orderid")){
                String orderid=result.get("orderid")==null?null:result.get("orderid").toString();
                if(!StringUtils.isEmpty(orderid)){
                    Orders order=ordersMapper.selectById(orderid);
                    String marrytime=order.getMarrytime();
                    String hour=order.getHour();
                    String endtime= DateUtil.format(DateUtil.offsetHour(DateUtil.parse(marrytime, "yyyy-MM-dd HH:mm:ss"), Integer.valueOf(hour)),"yyyy-MM-dd HH:mm:ss");
                   /* if(result.containsKey("caridList")){
                        List<String> caridList=result.get("caridList")==null?null:(List<String>)result.get("caridList");
                        if(caridList!=null&&caridList.size()>0){
                            map.put("caridList",caridList);
                        }
                    }*/
                    result.put("starttime",marrytime);
                    result.put("endtime",endtime);
                }
            }

            carList=carMapper.selectMyCarByUserId(result);
        }
        return carList;
    }

    @Override
    public List<Map<String, Object>> selectAllMyCar(String carnumber) {
        List<Map<String,Object>> listMap=carMapper.selectAllMyCar(carnumber);
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
    public Map<String, Object> selectMyCarByCarid(String carid) {
        Map<String,Object> carMap=new HashMap<>();
        if(!StringUtils.isEmpty(carid)){
            carMap=carMapper.selectMyCarByCarid(carid);
            if(carMap.containsKey("imgurl")){
                String imgurl= carMap.get("imgurl")==null?null:carMap.get("imgurl").toString();
                if(!StringUtils.isEmpty(imgurl)){
                    List<String> imgurlList = Arrays.asList(imgurl.split(";"));
                    if(imgurlList!=null){
                        carMap.put("imgurl",imgurlList);
                    }
                }
            }
        }
        return carMap;
    }

    @Override
    public void carAudit(Map<String, Object> map) {
        if(!map.isEmpty()){
           String status=map.get("status") == null?null:map.get("status").toString();
           String carid=map.get("carid") == null?null:map.get("carid").toString();
           if(!StringUtils.isEmpty(status)){
               Car car = carMapper.selectById(carid);
               if("2".equals(status)){
                   String cause=map.get("cause") == null?null:map.get("cause").toString();
                   car.setCause(cause);
                   Notice notice=new Notice();
                   notice.setAddition("车辆被驳回");
                   if(StringUtils.isEmpty(cause)){
                       cause="无";
                   }
                   notice.setContent("您提交的车辆已被驳回！原因："+cause);
                   notice.setIsread("0");
                   notice.setAddtime(DateUtil.now());
                   notice.setUserid(car.getUserid());
                   noticeMapper.insert(notice);
               }
               car.setCarid(carid);
               car.setStatus(status);
               carMapper.updateById(car);
           }
        }
    }
}
