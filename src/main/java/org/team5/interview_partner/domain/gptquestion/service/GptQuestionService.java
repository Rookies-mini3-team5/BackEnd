package org.team5.interview_partner.domain.gptquestion.service;

import org.team5.interview_partner.domain.gptquestion.dto.GptQuestionListResponse;
import org.team5.interview_partner.domain.gptquestion.dto.GptQuestionResponse;

public interface GptQuestionService {
    GptQuestionListResponse gptQuestionList(int sectionId);
    GptQuestionResponse gptQuestion(int sectionId);
}
