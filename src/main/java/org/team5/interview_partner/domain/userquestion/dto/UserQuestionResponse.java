package org.team5.interview_partner.domain.userquestion.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserQuestionResponse {
    public int id;
    public int sectionId;
    public String question;
    public String answer;
}
