package org.team5.interview_partner.entity.section;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.team5.interview_partner.entity.BaseEntity;

import java.time.LocalDateTime;
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "section")
@EntityListeners(AuditingEntityListener.class)
public class SectionEntity extends BaseEntity {
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "job_id", nullable = false)
    private Integer jobId;

    @Column(name = "occupational_id", nullable = false)
    private Integer occupationalId;

    @Column(name = "name", nullable = false, length = 100)
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
