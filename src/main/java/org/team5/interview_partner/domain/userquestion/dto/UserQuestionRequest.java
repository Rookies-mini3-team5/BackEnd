package org.team5.interview_partner.domain.userquestion.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserQuestionRequest {
    @NotEmpty
    private String question;
}
