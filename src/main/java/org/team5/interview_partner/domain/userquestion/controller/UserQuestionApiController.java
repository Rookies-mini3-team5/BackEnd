package org.team5.interview_partner.domain.userquestion.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.team5.interview_partner.common.api.Api;
import org.team5.interview_partner.domain.userquestion.dto.UserQuestionListResponse;
import org.team5.interview_partner.domain.userquestion.dto.UserQuestionRequest;
import org.team5.interview_partner.domain.userquestion.dto.UserQuestionResponse;
import org.team5.interview_partner.domain.userquestion.service.UserQuestionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/section/user/question")
public class UserQuestionApiController {
    private final UserQuestionService userQuestionService;


    //유저 질문
    @PostMapping("/{sectionId}")
    public Api addUserQuestion(
            @PathVariable("sectionId") int sectionId,
            @RequestBody
            UserQuestionRequest userQuestionRequest
    ){
        userQuestionService.addUserQuestion(sectionId,userQuestionRequest);
        return Api.CREATE();
    }

    //질문 답변 목록 조회
    @GetMapping("/list/{sectionId}")
    public Api<UserQuestionListResponse> userQuestionList(
            @PathVariable("sectionId") int sectionId
    ){
        UserQuestionListResponse userQuestionListResponse = userQuestionService.userQuestionList(sectionId);
        return Api.OK(userQuestionListResponse);
    }

    //질문 답변 조회
    @GetMapping("/{userQuestionId}")
    public Api<UserQuestionResponse> userQuestion(
            @PathVariable("userQuestionId") int userQuestionId
    ){
        UserQuestionResponse userQuestionResponse = userQuestionService.userQeustion(userQuestionId);
        return Api.OK(userQuestionResponse);
    }

}
