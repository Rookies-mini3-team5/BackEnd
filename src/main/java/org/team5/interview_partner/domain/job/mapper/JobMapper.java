package org.team5.interview_partner.domain.job.mapper;

import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.domain.job.dto.JobResponse;
import org.team5.interview_partner.entity.job.JobEntity;

import java.util.Optional;

public class JobMapper {
    public static JobResponse toResponse(JobEntity jobEntity){
        return Optional.ofNullable(jobEntity)
                .map(it->{
                    return JobResponse.builder()
                            .id(jobEntity.getId())
                            .jobName(jobEntity.getJobName())
                            .build();
                }).orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST));
    }
}
