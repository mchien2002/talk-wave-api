package com.chatapi.sigmaapi.service.group;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatapi.sigmaapi.entity.GroupConversation;
import com.chatapi.sigmaapi.entity.Message;
import com.chatapi.sigmaapi.repositories.GroupRepository;
import com.chatapi.sigmaapi.repositories.MemberOfGroupRepository;
import com.chatapi.sigmaapi.repositories.MessageRepository;
import com.chatapi.sigmaapi.util.DateFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

@Service
public class GroupServiceIml implements GroupService {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MemberOfGroupRepository memberOfGroupRepository;
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public void saveGroup(GroupConversation group) {
        groupRepository.save(group);
    }

    @Override
    public GroupConversation getGroupById(String id) {
        GroupConversation group = groupRepository.findById(id).get();
        group.setMembers(memberOfGroupRepository.getMemberByGroupId(group.getGroupId()));
        group.setLastMessage(messageRepository.findById(group.getLastMesssaegId()).get());
        return group;
    }

    @Override
    public List<GroupConversation> getListGroupOfMember(String userId) {
        var listStr = groupRepository.getListGroupOfMember(userId);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateFormat());
        Gson gson = gsonBuilder.create();
        Type entityType = new TypeToken<List<GroupConversation>>() {
        }.getType();

        List<GroupConversation> listGroupOfMember;
        listGroupOfMember = gson.fromJson(listStr, entityType);
        return listGroupOfMember.stream()
                .sorted(Comparator.comparing(g -> ((GroupConversation) g).getLastMessage().getCreatedAt())
                        .reversed())
                .collect(Collectors.toList());
    }

    @Override
    public GroupConversation getPublicGroupWithMember(List<String> listMember) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateFormat());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(groupRepository.findGroupDetailByListMember(listMember), GroupConversation.class);
    }

    @Override
    public void createGroup(GroupConversation group) {
        Message firstMessage = new Message().createFirstMessage(group.getCreatorUin(), group.getGroupType(), group.getCreatedAt());
        messageRepository.saveAndFlush(firstMessage);
        group.setGroupStatus(0);
        group.setTheme(0);
        group.setLastMesssaegId(firstMessage.getMessageId());
        groupRepository.saveAndFlush(group);
        firstMessage.setGroupId(group.getGroupId());
        messageRepository.save(firstMessage);
        memberOfGroupRepository.addMemberToGroup(group.getGroupId(),
                group.getMembers());
    }
}
