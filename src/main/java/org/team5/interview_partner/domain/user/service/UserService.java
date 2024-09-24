package org.team5.interview_partner.domain.user.service;

import org.springframework.web.multipart.MultipartFile;
import org.team5.interview_partner.domain.user.dto.JoinRequest;
import org.team5.interview_partner.domain.user.dto.LoginRequest;
import org.team5.interview_partner.domain.user.dto.LoginResponse;

public interface UserService {
    //회원가입
    void join(JoinRequest joinRequest, MultipartFile file) throws Exception;
    //로그인
    LoginResponse login(LoginRequest loginRequest);
}