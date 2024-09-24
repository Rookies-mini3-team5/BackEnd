package org.team5.interview_partner.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.common.utils.FileUtils;
import org.team5.interview_partner.common.utils.JwtUtils;
import org.team5.interview_partner.domain.user.dto.JoinRequest;
import org.team5.interview_partner.domain.user.dto.LoginRequest;
import org.team5.interview_partner.domain.user.dto.LoginResponse;
import org.team5.interview_partner.domain.user.mapper.UserMapper;
import org.team5.interview_partner.entity.user.UserPictureEntity;
import org.team5.interview_partner.entity.user.UserPictureRepository;
import org.team5.interview_partner.entity.user.UserRepository;
import org.team5.interview_partner.entity.user.UsersEntity;
import org.team5.interview_partner.entity.user.enums.Role;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserPictureRepository userPictureRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;
    private final FileUtils fileUtils;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    //회원가입
    public void join(JoinRequest joinRequest, MultipartFile file) throws Exception{

        // Check if username already exists
        if (userRepository.existsByUsername(joinRequest.getUsername())) {
            throw new ApiException(ErrorCode.BAD_REQUEST,"이미 존재하는 ID입니다.");
        }

        // Check if nickname already exists
        if (userRepository.existsByEmail(joinRequest.getEmail())) {
            throw new ApiException(ErrorCode.BAD_REQUEST,"이미 존재하는 이메일입니다.");
        }

        // 1. Create and save the user
        UsersEntity user = UserMapper.toEntity(joinRequest);
        user.setPassword(bCryptPasswordEncoder.encode(joinRequest.getPassword()));

        userRepository.save(user);

        // 2. Handle the profile picture
        String storedFilePath;
        String originalFileName;
        String fileSize;

        if (file != null && !file.isEmpty()) {
            // Store the uploaded file
            storedFilePath = fileUtils.storeFile(file, user.getUsername());

            originalFileName = file.getOriginalFilename();
            fileSize = String.valueOf(file.getSize());
        } else {
            // Use default picture
            storedFilePath = Paths.get(uploadPath, "defaultPicture.jpg").toString();
            originalFileName = "defaultPicture.jpg";

            // Get the file size of the default picture
            Path defaultPicturePath = Paths.get(storedFilePath);
            long defaultFileSize = Files.size(defaultPicturePath);
            fileSize = String.valueOf(defaultFileSize);
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

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        //유저 유무 확인
        UsersEntity usersEntity = Optional.ofNullable(userRepository.findByUsernameAndRole(loginRequest.getUsername(), Role.ROLE_USER))
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST,"유저 정보가 없습니다."));
        //바말번호 확인
        boolean passwordMatch = bCryptPasswordEncoder.matches(loginRequest.getPassword(), usersEntity.getPassword());
        if(!passwordMatch){
            throw new ApiException(ErrorCode.BAD_REQUEST,"비밀번호가 틀렸습니다.");
        }
        LoginResponse loginResponse = UserMapper.toResponse(usersEntity);
        loginResponse.setToken(jwtUtils.generateToken(usersEntity));
        return loginResponse;
    }
}