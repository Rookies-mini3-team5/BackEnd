package org.team5.interview_partner.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.team5.interview_partner.common.api.Api;
import org.team5.interview_partner.domain.user.dto.JoinRequest;
import org.team5.interview_partner.domain.user.dto.LoginRequest;
import org.team5.interview_partner.domain.user.dto.LoginResponse;
import org.team5.interview_partner.domain.user.service.UserService;

@RestController
@RequiredArgsConstructor
//jwt가 필요없는 api의 경우 jwtRequestFilter에서 url을 계속 추가해주면서 예외처리를 해줘해서 앞에 open-api를 붙여 추가적인 작업이 없게 만들었습니다
@RequestMapping("/open-api")
@Slf4j
public class UserOpenApiController {
    private final UserService userService;

    @PostMapping("/join")
    public Api<String> join(
            @RequestPart(value = "data") JoinRequest joinRequest,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws Exception {
        userService.join(joinRequest, file);
        return Api.OK("create");
    }

    @PostMapping("/login")
    public Api<LoginResponse> login(
            @RequestBody LoginRequest loginRequest
    ) {
        LoginResponse loginResponse = userService.login(loginRequest);
        return Api.OK(loginResponse);
    }
}