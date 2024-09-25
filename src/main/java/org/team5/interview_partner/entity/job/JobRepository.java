package org.team5.interview_partner.entity.job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team5.interview_partner.entity.occupational.OccupationalEntity;

import java.util.List;

public interface JobRepository extends JpaRepository<JobEntity,Integer> {
    List<JobEntity> findAllByOccupational(OccupationalEntity occupational);
}
