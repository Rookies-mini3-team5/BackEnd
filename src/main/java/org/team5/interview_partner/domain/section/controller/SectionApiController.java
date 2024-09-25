package org.team5.interview_partner.domain.section.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.team5.interview_partner.common.api.Api;
import org.team5.interview_partner.domain.section.dto.*;
import org.team5.interview_partner.domain.section.service.SectionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class SectionApiController {
    private final SectionService sectionService;

    // section 목록 조회
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

    @GetMapping("/section/{sectionId}")
    public Api<GetSectionInfoResponse> getSectionInfo(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("sectionId") int sectionId
    ){
        GetSectionInfoResponse getSectionInfoResponse = sectionService.getSectionInfo(authorization, sectionId);
        return Api.OK(getSectionInfoResponse);
    }
}
