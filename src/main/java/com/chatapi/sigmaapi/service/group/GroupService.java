package com.chatapi.sigmaapi.service.group;

import java.util.List;

import com.chatapi.sigmaapi.entity.GroupConversation;

public interface GroupService {
    void saveGroup(GroupConversation group);

    GroupConversation getGroupById(String id);

    List<GroupConversation> getListGroupOfMember(String userId);

    GroupConversation getPublicGroupWithMember(List<String> listMember);

    void createGroup(GroupConversation group);

}
