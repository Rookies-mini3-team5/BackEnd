package org.team5.interview_partner.domain.job.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team5.interview_partner.common.api.Api;
import org.team5.interview_partner.domain.job.dto.JobListResponse;
import org.team5.interview_partner.domain.job.dto.OccupationalListResponse;
import org.team5.interview_partner.domain.job.service.JobService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/occupational")
public class JobOpenApiController {
    private final JobService jobService;
    @GetMapping("")
    public Api<OccupationalListResponse> occupationalInfoList(){
        OccupationalListResponse occupationalListResponse = jobService.occupationalInfoList();
        return Api.OK(occupationalListResponse);
    }
    @GetMapping("/{occupationalId}")
    public Api<JobListResponse> jobListResponse(
            @PathVariable("occupationalId") int occupationalId
    ){
        JobListResponse jobListResponse = jobService.jobListResponse(occupationalId);
        return Api.OK(jobListResponse);
    }
}
