package org.team5.interview_partner.domain.user.dto;

import lombok.Data;

@Data
public class JoinRequest {
    private String username;
    private String name;
    private String password;
    private String email;
}