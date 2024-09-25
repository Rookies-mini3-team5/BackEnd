package org.team5.interview_partner.domain.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.team5.interview_partner.common.api.Api;
import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.common.utils.JwtUtils;
import org.team5.interview_partner.domain.user.dto.UpdateProfileRequest;
import org.team5.interview_partner.domain.user.dto.UserInfoResponse;
import org.team5.interview_partner.domain.user.service.UserService;
import org.team5.interview_partner.entity.user.UserPictureEntity;
import org.team5.interview_partner.entity.user.UserPictureRepository;
import org.team5.interview_partner.entity.user.UserRepository;
import org.team5.interview_partner.entity.user.UsersEntity;

import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserPictureRepository userPictureRepository;

    // 마이페이지 조회
    @GetMapping("/mypage")
    public Api<UserInfoResponse> getProfile(
            @RequestHeader("Authorization") String authorization
    ){
        UserInfoResponse userInfoResponse = userService.getProfile(authorization);
        return Api.OK(userInfoResponse);
    }

    // 프로필 업데이트(사진, 닉네임)
    @PatchMapping("/mypage")
    public Api<String> updateProfile(
            @RequestPart(value = "data", required = false) UpdateProfileRequest updateProfileRequest,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestHeader("Authorization") String authorization
    ) throws Exception {
        if (updateProfileRequest == null && (file == null || file.isEmpty())) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "데이터가 없습니다.");
        }
        userService.updateProfile(updateProfileRequest, file, authorization);
        return Api.OK("Profile updated successfully");
    }

    // 프로필 사진 조회
    @GetMapping("/mypage/picture")
    public void downloadUserPicture(
            @RequestHeader("Authorization") String authorization,
            HttpServletResponse response
    ) throws Exception {
        String token = authorization.substring(7);
        String username = jwtUtils.getSubjectFromToken(token);
        UsersEntity user = userRepository.findByUsername(username);
        UserPictureEntity userPicture = userPictureRepository.findByUserId(user.getId());

        if(userPicture.getFilePath() == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "사진이 없습니다.");
        }

        Path path = Paths.get(userPicture.getFilePath());
        byte[] file = Files.readAllBytes(path);

        response.setContentType("apllication/octect-stream");
        response.setContentLength(file.length);
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(userPicture.getOriginalFileName(), "UTF-8") + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");

        response.getOutputStream().write(file);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}
