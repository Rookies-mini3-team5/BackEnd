package org.team5.interview_partner.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team5.interview_partner.common.api.Api;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class UserController {
    @GetMapping("/mypage")
    public Api<UserInfoResponse> getProfile(
            @RequestHeader("Authorization") String authorization
    ){
        UserInfoResponse userInfoResponse = userService.getProfile(authorization);
        return Api.OK(userInfoResponse);
    }
}
