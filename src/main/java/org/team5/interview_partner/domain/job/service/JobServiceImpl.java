package org.team5.interview_partner.domain.job.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.domain.job.dto.JobListResponse;
import org.team5.interview_partner.domain.job.dto.JobResponse;
import org.team5.interview_partner.domain.job.dto.OccupationalListResponse;
import org.team5.interview_partner.domain.job.dto.OccupationalResponse;
import org.team5.interview_partner.domain.job.mapper.JobMapper;
import org.team5.interview_partner.domain.job.mapper.OccupationalMapper;
import org.team5.interview_partner.entity.job.JobEntity;
import org.team5.interview_partner.entity.job.JobRepository;
import org.team5.interview_partner.entity.occupational.OccupationalEntity;
import org.team5.interview_partner.entity.occupational.OccupationalRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService{
    private final JobRepository jobRepository;
    private final OccupationalRepository occupationalRepository;


    //직군 정보 목록
    public OccupationalListResponse occupationalInfoList(){
        List<OccupationalEntity> occupationalEntities = occupationalRepository.findAll();
        List<OccupationalResponse> occupationalResponses = occupationalEntities.stream()
                .map(OccupationalMapper::toResponse)
                .collect(Collectors.toList());

        return OccupationalListResponse.builder()
                .occupationalList(occupationalResponses)
                .build();
    }

    //직업 정보 목록
    public JobListResponse jobListResponse(int occupationalId){
        OccupationalEntity occupational = occupationalRepository.findById(occupationalId)
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST));
        List<JobEntity> jobEntities = jobRepository.findAllByOccupational(occupational);
        List<JobResponse> jobResponses = jobEntities.stream()
                .map(JobMapper::toResponse)
                .collect(Collectors.toList());

        return JobListResponse.builder()
                .jobList(jobResponses)
                .build();
    }

}
