package com.chatapi.sigmaapi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.chatapi.sigmaapi.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    User findByPhone(String phone);

    User findByEmail(String email);

    @Transactional
    @Query(value = "SELECT JSON_ARRAYAGG(JSON_OBJECT('userId', u.user_id, 'userName', u.user_name, 'fullName', u.full_name, 'phone', u.phone, 'avatar', u.avatar, 'localName', u.local_name)) FROM users u WHERE u.email LIKE :value OR u.phone = :value OR u.full_name LIKE :value OR u.local_name LIKE :value OR u.user_name like :value", nativeQuery = true)
    String findUserByKey(String value);

    @Transactional
    @Query(value = "SELECT * FROM users u WHERE u.user_id IN (SELECT DISTINCT user_id FROM member_of_group WHERE group_id IN (SELECT group_id FROM member_of_group WHERE user_id = :userId ) AND user_id != :userId);", nativeQuery = true)
    List<User> findUserContactInGroup(@Param("userId") String userId);

}
