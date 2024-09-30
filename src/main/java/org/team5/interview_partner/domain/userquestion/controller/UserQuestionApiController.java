package org.team5.interview_partner.domain.userquestion.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.team5.interview_partner.common.api.Api;
import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.domain.gpt.GptApiService;
import org.team5.interview_partner.domain.userquestion.dto.UserQuestionListResponse;
import org.team5.interview_partner.domain.userquestion.dto.UserQuestionRequest;
import org.team5.interview_partner.domain.userquestion.dto.UserQuestionResponse;
import org.team5.interview_partner.domain.userquestion.service.UserQuestionService;
import org.team5.interview_partner.entity.gptquestion.GptQuestionEntity;
import org.team5.interview_partner.entity.gptquestion.GptQuestionRepository;
import org.team5.interview_partner.entity.section.SectionEntity;
import org.team5.interview_partner.entity.section.SectionRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/section/user/question")
public class UserQuestionApiController {
    private final UserQuestionService userQuestionService;


    //유저 질문
    @PostMapping("/{sectionId}")
    public Api<UserQuestionResponse> addUserQuestion(
            @PathVariable("sectionId") int sectionId,
            @RequestBody
            UserQuestionRequest userQuestionRequest,
            Authentication authentication
    ){
        UserQuestionResponse userQuestionResponse = userQuestionService.addUserQuestion(sectionId,userQuestionRequest,authentication);
        return Api.CREATE(userQuestionResponse);
    }

    //질문 답변 목록 조회
    @GetMapping("/list/{sectionId}")
    public Api<UserQuestionListResponse> userQuestionList(
            @PathVariable("sectionId") int sectionId,
            Authentication authentication
    ){
        UserQuestionListResponse userQuestionListResponse = userQuestionService.userQuestionList(sectionId,authentication);
        return Api.OK(userQuestionListResponse);
    }

    //질문 답변 조회
    @GetMapping("/one/{userQuestionId}")
    public Api<UserQuestionResponse> userQuestion(
            @PathVariable("userQuestionId") int userQuestionId,
            Authentication authentication
    ){
        UserQuestionResponse userQuestionResponse = userQuestionService.userQuestion(userQuestionId,authentication);
        return Api.OK(userQuestionResponse);
    }

}
