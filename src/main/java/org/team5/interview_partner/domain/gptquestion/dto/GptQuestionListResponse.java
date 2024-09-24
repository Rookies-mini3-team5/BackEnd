package org.team5.interview_partner.domain.gptquestion.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GptQuestionListResponse {
    List<GptQuestionResponse> gptQuestionList;
}
