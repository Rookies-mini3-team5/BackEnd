package org.team5.interview_partner.common.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
@Component
public class FileUtils {

    @Value("${spring.servlet.multipart.location}")
    private String baseUploadDir; // 기본 업로드 디렉토리

    public String storeFile(MultipartFile file, String username) throws Exception {
        if (ObjectUtils.isEmpty(file) || file.isEmpty()) {
            return null;
        }

        // 사용자별 디렉토리를 지정
        String userDir = baseUploadDir + File.separator + username;
        File dir = new File(userDir);
        if (!dir.exists()) {
            dir.mkdirs(); // 다중 디렉토리 생성
        }

        // 파일 확장자 결정
        String contentType = file.getContentType();
        String fileExtension = getFileExtension(contentType);

        // 저장에 사용할 파일 이름을 조합 (현재 시간을 파일명으로 사용)
        String storedFileName = System.nanoTime() + fileExtension;
        File dest = new File(userDir + File.separator + storedFileName);

        // 파일을 저장 디렉토리에 저장
        file.transferTo(dest);

        // 파일 경로 반환 (DB에 저장할 용도로 사용)
        return userDir + File.separator + storedFileName;
    }

    private String getFileExtension(String contentType) {
        if (contentType.contains("jpeg")) {
            return ".jpg";
        } else if (contentType.contains("png")) {
            return ".png";
        } else if (contentType.contains("gif")) {
            return ".gif";
        } else {
            return "";
        }
    }

    public void deleteFile(String filePath) throws Exception {
        if (filePath != null && !filePath.isEmpty()) {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
        }
    }
}