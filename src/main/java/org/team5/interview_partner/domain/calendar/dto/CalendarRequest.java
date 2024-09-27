package org.team5.interview_partner.domain.calendar.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class CalendarRequest {
    private String date;

    private String memo;
}
