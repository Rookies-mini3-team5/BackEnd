package org.team5.interview_partner.entity.calendar;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.team5.interview_partner.entity.BaseEntity;
import org.team5.interview_partner.entity.user.UsersEntity;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "calendar")
public class CalendarEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)  // User 엔티티를 참조
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

    @Column(name = "date", nullable = false, length = 50)
    private String date;

    @Column(name = "memo", nullable = false, length = 100)
    private String memo;
}
