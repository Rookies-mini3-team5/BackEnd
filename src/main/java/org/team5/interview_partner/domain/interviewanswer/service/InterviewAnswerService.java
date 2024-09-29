package org.team5.interview_partner.domain.interviewanswer.service;

import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerListResponse;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerRequest;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerResponse;

public interface InterviewAnswerService {
    void addInterviewAnswer(int gptQuestionId, InterviewAnswerRequest interviewAnswerRequest);
    InterviewAnswerListResponse interviewAnswerList(int gptQuestionId);
    InterviewAnswerResponse interviewAnswer(int interviewAnswerId);
}
