package com.jeeplus.modules.shop.api.chat.service.impl;

import cn.hutool.core.date.DateUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.shop.api.chat.entity.Chat;
import com.jeeplus.modules.shop.api.chat.mapper.ChatMapper;
import com.jeeplus.modules.shop.api.chat.service.ChatService;
import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.customer.service.CustomerService;
import com.jeeplus.modules.shop.rider.entity.Rider;
import com.jeeplus.modules.shop.rider.service.RiderService;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService {
    @Resource
    private ChatMapper chatMapper;
    @Resource
    private CustomerService customerService;
    @Resource
    private StoreService storeService;
    @Resource
    private RiderService riderService;

    @Override
    public List<Map<String, Object>> selectByUseridAndStatus(String userid, String status) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        List<Map<String,Object>> resultMap=new ArrayList<>();
        if(!StringUtils.isEmpty(userid)&&!StringUtils.isEmpty(status)){
            listMap=chatMapper.selectByUseridAndStatus(userid,status);
            List<String> userList=new ArrayList<>();
            for (Map<String,Object> map:listMap){
                String from=map.get("form").toString();
                String to=map.get("goto").toString();
                if(userid.equals(from)){
                    if(!userList.contains(to)){
                        userList.add(to);
                    }
                }else if(userid.equals(to)){
                    if(!userList.contains(from)){
                        userList.add(from);
                    }
                }
            }

            for (String id:userList){
                Map<String,Object> result=new HashMap<>();
                if("0".equals(status)){
                    /**查询对方用户的最新消息*/
                    result=chatMapper.selectByFormAndGotoCostomer(id,userid);
                }else if("1".equals(status)){
                    /**查询对方商家的最新消息消息*/
                    result=chatMapper.selectByFormAndGotoStore(id,userid);
                }else if("2".equals(status)){
                    /**查询对方骑手的最新消息消息*/
                    result=chatMapper.selectByFormAndGotoRider(id,userid);
                }
                if(result.containsKey("addtime")){
                    String addtime=(String)result.get("addtime");
                    if(addtime.contains(DateUtil.today())){
                        addtime=DateUtil.format(DateUtil.parse(addtime),"HH:mm");
                        result.put("addtime",addtime);
                    }
                }
                if(result!=null&&!result.isEmpty()){
                    if(result.containsKey("delFlag")){
                        String delFlag=result.get("delFlag").toString();
                        if("1".equals(delFlag)){
                            result.put("number",chatMapper.selectByFormAndGotoCount(id,userid));
                            resultMap.add(result);
                        }
                    }

                }
            }
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> selectMessageByFormAndTo(Map<String,Object> map) {
        Map<String,Object> result=new HashMap<>();
        if(map!=null&&!map.isEmpty()){

            String wostatus=map.get("wostatus")==null?null:(String)map.get("wostatus");
            String nistatus=map.get("nistatus")==null?null:(String)map.get("nistatus");
            String userid=map.get("userid")==null?null:(String)map.get("userid");
            String oppositeid=map.get("oppositeid")==null?null:(String)map.get("oppositeid");
            Integer number=map.get("number")==null?null:(Integer)map.get("number");
            List<Map<String,Object>> listMap=chatMapper.selectMessageByFormAndTo(userid,oppositeid);

            if(number>0&&number!=null){
                int index=0;
                for (Map<String,Object> list:listMap){
                    if(index>=number){
                        break;
                    }
                    String chatid=(String)list.get("chatid");
                    Chat chat=new Chat();
                    chat.setChatid(chatid);
                    chat.setIsRead("1");
                    chat.setDelFlag("1");
                    chatMapper.updateByChatId(chat);
                    index++;
                }
            }

            String woname=null;
            String niname=null;
            String woimg=null;
            String niimg=null;
            if("0".equals(wostatus)&&"1".equals(nistatus)){
                Customer customer=customerService.get(userid);
                woname=customer.getName();
                woimg=customer.getImg();
                Store store=storeService.get(oppositeid);
                niname=store.getName();
                niimg=store.getImg();
            }else if("0".equals(wostatus)&&"2".equals(nistatus)){
                Customer customer=customerService.get(userid);
                woname=customer.getName();
                woimg=customer.getImg();
                Rider rider=riderService.get(oppositeid);
                niname=rider.getName();
                niimg=rider.getImg();
            }else if("1".equals(wostatus)&&"0".equals(nistatus)){

                Store store=storeService.get(userid);
                woname=store.getName();
                woimg=store.getImg();
                Customer customer=customerService.get(oppositeid);

                niname=customer.getName();
                niimg=customer.getImg();
            }else if("1".equals(wostatus)&&"2".equals(nistatus)){
                Store store=storeService.get(userid);
                woname=store.getName();
                woimg=store.getImg();
                Rider rider=riderService.get(oppositeid);
                niname=rider.getName();
                niimg=rider.getImg();
            }else if("2".equals(wostatus)&&"0".equals(nistatus)){
                Rider rider=riderService.get(userid);
                woname=rider.getName();
                woimg=rider.getImg();
                Customer customer=customerService.get(oppositeid);
                niname=customer.getName();
                niimg=customer.getImg();

            }else if("2".equals(wostatus)&&"1".equals(nistatus)){
                Rider rider=riderService.get(userid);
                woname=rider.getName();
                woimg=rider.getImg();
                Store store=storeService.get(oppositeid);
                niname=store.getName();
                niimg=store.getImg();
            }
            result.put("list",listMap);
            result.put("woname",woname);
            result.put("woimg",woimg);
            result.put("niname",niname);
            result.put("niimg",niimg);
        }
        return result;
    }

    @Override
    public void updateByChatId(Chat chat) {
        if(chat!=null){
            chat.setDelFlag("0");
            chatMapper.updateByChatId(chat);
        }
    }

    @Override
    public Integer selectMessageNumber(String userid) {
        Integer number=null;
        if(!StringUtils.isEmpty(userid)){
            number=chatMapper.selectMessageNumber(userid);
        }
        return number;
    }

    @Override
    public Chat selectMessageById(String chatid) {
        Chat chat=null;
        if(!StringUtils.isEmpty(chatid)){
            chat=chatMapper.selectMessageById(chatid);
        }
        return chat;
    }

    @Override
    public void insert(Chat chat) {
        if(chat!=null){
            chatMapper.insert(chat);
        }
    }
}
