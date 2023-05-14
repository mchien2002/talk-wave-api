package com.chatapi.sigmaapi.controllers.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.chatapi.sigmaapi.constant.RouteConstant;
import com.chatapi.sigmaapi.helper.MyResponse;
import com.chatapi.sigmaapi.service.message.media.MediaService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class MediaController {
    @Autowired
    private MediaService attachmentService;

    @GetMapping(RouteConstant.IMAGE_URL + "/{fileImg}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileImg) {
        try {
            byte[] imageData = attachmentService.downloadImage(fileImg);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);
        } catch (Exception e) {
            log.error("Error: " + e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyResponse().errorResponse());
        }
    }

    @GetMapping(RouteConstant.AUDIO_URL + "/{fileAudio}")
    public ResponseEntity<?> downloadAudio(@PathVariable String fileAudio) {
        try {
            byte[] audioData = attachmentService.downloadAudio(fileAudio);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("audio/mp3")).body(audioData);
        } catch (Exception e) {
            log.error("Error: " + e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyResponse().errorResponse());
        }
    }

    @GetMapping(RouteConstant.VIDEO_URL + "/{videoName}")
    public ResponseEntity<?> downloadVideo(@PathVariable String videoName) {
        try {
            byte[] videoData = attachmentService.downloadVideo(videoName);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("video/mp4")).body(videoData);
        } catch (Exception e) {
            log.error("Error: " + e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MyResponse().errorResponse());
        }   
    }
}
