package com.chatapi.sigmaapi.service.message;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatapi.sigmaapi.entity.Message;
import com.chatapi.sigmaapi.entity.UserSeenMessage;
import com.chatapi.sigmaapi.repositories.MessageRepository;
import com.chatapi.sigmaapi.repositories.UsersSeenMessageRepository;
import com.chatapi.sigmaapi.util.DateFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UsersSeenMessageRepository usersSeenMessageRepository;

    @Override
    public Message getMessageDetail(String messageID) {
        return messageRepository.findById(messageID).get();
    }

    @Override
    public List<Message> getListMessageByGroupID(String groupID) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateFormat());
        gsonBuilder.registerTypeAdapterFactory(new TypeAdapterFactory() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                if (typeToken.getRawType() != Message.class) {
                    return null;
                }

                TypeAdapter<Message> delegate = (TypeAdapter<Message>) gson.getDelegateAdapter(this, typeToken);

                return (TypeAdapter<T>) new TypeAdapter<Message>() {
                    @Override
                    public void write(JsonWriter out, Message value) throws IOException {
                        delegate.write(out, value);
                    }

                    @Override
                    public Message read(JsonReader in) throws IOException {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(Date.class, new DateFormat());
                        Gson gson = gsonBuilder.create();
                        Message message = delegate.read(in);
                        message.setAttachmentMedia(gson.toJson(message.getAttachment()));
                        return message;
                    }
                };
            }
        });
        Gson gson = gsonBuilder.create();
        Type entityType = new TypeToken<List<Message>>() {
        }.getType();
        List<Message> listMessage = gson.fromJson(messageRepository.getListMessageOfGroup(groupID), entityType);
        return listMessage.stream()
                .sorted(Comparator.comparing(g -> ((Message) g).getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public void saveMessage(Message mess) {
        messageRepository.saveAndFlush(mess);
    }

    @Override
    public List<Message> getListMessageNotSeen(String groupId, String userId) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateFormat());
        Gson gson = gsonBuilder.create();
        Type entityType = new TypeToken<List<Message>>() {
        }.getType();
        List<Message> listMessageNotSeen = gson.fromJson(messageRepository.getListMessageNotSeenByUser(groupId, userId),
                entityType);
        return listMessageNotSeen;

    }

    @Override
    public void addUserSeenMessages(String userId, List<String> listMessageId) {
        usersSeenMessageRepository.addUserSeenMessage(userId, listMessageId);
        messageRepository.updateSeenMessageStatus(listMessageId);
    }

    @Override
    public void addUserSeenMessage(String userId, String messageId) {
        UserSeenMessage newRecord = new UserSeenMessage();
        newRecord.setMessageId(messageId);
        newRecord.setUserId(userId);
        usersSeenMessageRepository.save(newRecord);
    }

    @Override
    public Message getMessageById(String messId) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateFormat());
        Gson gson = gsonBuilder.create();
        String json = messageRepository.getMessageDetailById(messId);
        Message messageDetail = gson.fromJson(json, Message.class);
        messageDetail.setAttachmentMedia(gson.toJson(messageDetail.getAttachment()));
        return messageDetail;
    }

}
