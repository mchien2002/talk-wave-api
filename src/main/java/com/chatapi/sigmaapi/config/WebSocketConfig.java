package com.chatapi.sigmaapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.chatapi.sigmaapi.constant.RouteConstant;
import com.chatapi.sigmaapi.service.group.GroupService;
import com.chatapi.sigmaapi.service.message.MessageService;
import com.chatapi.sigmaapi.service.message.media.MediaService;
import com.chatapi.sigmaapi.service.user.UserService;
import com.chatapi.sigmaapi.websocket.MySocketDataHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private MediaService mediaService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(myWebSocketHandler(), RouteConstant.WEB_SOCKET_CONNECTION)
                .setAllowedOrigins("*");
    }

    @Bean
    public MySocketDataHandler myWebSocketHandler() {
        return new MySocketDataHandler(messageService, groupService, userService, mediaService);
    }

}
