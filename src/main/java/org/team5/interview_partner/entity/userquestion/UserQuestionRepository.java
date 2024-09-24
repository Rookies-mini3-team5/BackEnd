package org.team5.interview_partner.entity.userquestion;


import org.springframework.data.jpa.repository.JpaRepository;
import org.team5.interview_partner.entity.section.SectionEntity;

import java.util.List;

public interface UserQuestionRepository extends JpaRepository<UserQuestionEntity, Integer> {
    List<UserQuestionEntity> findAllBySectionEntity(SectionEntity sectionEntity);
}
