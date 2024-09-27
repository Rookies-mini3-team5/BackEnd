package org.team5.interview_partner.domain.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
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
import java.util.Optional;
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
        //해당 날짜에 이미 값이 있는지 확인
        CalendarEntity calendarEntityCompare = calendarRepository.findByUserAndDate(usersEntity, calendarRequest.getDate());
        if(calendarEntityCompare != null){
            throw new ApiException(ErrorCode.BAD_REQUEST,"해당 날짜에는 이미 메모가 있습니다.");
        }

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

    @Override
    public void calendarModify(Authentication authentication, CalendarRequest calendarRequest, int calendarId) {
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        UsersEntity usersEntity = userRepository.findByUsername(userDetails.getUsername());
        CalendarEntity calendarEntity = calendarRepository.findById(calendarId)
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST,"calendarId에 해당하는 데이터가 없습니다."));

        CalendarEntity calendarEntityCompare = calendarRepository.findByUserAndDate(usersEntity, calendarRequest.getDate());
        if(calendarEntityCompare != null && calendarEntityCompare.getId() != calendarEntity.getId()){
            throw new ApiException(ErrorCode.BAD_REQUEST,"해당 날짜에는 이미 메모가 있습니다.");
        }



        if(!calendarRequest.getDate().isEmpty()){
            calendarEntity.setDate(calendarRequest.getDate());
        }
        if(!calendarRequest.getMemo().isEmpty()){
            calendarEntity.setMemo(calendarRequest.getMemo());
        }
        calendarRepository.save(calendarEntity);
    }

    @Override
    public void deleteCalendarMemo(Authentication authentication, int calendarId) {
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        UsersEntity usersEntity = userRepository.findByUsername(userDetails.getUsername());
        Optional.ofNullable(calendarRepository.findByUserAndId(usersEntity,calendarId))
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST,"삭제할 데이터가 없습니다."));

        calendarRepository.deleteById(calendarId);
    }


}
