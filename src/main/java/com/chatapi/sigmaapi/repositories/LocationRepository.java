package com.chatapi.sigmaapi
.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatapi.sigmaapi
.entity.Location;

public interface LocationRepository extends JpaRepository<Location, String> {
}
