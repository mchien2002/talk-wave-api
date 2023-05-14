package com.chatapi.sigmaapi.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Entity
@Table(name = "audioes")
@Getter
@Setter
public class Audio {
    @Id
    @Column(name = "url")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String url;

    @Column(name = "local_url")
    private String localUrl;

    @Column(name = "duration")
    private int duration;
    
    @JsonIgnore
    @Column(name = "message_id")
    private String messageId;

    @Column(name = "type")
    private String type;

    @Lob
    @JsonIgnore
    @Column(name = "audio_data", nullable = false, length = 1000)
    private byte[] audioData;

}
