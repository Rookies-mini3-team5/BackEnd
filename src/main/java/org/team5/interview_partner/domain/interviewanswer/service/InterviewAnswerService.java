package org.team5.interview_partner.domain.interviewanswer.service;

import org.springframework.security.core.Authentication;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerListResponse;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerRequest;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerResponse;

public interface InterviewAnswerService {
    void addInterviewAnswer(int gptQuestionId, InterviewAnswerRequest interviewAnswerRequest, Authentication authentication);
    InterviewAnswerListResponse interviewAnswerList(int gptQuestionId,Authentication authentication);
    InterviewAnswerResponse interviewAnswer(int interviewAnswerId,Authentication authentication);
}
