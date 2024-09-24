package org.team5.interview_partner.entity.interviewanswer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team5.interview_partner.entity.BaseEntity;
import org.team5.interview_partner.entity.gptquestion.GptQuestionEntity;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "interview_answer")
public class InterviewAnswerEntity extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "gpt_question_id", referencedColumnName = "id")
    private GptQuestionEntity gptQuestion;

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;
}
