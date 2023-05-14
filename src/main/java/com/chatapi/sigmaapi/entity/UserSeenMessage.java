package com.chatapi.sigmaapi.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_seen_message")
@IdClass(UserSeenMessage.class)
@Getter
@Setter
public class UserSeenMessage implements Serializable {
    @Id
    @Column(name = "message_id")
    private String messageId;

    @Id
    @Column(name = "user_id")
    private String userId;
}
