package org.team5.interview_partner.domain.job.service;

import org.team5.interview_partner.domain.job.dto.JobListResponse;
import org.team5.interview_partner.domain.job.dto.OccupationalListResponse;

public interface JobService {
    OccupationalListResponse occupationalInfoList();

    JobListResponse jobListResponse(int occupationalId);
}
