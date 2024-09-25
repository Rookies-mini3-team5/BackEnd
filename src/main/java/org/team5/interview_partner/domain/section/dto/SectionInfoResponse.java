package org.team5.interview_partner.domain.section.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SectionInfoResponse {
    private Integer id;
    private String name;             // 섹션 이름
    private String job;             // 직업
    private String occupational;     // 직종
    private LocalDateTime createdAt; // 생성시점
    private LocalDateTime updatedAt; // 수정시점
}
