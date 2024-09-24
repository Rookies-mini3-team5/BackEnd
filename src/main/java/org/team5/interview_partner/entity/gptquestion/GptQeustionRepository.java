package org.team5.interview_partner.entity.gptquestion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team5.interview_partner.entity.section.SectionEntity;

import java.util.List;

public interface GptQeustionRepository extends JpaRepository<GptQuestionEntity, Integer> {
    List<GptQuestionEntity> findAllBySection(SectionEntity sectionEntity);
}
