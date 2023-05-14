package com.chatapi.sigmaapi.controllers.group;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.chatapi.sigmaapi.constant.RouteConstant;
import com.chatapi.sigmaapi.helper.MyResponse;
import com.chatapi.sigmaapi.service.group.GroupService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class GroupController {
    @Autowired
    private GroupService groupService;

    @GetMapping(value = RouteConstant.GROUP_PROFILE + "/{id}")
    public ResponseEntity<?> getGoupsByID(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MyResponse(true, "Successful", null, groupService.getGroupById(id)));
        } catch (Exception e) {
            log.error("Error: " + e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyResponse(false, "Action failed", null, null));
        }
    }

    @PostMapping(value = RouteConstant.GROUP_BY_MEMBERS)
    public ResponseEntity<?> getGroupByMember(@RequestBody List<String> listUserId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MyResponse(true, "Successful", null, groupService.getPublicGroupWithMember(listUserId)));
        } catch (Exception e) {
            log.error("Error: " + e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyResponse(false, "Action failed", null, null));
        }
    }

}
