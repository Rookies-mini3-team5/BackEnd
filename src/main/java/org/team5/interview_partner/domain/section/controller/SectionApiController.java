package org.team5.interview_partner.domain.section.controller;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.team5.interview_partner.common.api.Api;
import org.team5.interview_partner.common.utils.JwtUtils;
import org.team5.interview_partner.domain.section.dto.*;
import org.team5.interview_partner.domain.section.service.SectionService;
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
    ) {
        List<SectionInfoResponse> sectionInfoResponseList = sectionService.sectionInfoList(authorization);
        return Api.OK(sectionInfoResponseList);
    }

    @PostMapping("/section")
    public Api<AddSectionResponse> addSection(
            @RequestHeader("Authorization") String authorization,
            @RequestBody AddSectionRequest addSectionRequest
    ) {
        AddSectionResponse addSectionResponse = sectionService.addSection(authorization, addSectionRequest);
        return Api.OK(addSectionResponse);
    }

    @PostMapping("/section/{sectionId}")
    public Api<List<AddSectionResumeResponse>> addSectionResumeResponse(
            @RequestHeader("Authorization") String authorization,
            @RequestBody(required = false) AddSectionResumeRequest addSectionResumeRequest,
            @PathVariable("sectionId") int sectionId
    ) {
        List<AddSectionResumeResponse> addSectionResumeResponse = sectionService.addSectionResume(authorization, addSectionResumeRequest, sectionId);
        return Api.OK(addSectionResumeResponse);
    }

    @DeleteMapping("/section/{sectionId}")
    public Api<String> deleteSection(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("sectionId") int sectionId
    ) {
        sectionService.deleteSection(authorization, sectionId);
        return Api.OK("Deleted successfully");
    }
}
