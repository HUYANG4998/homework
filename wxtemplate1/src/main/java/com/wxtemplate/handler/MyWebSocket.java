package com.wxtemplate.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlUtils;
import com.wxtemplate.api.mapper.ChatMapper;
import com.wxtemplate.api.util.JSONUtils;
import com.wxtemplate.api.util.Utils;
import com.wxtemplate.tools.Tool;
import com.wxtemplate.api.entity.*;
import com.wxtemplate.api.service.*;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import org.yeauty.annotation.*;
import org.yeauty.pojo.ParameterMap;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint (port = 6543 ,path = "/myHandler")
@Component
@Slf4j
public class MyWebSocket {
    //需要在webSocketConfigs 中配置
    //public static IRoomService roomService;
    //保存session
    public static ConcurrentHashMap<String, Session> userInfo = new ConcurrentHashMap<>();

    /**
     * 建立链接
     * @param session
     * @param headers
     * @param parameterMap
     * @throws IOException
     */
    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, ParameterMap parameterMap) throws IOException {
        String userid = parameterMap.getParameter("userid");
            if(session!=null&&session.isOpen()&&!StringUtils.isEmpty(userid)){
                userInfo.put(userid,session);//保存session

                SqlSession sqlsession=Utils.openSession();
                List<String> contentList=new ArrayList<>();
                String s=null;
                try {
                    ChatMapper chatMapper = sqlsession.getMapper(ChatMapper.class);
                    Map<String,Object> chatMap=chatMapper.selectUserAndService(userid);
                    if(chatMap!=null&&!chatMap.isEmpty()){
                            chatMapper.updateChatServiceContent(chatMap.get("chatid").toString());
                             s= Tool.WSResult(true, "链接成功！", chatMap,"0");
                    }else{
                        List<Chat> chatList=chatMapper.selectChatBySereviceIdAndContentIsNotNull(userid);
                        chatList.parallelStream().forEach((chat)->{
                            if(!StringUtils.isEmpty(chat.getUsercontent())){
                                contentList.add(chat.getUsercontent());
                                chatMapper.updateChatUserContent(chat.getChatid());
                            }
                        });
                        s= Tool.WSResult(true, "链接成功！", contentList,"0");
                    }
                    sqlsession.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(sqlsession!=null){
                        sqlsession.close();
                    }
                }

                if(session.isWritable()){
                    session.sendText(s);
                }
            }
            return;
    }

    @OnClose
    public void onClose(Session session) throws IOException {
       /*System.out.println("one connection closed");*/
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        session.close();
        /*System.out.println("onError");*/
        /*throwable.printStackTrace();*/
    }

    @OnMessage
    public void OnMessage(Session session, String message) {
        JSON parse = JSONUtil.parse(message);

        /**
         * 心跳
         * 参数：code:761  userid
         */
        Object code = parse.getByPath("code");
        if("761".equals(code.toString())){
            String userid = parse.getByPath("userid").toString();
            boolean b = userInfo.containsKey(userid);
            if(b &&  userInfo.get(userid).isWritable()){
                heartbeat(  userInfo.get(userid),userid);
            }else{
                userInfo.put(userid,session);
                heartbeat(  userInfo.get(userid),userid);
            }
            return;
        }
        Map<String,Object> map = (Map)parse.getByPath("data");
        Map<String,Object> mine=(Map)map.get("mine");
        Map<String,Object> to=(Map)map.get("to");
        Object time = parse.getByPath("time");
        mine.put("time",time.toString());
        String myuserid = mine.get("id").toString();
        String frienduserid=to.get("id").toString();
        /**
         * 聊天
         * 参数：code ：5 ；anotherid:聊天对方的id；userid：自己的id；content：内容
         */
        if("5".equals(code.toString())){
            /*String anotherid = parse.getByPath("anotherid").toString();
            String userid = parse.getByPath("userid").toString();
            String content = parse.getByPath("content").toString();*/
            //判断另一方是否在线
            if(userInfo.containsKey(frienduserid)&& userInfo.get(frienduserid).isWritable()){
                Session session1 = userInfo.get(frienduserid);//
                //发送的内容 根据需求填写
                HashMap<Object, Object> obj = new HashMap<>();
                obj.put("username", mine.get("username").toString()); //根据需求填写
                obj.put("avatar",mine.get("avatar").toString()); //根据需求填写
                obj.put("content",mine.get("content").toString());
                obj.put("id",myuserid);
                obj.put("status",parse.getByPath("status").toString());
                /*obj.put("type","friend");*/
                String s = Tool.WSResult(true, "链接成功！", obj,"1");
                if(session1.isOpen()&&session1.isActive()&&session1.isWritable()){

                    session1.sendText(s);
                }


            }else{
                //如果不再线-数据保存 数据库 自己处理
                SqlSession sqlsession=Utils.openSession();
                try {
                    ChatMapper chatMapper = sqlsession.getMapper(ChatMapper.class);
                    /**我是客服*/
                    Chat chat=null;
                    String sqlcontent=null;
                    chat=chatMapper.myIsServiceOrUser(myuserid,frienduserid);
                    JsonWebsocket json=new JsonWebsocket();
                    json.setAvatar(mine.get("avatar").toString());
                    json.setContent(mine.get("content").toString());
                    json.setId(mine.get("id").toString());
                    json.setUsername(mine.get("username").toString());
                    json.setTime(mine.get("time").toString());
                    json.setMine(mine.get("mine").toString());
                    json.setStatus(parse.getByPath("status").toString());
                    if(chat!=null){
                        /**客服*/
                        sqlcontent=chat.getServicecontent();
                        if(!StringUtils.isEmpty(sqlcontent)){
                            List<JsonWebsocket> list=JSONUtils.toBeanArray(sqlcontent,JsonWebsocket.class);
                            list.add(json);
                            chat.setServicecontent(com.alibaba.fastjson.JSON.toJSONString(list));
                        }else{
                            chat.setServicecontent("["+com.alibaba.fastjson.JSON.toJSONString(json)+"]");
                        }
                    }else{
                        chat=chatMapper.myIsServiceOrUser(frienduserid,myuserid);
                        if(chat!=null){
                            /**用户*/
                            sqlcontent=chat.getUsercontent();
                            List<JsonWebsocket> list=new ArrayList<>();
                            if(!StringUtils.isEmpty(sqlcontent)){
                                list=JSONUtils.toBeanArray(sqlcontent,JsonWebsocket.class);
                                list.add(json);
                                chat.setUsercontent(com.alibaba.fastjson.JSON.toJSONString(list));
                            }else{

                                chat.setUsercontent("["+com.alibaba.fastjson.JSON.toJSONString(json)+"]");
                            }
                        }
                    }
                    chatMapper.updateById(chat);
                    sqlsession.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(sqlsession!=null){
                        sqlsession.close();
                    }
                }
            }

            /**我是用户*/
            /*if(userInfo.containsKey(userid)&& userInfo.get(userid).isWritable()){
                Session session1 = userInfo.get(userid);
                HashMap<Object, Object> obj = new HashMap<>();
                obj.put("username","呵呵"); //根据需求填写
                obj.put("avatar","http://zhvsa18a6x.52http.tech/image/06be062f-3077-4150-b420-58f081f42151.jpg"); //根据需求填写
                obj.put("content",mine.get("content").toString());
                obj.put("id",userid);
                obj.put("type","friend");
                String s = Tool.WSResult(true, "链接成功！", obj,"1");
                session1.sendText(s);
            }*/
            return;
        }


    }


    /**
     * 心跳
     * @param session
     * @param userid
     */
    public void heartbeat(Session session,String userid ){
        Map<String ,String> map = new HashMap<>();
        map.put("content","active");
        String textMessage = Tool.WSResult(true, "active", map, "761");
        if(session.isWritable()){
            session.sendText(textMessage);
        }

    }




}