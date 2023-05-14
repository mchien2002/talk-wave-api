package com.chatapi.sigmaapi.service.user;

import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chatapi.sigmaapi.entity.User;
import com.chatapi.sigmaapi.entity.UserOnlineStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public interface UserService {
    List<User> searchUser(String value) throws JsonMappingException, JsonProcessingException;

    User findUserByID(String id);

    User updateProfile(User newUser);

    User uploadAvatarUser(MultipartFile imgFile, String userId) throws IOException;

    void setUserOnline(String userId);

    void setUserOffline(String userId);

    UserOnlineStatus getUserOnlineStatus(String userId);

    void setUserStatus(String userId, int status);

    List<User> findUserContactInGroup(String userId);
}
