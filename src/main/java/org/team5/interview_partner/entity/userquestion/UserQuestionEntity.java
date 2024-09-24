package org.team5.interview_partner.entity.userquestion;

import jakarta.persistence.*;
import lombok.*;
import org.team5.interview_partner.entity.BaseEntity;
import org.team5.interview_partner.entity.section.SectionEntity;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_question")
public class UserQuestionEntity extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    private SectionEntity sectionEntity;

    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(name = "answer", columnDefinition = "TEXT")
    private String answer;
}
