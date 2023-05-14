package com.chatapi.sigmaapi.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.chatapi.sigmaapi.entity.Message;

public interface MessageRepository extends JpaRepository<Message, String> {
    @Transactional
    @Query(value = "SELECT JSON_ARRAYAGG(JSON_OBJECT('messageId', m.message_id, 'senderAvatar',m.sender_avatar, 'groupId', m.group_id, 'createdAt', DATE_FORMAT(m.created_at, '%Y-%m-%d %H:%i:%s'), 'groupType', m.group_type,'message' ,m.message, 'senderName', m.sender_name, 'status', m.status, 'type', m.type,'updateAt', DATE_FORMAT(m.update_at, '%Y-%m-%d %H:%i:%s'),'senderUin', m.sender_uin,'group_id', m.group_id, 'seenUins', (SELECT JSON_ARRAYAGG(usm.user_id) FROM user_seen_message usm WHERE usm.message_id = m.message_id), 'attachment', CASE WHEN m.type = 2 THEN (SELECT JSON_OBJECT('url', img.url, 'subIndex', img.sub_index, 'width', img.width, 'height', img.height, 'type', img.type) FROM images img WHERE img.message_id = m.message_id) WHEN m.type = 3 THEN (SELECT JSON_OBJECT('url', au.url, 'localUrl', au.local_url, 'duration', au.duration, 'type', au.type) FROM audioes au WHERE au.message_id = m.message_id) WHEN m.type = 4 THEN (SELECT JSON_OBJECT('url', vi.url, 'height', vi.height, 'duration', vi.duration, 'width', vi.width, 'type', vi.type, 'thumbnailUrl', vi.thumbnail_url) FROM videoes vi WHERE vi.message_id = m.message_id) ELSE NULL END)) FROM messages m WHERE m.group_id = :groupId", nativeQuery = true)
    String getListMessageOfGroup(@Param("groupId") String groupId);

    @Transactional
    @Query(value = "SELECT JSON_ARRAYAGG(JSON_OBJECT('messageId', m.message_id, 'senderAvatar',m.sender_avatar, 'groupId', m.group_id, 'createdAt', DATE_FORMAT(m.created_at, '%Y-%m-%d %H:%i:%s'), 'groupType', m.group_type,'message' ,m.message, 'senderName', m.sender_name, 'status', m.status, 'type', m.type,'updateAt', DATE_FORMAT(m.update_at, '%Y-%m-%d %H:%i:%s'),'senderUin', m.sender_uin,'group_id', m.group_id, 'seenUins', (SELECT JSON_ARRAYAGG(usm.user_id) FROM user_seen_message usm WHERE usm.message_id = m.message_id), 'attachment', CASE WHEN m.type = 2 THEN (SELECT JSON_OBJECT('url', img.url, 'subIndex', img.sub_index, 'width', img.width, 'height', img.height, 'type', img.type) FROM images img WHERE img.message_id = m.message_id) WHEN m.type = 3 THEN (SELECT JSON_OBJECT('url', au.url, 'localUrl', au.local_url, 'duration', au.duration, 'type', au.type) FROM audioes au WHERE au.message_id = m.message_id) WHEN m.type = 4 THEN (SELECT JSON_OBJECT('url', vi.url, 'height', vi.height, 'duration', vi.duration, 'width', vi.width, 'type', vi.type, 'thumbnailUrl', vi.thumbnail_url) FROM videoes vi WHERE vi.message_id = m.message_id) ELSE NULL END)) FROM messages m WHERE m.group_id = :groupId AND m.message_id NOT IN (SELECT usm.message_id FROM user_seen_message usm WHERE usm.user_id = :userId);", nativeQuery = true)
    String getListMessageNotSeenByUser(@Param("groupId") String groupId, @Param("userId") String userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE messages m SET m.status = 3 WHERE m.message_id IN :messageIds", nativeQuery = true)
    void updateSeenMessageStatus(@Param("messageIds") List<String> messageIds);

    @Transactional
    @Query(value = "SELECT JSON_OBJECT('messageId', m.message_id, 'senderAvatar',m.sender_avatar, 'groupId', m.group_id, 'createdAt', DATE_FORMAT(m.created_at, '%Y-%m-%d %H:%i:%s'), 'groupType', m.group_type,'message' ,m.message, 'senderName', m.sender_name, 'status', m.status, 'type', m.type,'updateAt', DATE_FORMAT(m.update_at, '%Y-%m-%d %H:%i:%s'),'senderUin', m.sender_uin,'group_id', m.group_id, 'seenUins', (SELECT JSON_ARRAYAGG(usm.user_id) FROM user_seen_message usm WHERE usm.message_id = m.message_id), 'attachment', CASE WHEN m.type = 2 THEN (SELECT JSON_OBJECT('url', img.url, 'subIndex', img.sub_index, 'width', img.width, 'height', img.height, 'type', img.type) FROM images img WHERE img.message_id = m.message_id) WHEN m.type = 3 THEN (SELECT JSON_OBJECT('url', au.url, 'localUrl', au.local_url, 'duration', au.duration, 'type', au.type) FROM audioes au WHERE au.message_id = m.message_id) WHEN m.type = 4 THEN (SELECT JSON_OBJECT('url', vi.url, 'height', vi.height, 'duration', vi.duration, 'width', vi.width, 'type', vi.type, 'thumbnailUrl', vi.thumbnail_url) FROM videoes vi WHERE vi.message_id = m.message_id) ELSE NULL END) FROM messages m WHERE m.message_id = :messId", nativeQuery = true)    
    String getMessageDetailById(@Param("messId") String messId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE messages m SET m.sender_avatar = :avatar WHERE m.sender_uin = :senderUin", nativeQuery = true)
    void setAvatarSender(@Param("avatar") String avatar, @Param("senderUin") String senderUin);
}
