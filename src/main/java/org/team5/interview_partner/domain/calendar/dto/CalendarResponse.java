package org.team5.interview_partner.domain.calendar.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team5.interview_partner.entity.user.UsersEntity;

@Data
@Builder

public class CalendarResponse {
    private int id;

    private String date;

    private String memo;
}
