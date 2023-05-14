package com.chatapi.sigmaapi.entity;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.chatapi.sigmaapi.entity.enum_model.MessageStatus;
import com.chatapi.sigmaapi.entity.enum_model.MessageType;
import com.chatapi.sigmaapi.util.DateFormat;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;

import lombok.*;

@Entity
@Table(name = "messages")
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "message_id")
    private String messageId;

    @Column(name = "type")
    private int type;

    @Column(name = "status")
    private int status;

    @Column(name = "group_type")
    private int groupType;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "message")
    private String message;

    @Column(name = "created_at")
    // @JsonFormat(pattern = DateFormat.DATE_FORMAT_DEFAULT)
    private Date createdAt;

    @Column(name = "update_at")
    // @JsonFormat(pattern = DateFormat.DATE_FORMAT_DEFAULT)
    private Date updateAt;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "sender_uin")
    private String senderUin;

    @Column(name = "sender_avatar")
    private String senderAvatar;

    @Column(name = "nonce")
    private String nonce;

    @Transient
    private List<String> seenUins;

    @Transient
    private List<String> deletedUins;

    @Transient
    private Object attachment;

    public Message createFirstMessage(String senderUin, int groupType, Date createdAt) {
        this.createdAt = createdAt;
        this.type = MessageType.FIRST_MESSAGE.ordinal();
        this.message = "";
        this.groupType = groupType;
        this.senderUin = senderUin;
        this.status = MessageStatus.SENT.ordinal();
        this.status = MessageStatus.SENDING.ordinal();
        this.updateAt = DateFormat.getCurrentTime();
        return this;
    }

    public Message fromJson(String json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                String dateString = json.getAsString();
                if (dateString.isEmpty()) {
                    return null; // hoặc trả về ngày mặc định
                } else {
                    try {
                        return new SimpleDateFormat(DateFormat.DATE_FORMAT_DEFAULT).parse(dateString);
                    } catch (ParseException e) {
                        throw new JsonParseException("Failed to parse date " + dateString, e);
                    }
                }
            }
        })
                .create();
        Message message = gson.fromJson(json, Message.class);
        return message;
    }

    public void setAttachmentMedia(String jsonAttachment){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateFormat());
        Gson gson = gsonBuilder.create();
        if (this.type == MessageType.IMAGE.ordinal()) {
            this.attachment = gson.fromJson(jsonAttachment , Image.class);
        } else if (this.type == MessageType.VIDEO.ordinal()){
            this.attachment = gson.fromJson(jsonAttachment , Video.class);
        }
    }
}
