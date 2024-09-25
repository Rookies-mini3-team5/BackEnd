package org.team5.interview_partner.domain.section.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.team5.interview_partner.common.utils.FileUtils;
import org.team5.interview_partner.common.utils.JwtUtils;
import org.team5.interview_partner.domain.section.dto.SectionInfoResponse;
import org.team5.interview_partner.domain.section.mapper.SectionMapper;
import org.team5.interview_partner.entity.section.SectionEntity;
import org.team5.interview_partner.entity.section.SectionRepository;
import org.team5.interview_partner.entity.user.UserPictureRepository;
import org.team5.interview_partner.entity.user.UserRepository;
import org.team5.interview_partner.entity.user.UsersEntity;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final SectionRepository sectionRepository;

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
}
