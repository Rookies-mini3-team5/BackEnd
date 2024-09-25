package org.team5.interview_partner.domain.user.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String name;
    private String email;
}
