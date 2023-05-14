package com.chatapi.sigmaapi.service.message.media;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chatapi.sigmaapi.entity.Audio;
import com.chatapi.sigmaapi.entity.Image;
import com.chatapi.sigmaapi.entity.Video;
import com.chatapi.sigmaapi.repositories.AudioRepository;
import java.awt.image.BufferedImage;
import com.chatapi.sigmaapi.repositories.ImageRepository;
import com.chatapi.sigmaapi.repositories.VideoRepository;
import com.chatapi.sigmaapi.util.FileUtils;

@Service
public class MediaServiceIml implements MediaService {
    @Autowired
    private ImageRepository imageAttRepository;
    @Autowired
    private AudioRepository audioAttRepository;
    @Autowired
    private VideoRepository videoAttRepository;

    @Override
    public Image saveImage(MultipartFile file, String messageId) throws FileNotFoundException, IOException {

        Image img = new Image();
        InputStream inputStream = file.getInputStream();
        BufferedImage image = ImageIO.read(inputStream);
        if (image != null) {
            img.setHeight(image.getHeight());
            img.setWidth(image.getWidth());
        }
        img.setMessageId(messageId);
        img.setType(file.getContentType());
        img.setImageData(FileUtils.compressImage(file.getBytes()));
        imageAttRepository.saveAndFlush(img);
        return img;
    }

    @Override
    public byte[] downloadImage(String imgId) {
        Image dbImgData = imageAttRepository.findById(imgId).get();
        byte[] imageByte = FileUtils.decompressImage(dbImgData.getImageData());
        return imageByte;
    }

    @Override
    public Audio saveAudio(MultipartFile file, String messageId)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Audio audio = new Audio();
        audio.setAudioData(FileUtils.compressImage(file.getBytes()));
        audio.setMessageId(messageId);
        audio.setType(file.getContentType());
        audioAttRepository.save(audio);
        return audio;
    }

    @Override
    public byte[] downloadAudio(String audioName) {
        Audio dbAudio = audioAttRepository.findById(audioName).get();
        byte[] audioByte = FileUtils.decompressImage(dbAudio.getAudioData());
        return audioByte;
    }

    @Override
    public Video saveVideo(MultipartFile file, String messageId)
            throws FileNotFoundException, IOException, UnsupportedAudioFileException, LineUnavailableException {
        Video video = new Video();
        video.setVideoData(FileUtils.compressImage(file.getBytes()));
        video.setType(file.getContentType());
        video.setMessageId(messageId);
        Image newThumbnail = new Image();
        newThumbnail.setImageData(FileUtils.compressImage(FileUtils.extractThumbnail(file, messageId).getBytes()));
        imageAttRepository.saveAndFlush(newThumbnail);
        video.setThumbnailUrl(newThumbnail.getUrl());
        videoAttRepository.save(video);
        return video;
    }

    @Override
    public byte[] downloadVideo(String fileName) {
        Video dbVideo = videoAttRepository.findById(fileName).get();
        byte[] videoByte = FileUtils.decompressImage(dbVideo.getVideoData());
        return videoByte;
    }

    // @Override
    // public Image saveImageBySocket(InputStream file, String messageId) throws
    // IOException {
    // BufferedImage image = ImageIO.read(file);
    // ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
    // byte[] buffer = new byte[1024];
    // int len;
    // while ((len = file.read(buffer)) != -1) {
    // byteBuffer.write(buffer, 0, len);
    // }
    // URLConnection connection = new URLConnection(null) {
    // @Override
    // public void connect() throws IOException {
    // }

    // @Override
    // public InputStream getInputStream() throws IOException {
    // return file;
    // }
    // };
    // Image newImageMessage = new Image();
    // newImageMessage.setImageData(buffer);
    // newImageMessage.setHeight(image.getHeight());
    // newImageMessage.setMessageId(messageId);
    // newImageMessage.setWidth(image.getWidth());
    // newImageMessage.setType(connection.getContentType());
    // imageAttRepository.saveAndFlush(newImageMessage);
    // return newImageMessage;
    // }
}
