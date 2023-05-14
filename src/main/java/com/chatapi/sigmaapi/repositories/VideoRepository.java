package com.chatapi.sigmaapi
.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatapi.sigmaapi
.entity.Video;

public interface VideoRepository extends JpaRepository<Video, String> {

}
