package com.chatapi.sigmaapi.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chatapi.sigmaapi.constant.RouteConstant;
import com.chatapi.sigmaapi.entity.User;
import com.chatapi.sigmaapi.helper.MyResponse;
import com.chatapi.sigmaapi.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(RouteConstant.USER_PROFILE)
    public ResponseEntity<?> getUserProfile(@RequestParam("id") String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MyResponse(true, "Successful", null, userService.findUserByID(id)));
        } catch (Exception e) {
            log.error("Error: " + e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyResponse().errorResponse());
        }
    }

    @RequestMapping(value = RouteConstant.SEARCH_USER, method = RequestMethod.GET)
    public ResponseEntity<?> searchUser(@RequestParam("key") String value) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MyResponse(true, "Successful", null, userService.searchUser(value)));
        } catch (Exception e) {
            log.error("Error: " + e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyResponse().errorResponse());
        }
    }

    @RequestMapping(value = RouteConstant.UPDATE_PROFILE_USER, method = RequestMethod.POST)
    public ResponseEntity<?> updateProfileUser(@RequestBody User user) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MyResponse(true, "Sửa thông tin người dùng thành công", null,
                            userService.updateProfile(user)));
        } catch (Exception e) {
            log.error("Error: " + e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyResponse().errorResponse());
        }
    }

    @RequestMapping(value = RouteConstant.UPLOAD_USER_AVATAR, method = RequestMethod.POST)
    public ResponseEntity<?> uploadAvatarUser(@RequestParam("userId") String userId,
            @RequestBody MultipartFile imgFile) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MyResponse(true, "Cập nhật ảnh đại diện thành công", null,
                            userService.uploadAvatarUser(imgFile, userId)));
        } catch (Exception e) {
            log.error("Error: " + e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyResponse().errorResponse());
        }
    }

    @RequestMapping(value = RouteConstant.USER_ONLINE_STATUS, method = RequestMethod.GET)
    public ResponseEntity<?> getUserOnlineStatus(@RequestParam("userId") String userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MyResponse(true, "Successful", null, userService.getUserOnlineStatus(userId)));
        } catch (Exception e) {
            log.error("Error: " + e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyResponse().errorResponse());
        }
    }

    @RequestMapping(value = RouteConstant.USER_HAVE_SAME_GROUP, method = RequestMethod.GET)
    public ResponseEntity<?> findUserContactInGroup(@RequestParam("userId") String userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MyResponse(true, "Successful", null, userService.findUserContactInGroup(userId)));
        } catch (Exception e) {
            log.error("Error: " + e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyResponse().errorResponse());
        }
    }

    @RequestMapping(value = RouteConstant.UPDATE_USER_ONLINE_STATUS, method = RequestMethod.POST)
    public ResponseEntity<?> updateUserStatus(@RequestParam("userId") String userId,
            @RequestParam("status") int status) {
        try {
            userService.setUserStatus(userId, status);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MyResponse(true, "Successful", null, null));
        } catch (Exception e) {
            log.error("Error: " + e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyResponse().errorResponse());
        }
    }
}
