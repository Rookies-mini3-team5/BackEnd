package org.team5.interview_partner.domain.section.mapper;

import org.team5.interview_partner.domain.section.dto.AddSectionRequest;
import org.team5.interview_partner.domain.section.dto.SectionInfoResponse;
import org.team5.interview_partner.entity.job.JobEntity;
import org.team5.interview_partner.entity.occupational.OccupationalEntity;
import org.team5.interview_partner.entity.section.SectionEntity;
import org.team5.interview_partner.entity.user.UsersEntity;

import java.util.Optional;

public class SectionMapper {

    // SectionEntity를 SectionInfoResponse로 변환하는 메서드
    public static SectionInfoResponse toSectionInfoResponse(SectionEntity section) {
        return SectionInfoResponse.builder()
                .id(section.getId())
                .name(section.getName())
                .job(section.getJob().getJobName())
                .occupational(section.getOccupational().getOccupationalName())
                .createdAt(section.getCreatedAt())
                .updatedAt(section.getUpdatedAt())
                .build();
    }

    public static SectionEntity toEntity(UsersEntity user, JobEntity job, OccupationalEntity occupational){
        return SectionEntity.builder()
                .user(user)
                .job(job)
                .occupational(occupational)
                .build();
    }
}
