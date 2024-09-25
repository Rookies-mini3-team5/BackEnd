package org.team5.interview_partner.domain.job.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JobListResponse {
    List<JobResponse> jobList;
}
