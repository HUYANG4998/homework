package com.wxtemplate.handler;

/**
 * Created by admin on 2019/7/10.
 */

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.jms.JMSException;


/**
 * 相当于controller的处理器
 */
@Component
public class MyHandler extends TextWebSocketHandler {

   // public static UserService userService;//用户service
    //会话链接

//    @JmsListener(destination = "${myqueque}")
//    public void receive(javax.jms.TextMessage textMessage) throws JMSException {
//        System.out.println("消费者收到消息" +textMessage.getText());
//
//    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //链接

    }
    //消息处理
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //消息处理
        session.sendMessage(new TextMessage("服务器返回收到的信息," ));
    }


}