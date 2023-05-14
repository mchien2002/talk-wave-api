package com.chatapi.sigmaapi.service.message;

import java.util.List;

import com.chatapi.sigmaapi.entity.Message;

public interface MessageService {
    Message getMessageDetail(String messageID);

    void saveMessage(Message mess);

    List<Message> getListMessageByGroupID(String groupID);

    List<Message> getListMessageNotSeen(String groupId, String userId);

    void addUserSeenMessages(String userId, List<String> listMessageId);

    void addUserSeenMessage(String userId, String messageId);
    Message getMessageById(String messId);

}
 