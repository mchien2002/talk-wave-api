package com.chatapi.sigmaapi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.chatapi.sigmaapi.entity.GroupConversation;

public interface GroupRepository extends JpaRepository<GroupConversation, String> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO member_of_group (group_id, user_id) VALUE (:groupId, :user)", nativeQuery = true)
    void addMemberToGroup(@Param("groupId") String groupId, @Param("user") Object user);

    @Transactional
    @Query(value = "SELECT * FROM group_conversation WHERE group_id = :groupId", nativeQuery = true)
    GroupConversation findGroupById(@Param("groupId") String groupId);

    @Transactional
    @Query(value = "SELECT JSON_ARRAYAGG(JSON_OBJECT('groupId', gr_con.group_id, 'creatorUin', gr_con.creator_uin, 'name', gr_con.name, 'groupType', gr_con.group_type,'avatar' ,gr_con.avatar, 'lastMessage', (SELECT JSON_OBJECT('messageId', message_id, 'status', status, 'message', message, 'senderUin', sender_uin, 'createdAt', DATE_FORMAT(created_at, '%Y-%m-%d %H:%i:%s'), 'type', type, 'groupType', group_type, 'seenUins', (SELECT JSON_ARRAYAGG(usm.user_id) FROM user_seen_message usm WHERE usm.message_id = gr_con.last_message)) FROM messages WHERE message_id = gr_con.last_message), 'lastActiceTime', gr_con.last_active_time, 'groupStatus', gr_con.group_status, 'members', (SELECT JSON_ARRAYAGG(member.user_id) FROM member_of_group member WHERE member.group_id = gr_con.group_id))) FROM group_conversation gr_con JOIN member_of_group mem ON mem.user_id = :userId WHERE gr_con.group_id = mem.group_id;", nativeQuery = true)
    String getListGroupOfMember(@Param("userId") String userId);

    @Transactional
    @Query(value = "SELECT JSON_OBJECT('groupId', gr_con.group_id, 'name', gr_con.name, 'groupType', gr_con.group_type,'avatar' ,gr_con.avatar, 'createdAt', DATE_FORMAT(gr_con.created_at, '%Y-%m-%d %H:%i:%s'),'lastMessage', (SELECT JSON_OBJECT('messageId', message_id, 'status', status, 'message', message, 'senderUin', sender_uin, 'createdAt', DATE_FORMAT(created_at, '%Y-%m-%d %H:%i:%s'), 'type', type, 'groupType', group_type, 'seenUins', (SELECT JSON_ARRAYAGG(usm.user_id) FROM user_seen_message usm WHERE usm.message_id = gr_con.last_message)) FROM messages WHERE message_id = gr_con.last_message), 'lastActiceTime', gr_con.last_active_time, 'groupStatus', gr_con.group_status, 'members', (SELECT JSON_ARRAYAGG(member.user_id) FROM member_of_group member WHERE member.group_id = gr_con.group_id)) FROM group_conversation gr_con  WHERE gr_con.group_id = (SELECT group_id FROM member_of_group WHERE user_id IN (:userIds) GROUP BY group_id HAVING COUNT(DISTINCT user_id) = 2);", nativeQuery = true)
    String findGroupDetailByListMember(@Param("userIds") List<String> userIds);
        
}
