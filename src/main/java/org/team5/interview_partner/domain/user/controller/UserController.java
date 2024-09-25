package org.team5.interview_partner.domain.user.controller;

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
import org.team5.interview_partner.entity.user.UserPictureRepository;
import org.team5.interview_partner.entity.user.UserRepository;

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
}
