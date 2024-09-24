package org.team5.interview_partner.domain.gptquestion.mapper;

import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.domain.gptquestion.dto.GptQuestionResponse;
import org.team5.interview_partner.entity.gptquestion.GptQuestionEntity;

import java.util.Optional;

public class GptQuestionMapper {

    public static GptQuestionResponse toResponse(GptQuestionEntity gptQuestionEntity){
        return Optional.ofNullable(gptQuestionEntity)
                .map(it->{
                    return GptQuestionResponse.builder()
                            .question(gptQuestionEntity.getQuestion())
                            .id(gptQuestionEntity.getId())
                            .answerGuide(gptQuestionEntity.getAnswerGuide())
                            .sectionId(gptQuestionEntity.getSection().getId())
                            .build();
                }).orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST));
    }
}
