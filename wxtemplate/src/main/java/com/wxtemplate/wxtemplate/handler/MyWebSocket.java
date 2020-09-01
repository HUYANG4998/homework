package com.wxtemplate.wxtemplate.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.wxtemplate.api.entity.ChatRecord;
import com.wxtemplate.wxtemplate.api.entity.GroupChatRead;
import com.wxtemplate.wxtemplate.api.mapper.ChatRecordMapper;
import com.wxtemplate.wxtemplate.api.mapper.GroupChatReadMapper;
import com.wxtemplate.wxtemplate.api.mapper.GroupMemberMapper;
import com.wxtemplate.wxtemplate.api.util.Utils;
import com.wxtemplate.wxtemplate.tools.Tool;
import io.netty.handler.codec.http.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.yeauty.annotation.*;
import org.yeauty.pojo.ParameterMap;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(port = 6543, path = "/myHandler")
@Component
@Slf4j
public class MyWebSocket {
    //需要在webSocketConfigs 中配置
    //public static IRoomService roomService;
    //保存session
    public static ConcurrentHashMap<String, Session> userInfo = new ConcurrentHashMap<>();

    /**
     * 建立链接
     *
     * @param session
     * @param headers
     * @param parameterMap
     * @throws IOException
     */
    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, ParameterMap parameterMap) throws IOException {
        String userid = parameterMap.getParameter("userid");
        if (session != null && session.isOpen() && !StringUtils.isEmpty(userid)) {
            userInfo.put(userid, session);//保存session
            String s = Tool.WSResult(true, "链接成功！", null, "0");
            session.sendText(s);
        }
        return;
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("one connection closed");

        session.close();
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        session.close();
        boolean b = userInfo.containsValue(session);
        System.out.println("onError");
        throwable.printStackTrace();
    }

    @OnMessage
    public void OnMessage(Session session, String message) {
        JSON parse = JSONUtil.parse(message);

        /**
         * 心跳
         * 参数：code:761  userid
         */
        Object code = parse.getByPath("code");
        if ("761".equals(code.toString())) {
            String userid = parse.getByPath("userid").toString();
            boolean b = userInfo.containsKey(userid);
            if (b && userInfo.get(userid).isWritable()) {
                heartbeat(userInfo.get(userid), userid);
            } else {
                userInfo.put(userid, session);
                heartbeat(userInfo.get(userid), userid);
            }
            return;
        }
        Map<String, Object> map = (Map) parse.getByPath("data");
        Map<String, Object> mine = (Map) map.get("mine");
        Map<String, Object> to = (Map) map.get("to");

        String myuserid = mine.get("id").toString();//我方id
        String friEndUserid = to.get("id").toString();//对方id
        String content = mine.get("content").toString();
        String redNumber=null;
        String redMoney=null;
        if( mine.containsKey("redNumber")){
            redNumber= mine.get("redNumber").toString();
        }
        if(mine.containsKey("redMoney")){
            redMoney= mine.get("redMoney").toString();
        }

        String status = parse.getByPath("status").toString();//friends或group
        String type = parse.getByPath("type").toString();//0照片1文本
        /**
         * 聊天
         * 参数：code ：5 ；anotherid:聊天对方的id；userid：自己的id；content：内容
         */
        if ("5".equals(code.toString())) {
            /*String anotherid = parse.getByPath("anotherid").toString();
            String userid = parse.getByPath("userid").toString();
            String content = parse.getByPath("content").toString();*/
            SqlSession sqlsession = Utils.openSession();
            ChatRecordMapper chatRecordMapper = sqlsession.getMapper(ChatRecordMapper.class);
            ChatRecord chatRecord = new ChatRecord();

            try {
                if ("1".equals(status)) {
                    chatRecord.setStatus("1");
                    //判断另一方是否在线
                    if (userInfo.containsKey(friEndUserid) && userInfo.get(friEndUserid).isWritable()) {
                        Session session1 = userInfo.get(friEndUserid);//selectSubordinate
                        //发送的内容 根据需求填写
                        String s = socketToString(parse);
                        if (session1.isOpen() && session1.isActive() && session1.isWritable()) {
                            session1.sendText(s);
                        }
                    }
                } else if("0".equals(status)) {
                    chatRecord.setStatus("0");
                    GroupMemberMapper groupMemberMapper = sqlsession.getMapper(GroupMemberMapper.class);
                    List<Map<String, Object>> listMap = groupMemberMapper.selectGroupMemberByGroupChatId(friEndUserid, null);
                    sendAllMessage(listMap, myuserid, sqlsession, socketToString(parse));

                }

                //如果不再线-数据保存 数据库 自己处理
                chatRecord.setFromUserId(myuserid);
                chatRecord.setToId(friEndUserid);
                chatRecord.setContent(content);
                chatRecord.setType(type);
                chatRecord.setIsRead("0");
                chatRecord.setDelFlag("1");
                chatRecord.setBody(JSONUtils.toJSONString(parse));
                if(!StringUtils.isEmpty(redNumber)){
                    chatRecord.setRedNumber(Integer.valueOf(redNumber));
                }
               if(!StringUtils.isEmpty(redMoney)){
                   chatRecord.setRedMoney(new BigDecimal(redMoney));
               }
                chatRecord.setAddtime(DateUtil.now());
                chatRecordMapper.insert(chatRecord);

                sqlsession.commit();
            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } finally {
                if (sqlsession != null) {
                    sqlsession.close();
                }
            }


            return;
        }


    }

    private void sendAllMessage(List<Map<String, Object>> listMap, String userId, SqlSession sqlSession, String result) {

        GroupChatReadMapper groupChatReadMapper = sqlSession.getMapper(GroupChatReadMapper.class);
        for (Map<String, Object> map : listMap) {
            String memberUserId = (String) map.get("user_id");
            String groupChatId = (String) map.get("group_chat_id");
            if (!userId.equals(memberUserId)) {
                if (userInfo.containsKey(memberUserId) && userInfo.get(memberUserId).isWritable()) {
                    Session session = userInfo.get(memberUserId);
                    if (session.isOpen() && session.isActive() && session.isWritable()) {
                        session.sendText(result);
                    }
                }
                GroupChatRead groupChatRead = groupChatReadMapper.selectByUserIdAndGroupChatId(memberUserId, groupChatId);
                groupChatRead.setIsRead(0);
                groupChatReadMapper.updateById(groupChatRead);
            }
        }
    }

    private String socketToString(JSON parse) {
        /*Map<String, Object> map = (Map) parse.getByPath("data");*/
       /* Map<String, Object> mine = (Map) map.get("mine");
        Map<String, Object> to = (Map) map.get("to");
        Map<String, Object> obj = new HashMap<>();
        obj.put("nickname", mine.get("username").toString()); //我方用户名
        obj.put("to_id", to.get("username").toString()); //我方用户名
        obj.put("head_image", mine.get("avatar").toString()); //我方头像
        obj.put("content", mine.get("content").toString());//内容
        obj.put("from_user_id", mine.get("id").toString());//我的用户id
        obj.put("status", parse.getByPath("status").toString());//0照片1文本
        obj.put("type", parse.getByPath("type").toString());//聊天方式
        if( mine.containsKey("redNumber")){
            obj.put("redNumber",mine.get("redNumber").toString());
        }
        if(mine.containsKey("redMoney")){
            obj.put("redMoney",mine.get("redMoney").toString());
        }*/
        /*obj.put("type","friend");*/

        return Tool.WSResult(true, "链接成功！", parse, "1");
    }


    /**
     * 心跳
     *
     * @param session
     * @param userid
     */
    public void heartbeat(Session session, String userid) {
        Map<String, String> map = new HashMap<>();
        map.put("content", "active");
        String textMessage = Tool.WSResult(true, "active", map, "761");
        if (session.isWritable()) {
            session.sendText(textMessage);
        }

    }


}