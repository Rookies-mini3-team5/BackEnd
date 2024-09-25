package org.team5.interview_partner.domain.job.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OccupationalResponse {
    private int id;
    private String occupationalName;
}
