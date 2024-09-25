package org.team5.interview_partner.domain.section.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team5.interview_partner.common.api.Api;
import org.team5.interview_partner.common.utils.JwtUtils;
import org.team5.interview_partner.domain.section.dto.SectionInfoResponse;
import org.team5.interview_partner.domain.section.service.SectionService;
import org.team5.interview_partner.domain.user.dto.UserInfoResponse;
import org.team5.interview_partner.domain.user.service.UserService;
import org.team5.interview_partner.entity.user.UserRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class SectionApiController {
    private final SectionService sectionService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @GetMapping("/section")
    public Api<List<SectionInfoResponse>> sectionInfoList(
            @RequestHeader("Authorization") String authorization
    ){
        List<SectionInfoResponse> sectionInfoResponseList = sectionService.sectionInfoList(authorization);
        return Api.OK(sectionInfoResponseList);
    }
}
