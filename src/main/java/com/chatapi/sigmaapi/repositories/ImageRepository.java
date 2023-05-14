package com.chatapi.sigmaapi
.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatapi.sigmaapi
.entity.Image;

public interface ImageRepository extends JpaRepository<Image, String> {
}
