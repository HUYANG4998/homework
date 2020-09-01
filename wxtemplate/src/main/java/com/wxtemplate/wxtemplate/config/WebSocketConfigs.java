package com.wxtemplate.wxtemplate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yeauty.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfigs {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
    
    /**
     * 配置service
     * @param roomService
     */

//    @Autowired
//    public void setMessageService(RoomServiceImpl roomService) {
//
//        MyWebSocket.roomService = roomService;
//    }
}
