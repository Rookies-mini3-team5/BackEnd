package org.team5.interview_partner.domain.section.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddSectionResumeResponse {
    private String expectedQuestion;
    private String answerGuide;
}
