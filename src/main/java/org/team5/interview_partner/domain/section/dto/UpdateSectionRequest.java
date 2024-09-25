package org.team5.interview_partner.domain.section.dto;

import lombok.Data;

@Data
public class UpdateSectionRequest {
    private String name;
    private String resume;
    private String emphasize;
}
