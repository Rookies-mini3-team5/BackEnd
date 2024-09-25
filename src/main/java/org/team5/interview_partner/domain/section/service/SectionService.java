package org.team5.interview_partner.domain.section.service;

import org.team5.interview_partner.domain.section.dto.SectionInfoResponse;

import java.util.List;

public interface SectionService {
    // 섹션 리스트 조회
    List<SectionInfoResponse> sectionInfoList(String authorization);
}
