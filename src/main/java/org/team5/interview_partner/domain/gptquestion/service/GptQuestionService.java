package org.team5.interview_partner.domain.gptquestion.service;

import org.team5.interview_partner.domain.gptquestion.dto.GptQuestionListResponse;

public interface GptQuestionService {
    GptQuestionListResponse gptQuestionList(int sectionId);
}
