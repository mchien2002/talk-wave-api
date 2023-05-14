package com.chatapi.sigmaapi.service.user;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chatapi.sigmaapi.entity.Image;
import com.chatapi.sigmaapi.entity.User;
import com.chatapi.sigmaapi.entity.UserOnlineStatus;
import com.chatapi.sigmaapi.entity.enum_model.UserStatus;
import com.chatapi.sigmaapi.repositories.ImageRepository;
import com.chatapi.sigmaapi.repositories.MessageRepository;
import com.chatapi.sigmaapi.repositories.UserOnlineStatusRepository;
import com.chatapi.sigmaapi.repositories.UserRepository;
import com.chatapi.sigmaapi.util.DateFormat;
import com.chatapi.sigmaapi.util.FileUtils;
import com.chatapi.sigmaapi.util.FormatString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.image.BufferedImage;

@Service
public class UserServiceIml implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserOnlineStatusRepository userOnlineStatusRepository;

    @Override
    public List<User> searchUser(String value) throws JsonMappingException, JsonProcessingException {
        if (value.equals("") || value.equals(null)) {
            return new ArrayList<>();
        }
        ObjectMapper mapper = new ObjectMapper();
        String responseData = userRepository.findUserByKey(FormatString.filterSQLMiddle(value));
        return mapper.readValue(responseData, new TypeReference<List<User>>() {
        });
    }

    @Override
    public User findUserByID(String id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User updateProfile(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public User uploadAvatarUser(MultipartFile imgFile, String userId) throws IOException {
        InputStream inputStream = imgFile.getInputStream();
        BufferedImage image = ImageIO.read(inputStream);
        User userUploadAvatar = findUserByID(userId);
        if (userUploadAvatar.getAvatar() != null) {
            Image newImage = imageRepository.findById(userUploadAvatar.getAvatar()).get();
            newImage.setType(imgFile.getContentType());
            if (image != null) {
                newImage.setHeight(image.getHeight());
                newImage.setWidth(image.getWidth());
            }
            newImage.setImageData(FileUtils.compressImage(imgFile.getBytes()));
            imageRepository.save(newImage);
        } else {
            Image newImage = new Image();
            newImage.setType(imgFile.getContentType());
            if (image != null) {
                newImage.setHeight(image.getHeight());
                newImage.setWidth(image.getWidth());
            }
            newImage.setImageData(FileUtils.compressImage(imgFile.getBytes()));
            imageRepository.saveAndFlush(newImage);
            userUploadAvatar.setAvatar(newImage.getUrl());
            messageRepository.setAvatarSender(newImage.getUrl(), userId);
            userRepository.save(userUploadAvatar);
        }
        return userUploadAvatar;
    }

    @Override
    public void setUserOnline(String userId) {
        UserOnlineStatus currentUser = userOnlineStatusRepository.findById(userId).get();
        currentUser.setStatus(UserStatus.ONLINE.ordinal());
        userOnlineStatusRepository.save(currentUser);
    }

    @Override
    public void setUserOffline(String userId) {
        UserOnlineStatus currentUser = userOnlineStatusRepository.findById(userId).get();
        if (currentUser.getStatus() == UserStatus.ONLINE.ordinal()) {
            currentUser.setStatus(UserStatus.OFFLINE.ordinal());
            currentUser.setLastTimeOnline(DateFormat.getCurrentTime());
            userOnlineStatusRepository.save(currentUser);
        }
    }

    @Override
    public UserOnlineStatus getUserOnlineStatus(String userId) {
        return userOnlineStatusRepository.findById(userId).get();
    }

    @Override
    public void setUserStatus(String userId, int status) {
        UserOnlineStatus currentUser = userOnlineStatusRepository.findById(userId).get();
        currentUser.setStatus(status);
        if (status == UserStatus.OFFLINE.ordinal()) {
            currentUser.setLastTimeOnline(DateFormat.getCurrentTime());
        }
        userOnlineStatusRepository.save(currentUser);
    }

    @Override
    public List<User> findUserContactInGroup(String userId) {
        return userRepository.findUserContactInGroup(userId);
    }

}
