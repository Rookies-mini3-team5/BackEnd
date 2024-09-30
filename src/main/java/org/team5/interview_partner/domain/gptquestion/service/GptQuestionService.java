package org.team5.interview_partner.domain.gptquestion.service;

import org.springframework.security.core.Authentication;
import org.team5.interview_partner.domain.gptquestion.dto.GptQuestionListResponse;
import org.team5.interview_partner.domain.gptquestion.dto.GptQuestionResponse;

public interface GptQuestionService {
    GptQuestionListResponse gptQuestionList(Authentication authentication,int sectionId);
    GptQuestionResponse gptQuestion(Authentication authentication,int gptQuestionId);
}
