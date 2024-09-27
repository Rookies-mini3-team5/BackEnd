package org.team5.interview_partner.entity.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team5.interview_partner.entity.user.UsersEntity;

import java.util.List;

public interface CalendarRepository extends JpaRepository<CalendarEntity,Integer> {
    List<CalendarEntity> findByUserAndDateContains(UsersEntity usersEntity,String date);

}
