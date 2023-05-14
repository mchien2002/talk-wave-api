package com.chatapi.sigmaapi
.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatapi.sigmaapi
.entity.UserOnlineStatus;

public interface UserOnlineStatusRepository extends JpaRepository<UserOnlineStatus, String> {

}
