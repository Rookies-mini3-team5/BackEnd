package org.team5.interview_partner.domain.interviewanswer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.team5.interview_partner.common.api.Api;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerListResponse;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerRequest;
import org.team5.interview_partner.domain.interviewanswer.service.InterviewAnswerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/section/interview/answer")
public class InterviewAnswerApiController {
    private final InterviewAnswerService interviewAnswerService;

    @PostMapping("/{gptQeustionId}")
    public Api addInterviewAnswer(
            @PathVariable("gptQeustionId") int gptQuestionId,
            @RequestBody InterviewAnswerRequest interviewAnswerRequest
    ){
        interviewAnswerService.addInterviewAnswer(gptQuestionId,interviewAnswerRequest);
        return Api.CREATE();
    }

    @GetMapping("/list/{gptQeustionId}")
    public Api<InterviewAnswerListResponse> interviewAnswerList(
            @PathVariable("gptQeustionId") int gptQeustionId
    ){

    }

}
