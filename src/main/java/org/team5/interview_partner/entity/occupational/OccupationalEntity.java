package org.team5.interview_partner.entity.occupational;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team5.interview_partner.entity.BaseEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "occupational")
public class OccupationalEntity extends BaseEntity {
    @Column(name = "occupational_name", nullable = false, length = 50)
    private String occupationalName;
}
