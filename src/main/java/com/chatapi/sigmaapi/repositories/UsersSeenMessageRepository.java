package com.chatapi.sigmaapi.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.chatapi.sigmaapi.entity.UserSeenMessage;

public interface UsersSeenMessageRepository extends JpaRepository<UserSeenMessage, String> {
    @Transactional
    @Query(value = "SELECT user_id FROM user_seen_message WHERE message_id = :messageId", nativeQuery = true)
    List<String> getUserSeenMessageByMessIdList(@Param("messageId") String groupId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO user_seen_message (user_id, message_id) SELECT :userId, m.message_id FROM messages m WHERE m.message_id IN :listMessage", nativeQuery = true)
    void addUserSeenMessage(@Param("userId") String userId, @Param("listMessage") List<String> listMessage);
}
