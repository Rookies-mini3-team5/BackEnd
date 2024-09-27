package org.team5.interview_partner.domain.interviewanswer.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import org.team5.interview_partner.entity.gptquestion.GptQuestionEntity;

import java.util.List;

@Data
@Builder
public class InterviewAnswerResponse {
    private int id;

    private int gptQuestionId;

    private String answer;

    private List<String> feedbackList;
}
