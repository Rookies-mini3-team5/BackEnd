package org.team5.interview_partner.entity.job;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team5.interview_partner.entity.BaseEntity;
import org.team5.interview_partner.entity.occupational.OccupationalEntity;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job")
public class JobEntity extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "occupational_id", referencedColumnName = "id")
    private OccupationalEntity occupational;

    @Column(name = "job_name", nullable = false, length = 50)
    private String jobName;
}
