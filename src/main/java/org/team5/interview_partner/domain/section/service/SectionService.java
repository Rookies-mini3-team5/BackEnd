package org.team5.interview_partner.domain.section.service;

import org.team5.interview_partner.domain.section.dto.*;

import java.util.List;

public interface SectionService {
    // 섹션 리스트 조회
    List<SectionInfoResponse> sectionInfoList(String authorization);
    // 섹션 생성
    AddSectionResponse addSection(String authorization, AddSectionRequest addSectionRequest);
    // 섹션 내 이력 및 경력 생성
    List<AddSectionResumeResponse> addSectionResume(String authorization, AddSectionResumeRequest addSectionResumeRequest, int sectionId);
}
