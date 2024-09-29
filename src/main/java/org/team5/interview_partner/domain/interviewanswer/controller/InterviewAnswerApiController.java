package org.team5.interview_partner.domain.interviewanswer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.team5.interview_partner.common.api.Api;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerListResponse;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerRequest;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerResponse;
import org.team5.interview_partner.domain.interviewanswer.service.InterviewAnswerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/section/interview/answer")
public class InterviewAnswerApiController {
    private final InterviewAnswerService interviewAnswerService;

    @PostMapping("/{gptQuestionId}")
    public Api addInterviewAnswer(
            @PathVariable("gptQuestionId") int gptQuestionId,
            @RequestBody InterviewAnswerRequest interviewAnswerRequest
    ){
        interviewAnswerService.addInterviewAnswer(gptQuestionId,interviewAnswerRequest);
        return Api.CREATE();
    }

    @GetMapping("/list/{gptQuestionId}")
    public Api<InterviewAnswerListResponse> interviewAnswerList(
            @PathVariable("gptQuestionId") int gptQuestionId
    ){
        InterviewAnswerListResponse interviewAnswerListResponse = interviewAnswerService.interviewAnswerList(gptQuestionId);
        return Api.OK(interviewAnswerListResponse);
    }

    @GetMapping("/{interviewAnswerId}")
    public Api<InterviewAnswerResponse> interviewAnswer(
            @PathVariable("interviewAnswerId") int interviewAnswerId
    ){
        InterviewAnswerResponse interviewAnswerResponse = interviewAnswerService.interviewAnswer(interviewAnswerId);
        return Api.OK(interviewAnswerResponse);
    }

}
