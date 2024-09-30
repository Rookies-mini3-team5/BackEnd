package org.team5.interview_partner.domain.gptquestion.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.team5.interview_partner.common.api.Api;
import org.team5.interview_partner.domain.gptquestion.dto.GptQuestionListResponse;
import org.team5.interview_partner.domain.gptquestion.dto.GptQuestionResponse;
import org.team5.interview_partner.domain.gptquestion.service.GptQuestionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/section/gpt/question")
public class GptQuestionApiController {
    private final GptQuestionService gptQuestionService;

    @GetMapping("/list/{sectionId}")
    public Api<GptQuestionListResponse> gptQuestionList(
        @PathVariable("sectionId") int sectionId,
        Authentication authentication
    ){
        GptQuestionListResponse response = gptQuestionService.gptQuestionList(authentication,sectionId);
        return Api.OK(response);
    }

    @GetMapping("/{gptQuestionId}")
    public Api<GptQuestionResponse> gptQuestion(
            @PathVariable("gptQuestionId") int gptQuestionId,
            Authentication authentication
    ){
        GptQuestionResponse response = gptQuestionService.gptQuestion(authentication,gptQuestionId);
        return Api.OK(response);
    }
}
