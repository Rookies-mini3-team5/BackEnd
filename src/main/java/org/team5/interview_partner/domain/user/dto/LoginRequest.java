package org.team5.interview_partner.domain.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
