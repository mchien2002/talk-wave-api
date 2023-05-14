package com.chatapi.sigmaapi
.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "member_of_group")
@IdClass(MemberOfGroup.class)
@Getter
@Setter
public class MemberOfGroup implements Serializable {
    @Id
    @Column(name = "group_id")
    private String groupId;

    @Id
    @Column(name = "user_id")
    private String userId;
}
