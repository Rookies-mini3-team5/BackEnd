package org.team5.interview_partner.domain.userquestion.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserQuestionListResponse {
    List<UserQuestionResponse> userQuestionList;
}
