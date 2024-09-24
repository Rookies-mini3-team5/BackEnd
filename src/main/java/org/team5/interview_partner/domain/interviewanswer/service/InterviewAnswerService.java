package org.team5.interview_partner.domain.interviewanswer.service;

import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerRequest;

public interface InterviewAnswerService {
    void addInterviewAnswer(int gptQuestionId, InterviewAnswerRequest interviewAnswerRequest);
}
