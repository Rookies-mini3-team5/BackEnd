package org.team5.interview_partner.entity.section;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team5.interview_partner.entity.BaseEntity;

import java.time.LocalDateTime;
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "section")
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

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
