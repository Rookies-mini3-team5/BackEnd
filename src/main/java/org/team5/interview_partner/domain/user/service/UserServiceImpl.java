package org.team5.interview_partner.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.common.utils.FileUtils;
import org.team5.interview_partner.common.utils.JwtUtils;
import org.team5.interview_partner.domain.user.dto.*;
import org.team5.interview_partner.domain.user.mapper.UserMapper;
import org.team5.interview_partner.entity.user.UserPictureEntity;
import org.team5.interview_partner.entity.user.UserPictureRepository;
import org.team5.interview_partner.entity.user.UserRepository;
import org.team5.interview_partner.entity.user.UsersEntity;
import org.team5.interview_partner.entity.user.enums.Role;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserPictureRepository userPictureRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;
    private final FileUtils fileUtils;

    // 기본 파일 저장소 경로
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    //회원가입
    public void join(JoinRequest joinRequest, MultipartFile file) throws Exception {

        // Check if username already exists
        if (userRepository.existsByUsername(joinRequest.getUsername())) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "이미 존재하는 ID입니다.");
        }

        // Check if nickname already exists
        if (userRepository.existsByEmail(joinRequest.getEmail())) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "이미 존재하는 이메일입니다.");
        }

        // 1. Create and save the user
        UsersEntity user = UserMapper.toEntity(joinRequest);
        user.setPassword(bCryptPasswordEncoder.encode(joinRequest.getPassword()));

        userRepository.save(user);

        // 2. Handle the profile picture
        String storedFilePath;
        String originalFileName;
        String fileSize;

        try {
            if (file != null && !file.isEmpty()) {
                // Store the uploaded file
                storedFilePath = fileUtils.storeFile(file, user.getUsername());

                originalFileName = file.getOriginalFilename();
                fileSize = String.valueOf(file.getSize());
            } else {
                // Use default picture
                    storedFilePath = Paths.get(uploadPath, "defaultPicture.jpeg").toString();
                originalFileName = "defaultPicture.jpeg";

                // Get the file size of the default picture
                Path defaultPicturePath = Paths.get(storedFilePath);
                long defaultFileSize = Files.size(defaultPicturePath);
                fileSize = String.valueOf(defaultFileSize);
            }
        } catch (IOException e) {
            throw new ApiException(ErrorCode.SERVER_ERROR, "파일 저장 실패");
        }

        // 3. Save the UserPictureEntity
        UserPictureEntity userPicture = UserPictureEntity.builder()
                .user(user)
                .originalFileName(originalFileName)
                .filePath(storedFilePath)
                .fileSize(fileSize)
                .createdAt(LocalDateTime.now())
                .build();

        userPictureRepository.save(userPicture);
    }

    // 로그인
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        //유저 유무 확인
        UsersEntity usersEntity = Optional.ofNullable(userRepository.findByUsernameAndRole(loginRequest.getUsername(), Role.ROLE_USER))
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "유저 정보가 없습니다."));
        //바말번호 확인
        boolean passwordMatch = bCryptPasswordEncoder.matches(loginRequest.getPassword(), usersEntity.getPassword());
        if (!passwordMatch) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "비밀번호가 틀렸습니다.");
        }
        LoginResponse loginResponse = UserMapper.toResponse(usersEntity);
        loginResponse.setToken(jwtUtils.generateToken(usersEntity));
        return loginResponse;
    }

    // 프로필 조회
    @Override
    public UserInfoResponse getProfile(String authorization) {
        // 유저 정보 추출
        String token = authorization.substring(7);
        String username = jwtUtils.getSubjectFromToken(token);
        UsersEntity user = userRepository.findByUsername(username);

        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setName(user.getName());
        userInfoResponse.setEmail(user.getEmail());

        return userInfoResponse;
    }

    // 프로필 수정
    @Override
    public void updateProfile(UpdateProfileRequest updateProfileRequest, MultipartFile file, String authorization) throws Exception {

        // 유저 정보 추출
        String token = authorization.substring(7);
        String username = jwtUtils.getSubjectFromToken(token);
        UsersEntity user = userRepository.findByUsername(username);

        if (updateProfileRequest != null) {
            if (updateProfileRequest.getName() != null) {
                user.setName(updateProfileRequest.getName());
            }

            if (updateProfileRequest.getEmail() != null) {
                user.setEmail(updateProfileRequest.getEmail());
            }

            userRepository.save(user);
        }

        // 프사 업데이트
        if (file != null && !file.isEmpty()) {

            UserPictureEntity userPicture = userPictureRepository.findByUserId(user.getId());

            // 회원가입 시 프사가 설정되므로 기본 프사가 아닐 시 삭제 후 작업
            if (!Objects.equals(userPicture.getFilePath(), Paths.get(uploadPath, "defaultPicture.jpeg").toString())) {
                fileUtils.deleteFile(userPicture.getFilePath());
            }

            userPicture.setOriginalFileName(file.getOriginalFilename());
            userPicture.setFilePath(fileUtils.storeFile(file, user.getUsername()));
            userPicture.setFileSize(String.valueOf(file.getSize()));

            userPictureRepository.save(userPicture);
        }
    }
}