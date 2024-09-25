package org.team5.interview_partner.domain.section.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetSectionInfoResponse {
    private int sectionId;
    private String sectionName;
    private String occupational;
    private String job;
    private String resume;
    private String emphasize;
}
