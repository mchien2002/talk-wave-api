package com.chatapi.sigmaapi
.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatapi.sigmaapi
.entity.Audio;

public interface AudioRepository extends JpaRepository<Audio, String> {
    
}
