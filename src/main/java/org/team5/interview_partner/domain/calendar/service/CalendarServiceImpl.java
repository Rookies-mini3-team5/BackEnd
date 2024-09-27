package org.team5.interview_partner.domain.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.team5.interview_partner.domain.calendar.dto.CalendarListResponse;
import org.team5.interview_partner.domain.calendar.dto.CalendarRequest;
import org.team5.interview_partner.domain.calendar.dto.CalendarResponse;
import org.team5.interview_partner.domain.calendar.mapper.CalendarMapper;
import org.team5.interview_partner.domain.user.dto.CustomUserDetail;
import org.team5.interview_partner.entity.calendar.CalendarEntity;
import org.team5.interview_partner.entity.calendar.CalendarRepository;
import org.team5.interview_partner.entity.user.UserRepository;
import org.team5.interview_partner.entity.user.UsersEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;

    //메모 추가
    @Override
    public void insertCalendar(Authentication authentication, CalendarRequest calendarRequest) {
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        UsersEntity usersEntity = userRepository.findByUsername(userDetails.getUsername());
        CalendarEntity calendarEntity = CalendarMapper.toEntity(usersEntity,calendarRequest);
        calendarRepository.save(calendarEntity);
    }
    //메모 목록 조회
    @Override
    public CalendarListResponse calendarList(Authentication authentication, String year, String month) {
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        UsersEntity usersEntity = userRepository.findByUsername(userDetails.getUsername());
        String date = year+"-"+month;
        List<CalendarEntity> calendarEntities = calendarRepository.findByUserAndDateContains(usersEntity,date);
        List<CalendarResponse> calendarList = calendarEntities.stream()
                .map(CalendarMapper::toResponse)
                .collect(Collectors.toList());
        CalendarListResponse calendarListResponse = new CalendarListResponse(calendarList);
        return calendarListResponse;

    }

    //메모 목록




}
