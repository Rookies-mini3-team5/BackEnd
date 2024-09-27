package org.team5.interview_partner.domain.calendar.mapper;

import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.domain.calendar.dto.CalendarRequest;
import org.team5.interview_partner.domain.calendar.dto.CalendarResponse;
import org.team5.interview_partner.entity.calendar.CalendarEntity;
import org.team5.interview_partner.entity.user.UsersEntity;

import java.util.Optional;

public class CalendarMapper {
    public static CalendarEntity toEntity(UsersEntity usersEntity, CalendarRequest calendarRequest){
        return Optional.ofNullable(calendarRequest).map(
                it->{
                    return CalendarEntity.builder()
                            .user(usersEntity)
                            .memo(calendarRequest.getMemo())
                            .date(calendarRequest.getDate())
                            .build();
                }
        ).orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST));
    }

    public static CalendarResponse toResponse(CalendarEntity calendarEntity){
        return Optional.ofNullable(calendarEntity).map(
                it->{
                    return CalendarResponse.builder()
                            .id(calendarEntity.getId())
                            .memo(calendarEntity.getMemo())
                            .date(calendarEntity.getDate())
                            .build();
                }
        ).orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST));
    }
}
