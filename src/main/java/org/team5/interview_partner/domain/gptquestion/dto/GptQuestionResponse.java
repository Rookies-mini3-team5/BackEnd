package org.team5.interview_partner.domain.gptquestion.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import org.team5.interview_partner.entity.section.SectionEntity;

@Data
@Builder
public class GptQuestionResponse {
    private int id;

    private int sectionId;

    private String question;

    private String answerGuide;
}
