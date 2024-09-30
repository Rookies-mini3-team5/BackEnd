package org.team5.interview_partner.domain.section.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AddSectionResumeResponse {
    private int questionId;
    private String expectedQuestion;
    private List<String> answerGuide;
}
