package org.team5.interview_partner.domain.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarListResponse {
    List<CalendarResponse> calendarList;
}
