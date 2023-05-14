package com.chatapi.sigmaapi.service.message.media;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.springframework.web.multipart.MultipartFile;

import com.chatapi.sigmaapi.entity.Audio;
import com.chatapi.sigmaapi.entity.Image;
import com.chatapi.sigmaapi.entity.Video;

public interface MediaService {
    Image saveImage(MultipartFile file, String messageId) throws FileNotFoundException, IOException;

    Video saveVideo(MultipartFile file, String messageId)
            throws FileNotFoundException, IOException, UnsupportedAudioFileException, LineUnavailableException;

    Audio saveAudio(MultipartFile file, String messageId)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException;

    byte[] downloadAudio(String audioName);

    byte[] downloadImage(String fileName);

    byte[] downloadVideo(String fileName);
}
