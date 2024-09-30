package org.team5.interview_partner.domain.gptquestion.controller;

import lombok.RequiredArgsConstructor;
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
        @PathVariable("sectionId") int sectionId
    ){
        GptQuestionListResponse response = gptQuestionService.gptQuestionList(sectionId);
        return Api.OK(response);
    }

    @GetMapping("/{gptQuestionId}")
    public Api<GptQuestionResponse> gptQuestion(
            @PathVariable("gptQuestionId") int gptQuestionId
    ){
        GptQuestionResponse response = gptQuestionService.gptQuestion(gptQuestionId);
        return Api.OK(response);
    }
}
