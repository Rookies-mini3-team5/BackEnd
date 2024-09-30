package org.team5.interview_partner.entity.section;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.team5.interview_partner.entity.BaseEntity;
import org.team5.interview_partner.entity.job.JobEntity;
import org.team5.interview_partner.entity.occupational.OccupationalEntity;
import org.team5.interview_partner.entity.user.UsersEntity;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "section")
@EntityListeners(AuditingEntityListener.class)
public class SectionEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)  // User 엔티티를 참조
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

    @ManyToOne(fetch = FetchType.LAZY)  // Job 엔티티를 참조
    @JoinColumn(name = "job_id", nullable = false)
    private JobEntity job;

    @ManyToOne(fetch = FetchType.LAZY)  // Occupational 엔티티를 참조
    @JoinColumn(name = "occupational_id", nullable = false)
    private OccupationalEntity occupational;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "resume", columnDefinition = "TEXT")
    private String resume;

    @Column(name = "emphasize", columnDefinition = "TEXT")
    private String emphasize;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
