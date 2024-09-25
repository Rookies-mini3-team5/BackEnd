package org.team5.interview_partner.domain.job.mapper;


import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.domain.job.dto.OccupationalResponse;
import org.team5.interview_partner.entity.occupational.OccupationalEntity;

import java.util.Optional;

public class OccupationalMapper {
    public static OccupationalResponse toResponse(OccupationalEntity occupational){
        return Optional.ofNullable(occupational)
                .map(it->{
                    return OccupationalResponse.builder()
                        .id(occupational.getId())
                        .occupationalName(occupational.getOccupationalName())
                        .build();
                }).orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST));
    }
}
