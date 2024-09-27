package org.team5.interview_partner.domain.calendar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.team5.interview_partner.common.api.Api;
import org.team5.interview_partner.domain.calendar.dto.CalendarListResponse;
import org.team5.interview_partner.domain.calendar.dto.CalendarRequest;
import org.team5.interview_partner.domain.calendar.service.CalendarService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarApiController {
    private final CalendarService calendarService;

    //메모 생성
    @PostMapping("/")
    public Api insertCalendar(
            @RequestBody
            CalendarRequest calendarRequest,
            Authentication authentication
    ){
        calendarService.insertCalendar(authentication,calendarRequest);
        return Api.CREATE();
    }

    //매모 목록 조회
    @GetMapping("/")
    public Api<CalendarListResponse> calendarList(
            Authentication authentication,
            @RequestParam(name = "year") String year,
            @RequestParam(name = "month") String month
    ){
        CalendarListResponse calendarListResponse = calendarService.calendarList(authentication,year,month);
        return Api.OK(calendarListResponse);
    }

    //메모 수정
}
