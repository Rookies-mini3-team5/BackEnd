package org.team5.interview_partner.domain.job.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import org.team5.interview_partner.entity.occupational.OccupationalEntity;

@Data
@Builder
public class JobResponse {
    private int id;

    private String jobName;
}
