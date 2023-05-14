package com.chatapi.sigmaapi.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.io.InputStreamReader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
    public static byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4 * 1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }

    public static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4 * 1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }

    public static MultipartFile convertToMultipartFile(String byteString) {
        // Chuyển đổi ByteString thành MultipartFile
        byte[] bytes = Base64.getDecoder().decode(byteString);
        InputStream inputStream = new ByteArrayInputStream(bytes);
        String fileName = UUID.randomUUID().toString();
        // Tạo đối tượng MultipartFile từ InputStream
        MultipartFile multipartFile = new MultipartFile() {
            @Override
            public String getName() {
                return "file";
            }

            @Override
            public String getOriginalFilename() {
                return fileName;
            }

            @Override
            public String getContentType() {
                return "application/octet-stream";
            }

            @Override
            public boolean isEmpty() {
                return bytes.length == 0;
            }

            @Override
            public long getSize() {
                return bytes.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return bytes;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return inputStream;
            }

            @Override
            public void transferTo(java.io.File file) throws IOException, IllegalStateException {
                // Không cần thiết trong trường hợp này
            }
        };
        return multipartFile;
    }

    public static MultipartFile convertToMultipartFileMp4(String byteString) throws IOException {
        byte[] bytes = Base64.getDecoder().decode(byteString);
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        MultipartFile multipartFile = new MockMultipartFile("filename.mp4", byteStream);
        return multipartFile;
    }

    public static MultipartFile extractThumbnail(MultipartFile file, String messageId) throws IOException {
        String mainPath = ClassUtils.getDefaultClassLoader()
                .getResource("")
                .getPath()
                .replace("/target/classes", "/src/main");
        String pythonScriptPath = mainPath + "java/com/chatapi/sigmaapi/assets/thumbnail/extract_images.py";
        String videoName = messageId + ".mp4";
        String outPutThumbnail = messageId;
        String fileThumbnail = mainPath + "java/com/chatapi/sigmaapi/assets/thumbnail/thumbnail_img/" + messageId
                + ".jpg";
        String fileVideo = mainPath + "java/com/chatapi/sigmaapi/assets/thumbnail/video_message/" + messageId + ".mp4";

        File videoFileMulti = new File(fileVideo);
        videoFileMulti.getParentFile().mkdirs();
        file.transferTo(videoFileMulti);

        ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPath.substring(1), videoName, outPutThumbnail);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        File newThumbnail = new File(fileThumbnail);
        FileInputStream inputThumbNail = new FileInputStream(newThumbnail);
        byte[] bytes = new byte[(int) newThumbnail.length()];
        inputThumbNail.read(bytes);
        MultipartFile multipartFile = new MockMultipartFile("file.jpg", bytes);
        inputThumbNail.close();
        videoFileMulti.delete();
        newThumbnail.delete();
        return multipartFile;
    }

    public static MultipartFile videoMultipartFile(String base64String, String messageId) throws IOException {
        String mainPath = ClassUtils.getDefaultClassLoader()
                .getResource("")
                .getPath()
                .replace("/target/classes", "/src/main");
        String fileVideoData = mainPath + "java/com/chatapi/sigmaapi/assets/thumbnail/data_video/" + messageId
                + ".h265";
        String ffmpeg = mainPath + "java/com/chatapi/sigmaapi/ffmpeg/bin/ffmpeg.exe";
        String fileVideo = mainPath + "java/com/chatapi/sigmaapi/assets/thumbnail/video_message/" + messageId + ".mp4";

        byte[] decodedBytes = Base64.getDecoder().decode(base64String);

        File input = new File(fileVideoData);
        try (FileOutputStream outputStream = new FileOutputStream(input)) {
            outputStream.write(decodedBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sử dụng FFmpeg để chuyển đổi định dạng video từ HEVC sang H.264 và kết hợp âm
        // thanh
        try {
            Process process = new ProcessBuilder(ffmpeg.substring(1), "-y", "-i", fileVideoData.substring(1), "-c:v",
                    "libx264", "-c:a", "aac", "-strict", "experimental", fileVideo.substring(1))
                    .redirectErrorStream(true).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Hiển thị log trên console
                // Hoặc lưu vào file log: FileWriter.write(line + "\n");
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        // Xóa file ảo
        input.delete();

        File videoMultipart = new File(fileVideo);
        FileInputStream inputThumbNail = new FileInputStream(videoMultipart);
        byte[] bytes = new byte[(int) videoMultipart.length()];
        inputThumbNail.read(bytes);
        MultipartFile multipartFile = new MockMultipartFile("file.mp4", bytes);
        inputThumbNail.close();
        new File(fileVideo).delete();
        return multipartFile;
    }

}
