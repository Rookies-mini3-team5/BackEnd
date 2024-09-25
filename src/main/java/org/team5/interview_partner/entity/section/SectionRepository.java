package org.team5.interview_partner.entity.section;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team5.interview_partner.entity.user.UsersEntity;

import java.util.List;

public interface SectionRepository extends JpaRepository<SectionEntity,Integer> {

    List<SectionEntity> findByUser(UsersEntity user);
}
