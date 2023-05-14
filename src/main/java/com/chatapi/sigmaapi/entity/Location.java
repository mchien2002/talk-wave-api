package com.chatapi.sigmaapi
.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.*;

@Table(name = "locations")
@Entity
@Getter
@Setter
public class Location {
    @Id
    @Column(name = "location_id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String locationId;

    @Column(name = "address")
    private String address;

    @Column(name = "coordinates")
    private String coordinates;
}
