package org.team5.interview_partner.domain.user.service;

import org.springframework.web.multipart.MultipartFile;
import org.team5.interview_partner.domain.user.dto.*;

public interface UserService {
    //회원가입
    void join(JoinRequest joinRequest, MultipartFile file) throws Exception;
    //로그인
    LoginResponse login(LoginRequest loginRequest);
    // 프로필 조회
    UserInfoResponse getProfile(String authorization);
    // 프로필 수정
    void updateProfile(UpdateProfileRequest updateProfileRequest, MultipartFile file, String authorization) throws Exception;
}