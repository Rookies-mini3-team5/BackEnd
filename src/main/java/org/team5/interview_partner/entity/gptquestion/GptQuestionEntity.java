package org.team5.interview_partner.entity.gptquestion;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team5.interview_partner.entity.BaseEntity;
import org.team5.interview_partner.entity.section.SectionEntity;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gpt_question")
public class GptQuestionEntity extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    private SectionEntity section;

    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(name = "answer_guide", nullable = false, columnDefinition = "TEXT")
    private String answerGuide;
}
