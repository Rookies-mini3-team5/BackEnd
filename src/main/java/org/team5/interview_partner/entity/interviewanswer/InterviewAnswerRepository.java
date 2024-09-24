package org.team5.interview_partner.entity.interviewanswer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team5.interview_partner.entity.gptquestion.GptQuestionEntity;

import java.util.List;

public interface InterviewAnswerRepository extends JpaRepository<InterviewAnswerEntity,Integer> {
    List<InterviewAnswerEntity> findAllByGptQuestion(GptQuestionEntity gptQuestionEntity);
}
