package com.chatapi.sigmaapi
.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Entity
@Table(name = "images")
@Getter
@Setter
public class Image {
    @Id
    @Column(name = "url")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String url;

    @Column(name = "sub_index")
    private int subIndex;

    @Column(name = "width")
    private int width;

    @Column(name = "height")
    private int height;

    @Lob
    @JsonIgnore
    @Column(name = "image_data", length = 1000)
    private byte[] imageData;

    @Column(name = "type")
    private String type;
    
    @JsonIgnore
    @Column(name = "message_id")
    private String messageId;
}
