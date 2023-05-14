package com.chatapi.sigmaapi.websocket;

import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import java.util.*;
import com.chatapi.sigmaapi.constant.SocketRequestType;
import com.chatapi.sigmaapi.entity.GroupConversation;
import com.chatapi.sigmaapi.entity.Message;
import com.chatapi.sigmaapi.entity.User;
import com.chatapi.sigmaapi.util.FileUtils;
import com.chatapi.sigmaapi.entity.model.SocketRequest;
import com.chatapi.sigmaapi.entity.model.SocketResponse;
import com.chatapi.sigmaapi.service.group.GroupService;
import com.chatapi.sigmaapi.service.message.MessageService;
import com.chatapi.sigmaapi.service.message.media.MediaService;
import com.chatapi.sigmaapi.service.user.UserService;
import com.chatapi.sigmaapi.util.DateFormat;
import com.chatapi.sigmaapi.util.EntityUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.stream.Collectors;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.chatapi.sigmaapi.entity.enum_model.MessageType;
import com.chatapi.sigmaapi.entity.enum_model.MessageStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MySocketDataHandler implements WebSocketHandler {
    private Map<String, WebSocketSession> listSessionsConnected = new HashMap<>();
    private Map<String, StringBuilder> listMessaeRequest = new HashMap<>();
    private Gson gson;

    private UserService userService;    
    private MessageService messageService;
    private GroupService groupService;
    private MediaService mediaService;

    public MySocketDataHandler(MessageService messageService,
            GroupService groupService, UserService userService, MediaService mediaService) {
        this.messageService = messageService;
        this.groupService = groupService;
        this.userService = userService;
        this.mediaService = mediaService;
        gson = new GsonBuilder()
                .setDateFormat(DateFormat.DATE_FORMAT_DEFAULT)
                .create();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String userOfSessionId = EntityUtils.getKeyByValue(listSessionsConnected, session);
        userService.setUserOffline(userOfSessionId);
        listSessionsConnected.remove(userOfSessionId);
        log.info("Socket closed from " + session.getId());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connected to " + session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        try {
            log.info("Size's message: " + message.getPayloadLength());
            // Xử lý message
            if (listMessaeRequest.containsKey(session.getId())) {
                if (session.isOpen() && message.getPayloadLength() == 8192) {
                    listMessaeRequest.get(session.getId()).append(message.getPayload());
                    return;
                } else {
                    listMessaeRequest.get(session.getId()).append(message.getPayload());
                }
            } else {
                if (session.isOpen() && message.getPayloadLength() == 8192) {
                    listMessaeRequest.put(session.getId(), new StringBuilder());
                    listMessaeRequest.get(session.getId()).append(message.getPayload());
                    return;
                } else {
                    listMessaeRequest.put(session.getId(), new StringBuilder());
                    listMessaeRequest.get(session.getId()).append(message.getPayload());
                }
            }
            String fullMessage = listMessaeRequest.get(session.getId()).toString();
            // Tiếp tục xử lý fullMessage
            SocketResponse sResponse = gson.fromJson(fullMessage, SocketResponse.class);
            log.info("Message: " + sResponse.getEvent());
            SocketRequest socketRequest = new SocketRequest();
            switch (sResponse.getEvent()) {
                case SocketRequestType.SOCKET_REQUEST_CREATE_MESSAGE: {
                    mySocketDataHandler.createMessageHandler(sResponse, socketRequest, session);
                    break;
                }
                case SocketRequestType.REGISTER_SOCKET_SESSION_USER: {
                    mySocketDataHandler.registerSessionUserHandler(sResponse, socketRequest, session);
                    break;
                }
                case SocketRequestType.SOCKET_REQUEST_CREATE_GROUP: {
                    mySocketDataHandler.createGroupHandler(sResponse, socketRequest, session);
                    break;
                }
                case SocketRequestType.SOCKET_REQUEST_LIST_GROUP: {
                    mySocketDataHandler.listGroupHandler(sResponse, socketRequest, session);
                    break;
                }
                case SocketRequestType.SOCKET_REQUEST_LIST_MESSAGE: {
                    mySocketDataHandler.listMessageHandler(sResponse, socketRequest, session);
                    break;
                }
                case SocketRequestType.SOCKET_REQUEST_SEEN_LASTEST_MESSAGE: {
                    mySocketDataHandler.seenMessageHandler(sResponse, socketRequest, session);
                    break;
                }
                default:
                    break;
            }
            listMessaeRequest.remove(session.getId());

        } catch (Exception e) {
            listMessaeRequest.remove(session.getId());
            e.printStackTrace();
            log.error(e.toString(), e);
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Error: " + exception.toString() + " from " + session.getId(), exception);
    }

    @Override
    public boolean supportsPartialMessages() {
        return true;
    }

    private void sendMessage(String userId, SocketRequest message) {
        WebSocketSession session = listSessionsConnected.get(userId);
        try {
            session.sendMessage(new TextMessage(gson.toJson(message)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<GroupConversation> getListGroupOfUserId(String userId) {
        List<GroupConversation> listGroupOfMember = groupService.getListGroupOfMember(userId);
        return listGroupOfMember;

    }

    private MySocketRequestHandler mySocketDataHandler = new MySocketRequestHandler() {

        @Override
        public void registerSessionUserHandler(SocketResponse sResponse, SocketRequest socketRequest,
                WebSocketSession session) {
            User userOfSession = gson.fromJson(gson.toJson(sResponse.getPayload().get("userProfile")),
                    User.class);
            userService.setUserOnline(userOfSession.getUserId());
            listSessionsConnected.put(userOfSession.getUserId(), session);
            log.info("Registered socket user by ID: " + userOfSession.getUserId());
        }

        @Override
        public void errorHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'errorHandler'");
        }

        @Override
        public void createGroupHandler(SocketResponse sResponse, SocketRequest socketRequest,
                WebSocketSession session) {
            GroupConversation groupSocket = new GroupConversation()
                    .fromJson(gson.toJson(sResponse.getPayload().get("group")));
            groupService.createGroup(groupSocket);
            for (String member : groupSocket.getMembers()) {
                Thread myThread = new Thread(() -> {
                    Map<String, List<GroupConversation>> listGroupUpdate = new HashMap<String, List<GroupConversation>>();
                    listGroupUpdate.put("listGroup", getListGroupOfUserId(member));
                    socketRequest.setEvent(SocketRequestType.SOCKET_REQUEST_LIST_GROUP);
                    socketRequest.setPayload(listGroupUpdate);
                    sendMessage(member, socketRequest);
                });
                myThread.start();
            }
            SocketRequest successCreateGroup = new SocketRequest();
            Map<String, Object> map = new HashMap<String, Object>();
            successCreateGroup.setEvent(SocketRequestType.SOCKET_REQUEST_CREATED_GROUP_SCCUSSEFUL);
            map.put("group", groupSocket);
            map.put("listMessage", messageService.getListMessageByGroupID(groupSocket.getGroupId()));
            successCreateGroup.setPayload(map);
            try {
                session.sendMessage(new TextMessage(gson.toJson(successCreateGroup)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void updateGroupHandler(SocketResponse sResponse, SocketRequest socketRequest,
                WebSocketSession session) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'updateGroupHandler'");
        }

        @Override
        public void muteGroupHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'muteGroupHandler'");
        }

        @Override
        public void unmuteGroupHandler(SocketResponse sResponse, SocketRequest socketRequest,
                WebSocketSession session) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'unmuteGroupHandler'");
        }

        @Override
        public void pinGroupHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'pinGroupHandler'");
        }

        @Override
        public void unpinGroupHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'unpinGroupHandler'");
        }

        @Override
        public void leaveGroupHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'leaveGroupHandler'");
        }

        @Override
        public void deleteGroupHandler(SocketResponse sResponse, SocketRequest socketRequest,
                WebSocketSession session) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'deleteGroupHandler'");
        }

        @Override
        public void listGroupHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session) {
            String userId = (String) EntityUtils.getEntityBySocketResponse(sResponse, "userId");
            socketRequest.setEvent(SocketRequestType.SOCKET_REQUEST_LIST_GROUP);
            Map<String, List<GroupConversation>> listGroupUpdate = new HashMap<String, List<GroupConversation>>();
            listGroupUpdate.put("listGroup", getListGroupOfUserId(userId));
            socketRequest.setPayload(listGroupUpdate);
            sendMessage(userId, socketRequest);
        }

        @Override
        public void checkGroupHandler(SocketResponse sResponse, SocketRequest socketRequest, WebSocketSession session) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'checkGroupHandler'");
        }

        @Override
        public void deleteGonversationHandler(SocketResponse sResponse, SocketRequest socketRequest,
                WebSocketSession session) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'deleteGonversationHandler'");
        }

        @Override
        public void createMessageHandler(SocketResponse sResponse, SocketRequest socketRequest,
                WebSocketSession session) {
            // CREATE MESSAGE
            Message messageSocket = new Message().fromJson(gson.toJson(sResponse.getPayload().get("message")));
            messageSocket.setStatus(MessageStatus.SENT.ordinal());
            messageService.saveMessage(messageSocket);
            switch (MessageType.values()[messageSocket.getType()]) {
                case IMAGE: {
                    try {
                        mediaService.saveImage(
                                FileUtils.convertToMultipartFile(
                                        (String) sResponse.getPayload().get("attachment")),
                                messageSocket.getMessageId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case AUDIO: {
                    try {
                        mediaService.saveAudio(
                                FileUtils.convertToMultipartFile(
                                        (String) sResponse.getPayload().get("attachment")),
                                messageSocket.getMessageId());
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case VIDEO: {
                    try {
                        mediaService.saveVideo(
                                FileUtils.videoMultipartFile(
                                        (String) sResponse.getPayload().get("attachment"), messageSocket.getMessageId()),
                                messageSocket.getMessageId());
                    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                default: {
                    break;
                }

            }
            // SET SEEN MESSAGE
            messageService.addUserSeenMessage(messageSocket.getSenderUin(), messageSocket.getMessageId());
            // SET LASTMESSAGE IN GROUP
            GroupConversation group = groupService.getGroupById(messageSocket.getGroupId());
            group.setLastMesssaegId(messageSocket.getMessageId());
            groupService.saveGroup(group);
            Message newMessage;
            if (messageSocket.getType() == MessageType.TEXT.ordinal()) {
                newMessage = messageSocket;
            } else {
                newMessage = messageService.getMessageById(messageSocket.getMessageId());
            }
            // SEEN TO EACH OF MEMBER IN GROUP
            for (String member : group.getMembers()) {
                Thread myThread = new Thread(() -> {
                    Map<String, Object> payLoad = new HashMap<String, Object>();
                    payLoad.put("listGroup", getListGroupOfUserId(member));
                    payLoad.put("newMessage", newMessage);
                    socketRequest.setEvent(SocketRequestType.SOCKET_REQUEST_CREATE_MESSAGE);
                    socketRequest.setPayload(payLoad);
                    sendMessage(member, socketRequest);
                });
                myThread.start();
            }

        }

        @Override
        public void updateMessageHandler(SocketResponse sResponse, SocketRequest socketRequest,
                WebSocketSession session) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'updateMessageHandler'");
        }

        @Override
        public void listMessageHandler(SocketResponse sResponse, SocketRequest socketRequest,
                WebSocketSession session) {
            String groupId = (String) EntityUtils.getEntityBySocketResponse(sResponse, "groupId");
            socketRequest.setEvent(SocketRequestType.SOCKET_REQUEST_LIST_MESSAGE);
            Map<String, List<Message>> listMessage = new HashMap<String, List<Message>>();
            listMessage.put("listMessage", messageService.getListMessageByGroupID(groupId));
            socketRequest.setPayload(listMessage);
            try {
                session.sendMessage(new TextMessage(gson.toJson(socketRequest)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void deleteMessageHandler(SocketResponse sResponse, SocketRequest socketRequest,
                WebSocketSession session) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'deleteMessageHandler'");
        }

        @Override
        public void reactionMessageHandler(SocketResponse sResponse, SocketRequest socketRequest,
                WebSocketSession session) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'reactionMessageHandler'");
        }

        @Override
        public void listReactionMessageHandler(SocketResponse sResponse, SocketRequest socketRequest,
                WebSocketSession session) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'listReactionMessageHandler'");
        }

        @Override
        public void seenMessageHandler(SocketResponse sResponse, SocketRequest socketRequest,
                WebSocketSession session) {
            String messageId = (String) EntityUtils.getEntityBySocketResponse(sResponse, "messageId");
            Message messageSeen = messageService.getMessageDetail(messageId);
            GroupConversation group = groupService.getGroupById(messageSeen.getGroupId());
            List<Message> listMessaeNotSeen = messageService.getListMessageNotSeen(group.getGroupId(),
                    EntityUtils.getKeyByValue(listSessionsConnected, session));
            messageService.addUserSeenMessages(EntityUtils.getKeyByValue(listSessionsConnected, session),
                    listMessaeNotSeen.stream()
                            .map(Message::getMessageId)
                            .collect(Collectors.toList()));
            for (Message item : listMessaeNotSeen) {
                // ĐỔI STATUS CỦA MESSAGE
                item.setStatus(MessageStatus.SEEN.ordinal());
                // THÊM USER CỦA SECSSION TRÊN ĐÃ XEM TIN NHẮN
                List<String> newSeenUins = item.getSeenUins();
                if (newSeenUins == null) {
                    newSeenUins = new ArrayList<String>();
                }
                newSeenUins.add(EntityUtils.getKeyByValue(listSessionsConnected, session));
            }
            for (String member : group.getMembers()) {
                Thread myThread = new Thread(() -> {
                    Map<String, Object> payLoad = new HashMap<String, Object>();
                    payLoad.put("seenMessages", listMessaeNotSeen);
                    payLoad.put("listGroup", getListGroupOfUserId(member));
                    socketRequest.setEvent(SocketRequestType.SOCKET_REQUEST_SEEN_LASTEST_MESSAGE);
                    socketRequest.setPayload(payLoad);
                    sendMessage(member, socketRequest);
                });
                myThread.start();
            }

        }

    };

}
