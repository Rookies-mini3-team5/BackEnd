package org.team5.interview_partner.domain.interviewanswer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class InterviewAnswerListResponse {
    List<InterviewAnswerResponse> interviewAnswerList;

}
