package com.chatapi.sigmaapi.websocket;

import org.springframework.web.socket.WebSocketSession;

import com.chatapi.sigmaapi.entity.model.SocketRequest;
import com.chatapi.sigmaapi.entity.model.SocketResponse;

public interface MySocketRequestHandler {
    void registerSessionUserHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void errorHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void createGroupHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void updateGroupHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void muteGroupHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void unmuteGroupHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void pinGroupHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void unpinGroupHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void leaveGroupHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void deleteGroupHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void listGroupHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void checkGroupHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void deleteGonversationHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void createMessageHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void updateMessageHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void listMessageHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void deleteMessageHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void reactionMessageHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void listReactionMessageHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);

    void seenMessageHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session);
}
