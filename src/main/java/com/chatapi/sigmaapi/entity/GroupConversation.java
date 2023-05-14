
package com.chatapi.sigmaapi.entity;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.chatapi.sigmaapi.util.DateFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;

import lombok.*;

@Entity
@Table(name = "group_conversation")
@Getter
@Setter
public class GroupConversation {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "group_id")
    private String groupId;

    @Column(name = "name")
    private String name;

    @Column(name = "group_type", nullable = false)
    private int groupType;

    @Column(name = "theme")
    private int theme;

    @Column(name = "emoji_group")
    private String emojiGroup;

    @Column(name = "last_active_time")
    // @JsonFormat(pattern = DateFormat.DATE_FORMAT_DEFAULT)
    private Date lastActiceTime;

    @Column(name = "created_at")
    // @JsonFormat(pattern = DateFormat.DATE_FORMAT_DEFAULT)
    private Date createdAt;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "group_status")
    private int groupStatus;

    @Column(name = "owner_uin", nullable = false)
    private String ownerUin;

    @Column(name = "creator_uin", nullable = false)
    private String creatorUin;

    @Transient
    private List<String> members;

    @Transient
    private List<?> removedMember;

    @Transient
    private List<?> mediaFiles;

    @Transient
    private Message lastMessage;

    @JsonIgnore
    @Column(name = "last_message")
    private String lastMesssaegId;

    public GroupConversation fromJson(String json) {
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
        GroupConversation gr = gson.fromJson(json, GroupConversation.class);
        return gr;
    }
}
