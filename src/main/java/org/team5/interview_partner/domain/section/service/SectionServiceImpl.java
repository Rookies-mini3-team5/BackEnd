package org.team5.interview_partner.domain.section.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.common.utils.FileUtils;
import org.team5.interview_partner.common.utils.JwtUtils;
import org.team5.interview_partner.domain.section.dto.AddSectionRequest;
import org.team5.interview_partner.domain.section.dto.AddSectionResponse;
import org.team5.interview_partner.domain.section.dto.SectionInfoResponse;
import org.team5.interview_partner.domain.section.mapper.SectionMapper;
import org.team5.interview_partner.entity.job.JobEntity;
import org.team5.interview_partner.entity.job.JobRepository;
import org.team5.interview_partner.entity.occupational.OccupationalEntity;
import org.team5.interview_partner.entity.occupational.OccupationalRepository;
import org.team5.interview_partner.entity.section.SectionEntity;
import org.team5.interview_partner.entity.section.SectionRepository;
import org.team5.interview_partner.entity.user.UserPictureRepository;
import org.team5.interview_partner.entity.user.UserRepository;
import org.team5.interview_partner.entity.user.UsersEntity;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final SectionRepository sectionRepository;
    private final JobRepository jobRepository;
    private final OccupationalRepository occupationalRepository;

    // DateTimeFormatter를 통해 YYMMDD 형식으로 포맷
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");

    @Override
    public List<SectionInfoResponse> sectionInfoList(String authorization) {
        // 유저 정보 추출
        String token = authorization.substring(7);
        String username = jwtUtils.getSubjectFromToken(token);
        UsersEntity user = userRepository.findByUsername(username);

        List<SectionEntity> sections = sectionRepository.findByUser(user);

        // SectionEntity를 SectionInfoResponse로 변환하여 반환
        return sections.stream().map(SectionMapper::toSectionInfoResponse).collect(Collectors.toList());
    }

    @Override
    public AddSectionResponse addSection(String authorization, AddSectionRequest addSectionRequest) {
        // 유저 정보 추출
        String token = authorization.substring(7);
        String username = jwtUtils.getSubjectFromToken(token);
        UsersEntity user = userRepository.findByUsername(username);

        String errorMsg = "";

        Optional<JobEntity> job = jobRepository.findById(addSectionRequest.getJob());
        Optional<OccupationalEntity> occupational = occupationalRepository.findById(addSectionRequest.getOccupational());
        if(job.isPresent() && occupational.isPresent()){
            JobEntity jobEntity = job.get();
            OccupationalEntity occupationalEntity = occupational.get();

            SectionEntity sectionEntity = SectionMapper.toEntity(user, jobEntity, occupationalEntity);
            SectionEntity section = sectionRepository.save(sectionEntity);

            return AddSectionResponse.builder()
                    .id(section.getId())
                    .build();
        } else if (job.isEmpty()) {
            errorMsg = "존재하지 않는 직업입니다.";
        } else {
            errorMsg = "존재하지 않는 직군입니다.";
        }

        throw new ApiException(ErrorCode.BAD_REQUEST, errorMsg);
    }
}
