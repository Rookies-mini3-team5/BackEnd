package org.team5.interview_partner.domain.user.dto;

import lombok.Builder;
import lombok.Data;
import org.team5.interview_partner.entity.user.enums.Role;

import java.time.LocalDateTime;

@Data
@Builder
public class LoginResponse {
    public String name;
    public String username;
    public String token;
    public Role role;
}