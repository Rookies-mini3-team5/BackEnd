package org.team5.interview_partner.domain.calendar.service;

import org.springframework.security.core.Authentication;
import org.team5.interview_partner.domain.calendar.dto.CalendarListResponse;
import org.team5.interview_partner.domain.calendar.dto.CalendarRequest;

public interface CalendarService {
    void insertCalendar(Authentication authentication, CalendarRequest calendarRequest);

    CalendarListResponse calendarList(Authentication authentication, String year, String month);

    void calendarModify(Authentication authentication, CalendarRequest calendarRequest, int calendarId);

    void deleteCalendarMemo(Authentication authentication,int calendarId);

}
