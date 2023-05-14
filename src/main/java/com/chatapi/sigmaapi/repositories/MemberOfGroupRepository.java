package com.chatapi.sigmaapi.repositories;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.chatapi.sigmaapi.entity.MemberOfGroup;

public interface MemberOfGroupRepository extends JpaRepository<MemberOfGroup, String> {
    @Transactional
    @Query(value = "SELECT user_id FROM member_of_group WHERE group_id = :groupId", nativeQuery = true)
    List<String> getMemberByGroupId(@Param("groupId") String groupId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO member_of_group (group_id, user_id) SELECT :groupId, u.user_id FROM users u WHERE u.user_id IN :userIds", nativeQuery = true)
    void addMemberToGroup(@Param("groupId") String groupId, @Param("userIds") List<String> userIds);

} 