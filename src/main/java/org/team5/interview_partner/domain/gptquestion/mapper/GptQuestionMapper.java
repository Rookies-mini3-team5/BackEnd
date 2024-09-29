package org.team5.interview_partner.domain.gptquestion.mapper;

import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.domain.gptquestion.dto.GptQuestionResponse;
import org.team5.interview_partner.entity.gptquestion.GptQuestionEntity;
import org.team5.interview_partner.entity.section.SectionEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GptQuestionMapper {

    public static GptQuestionResponse toResponse(GptQuestionEntity gptQuestionEntity){
        return Optional.ofNullable(gptQuestionEntity)
                .map(it->{
                    // getAnswerGuide() 결과물을 ",,"로 split 후 리스트로 변환
                    List<String> answerGuideList = Arrays.asList(
                            Optional.ofNullable(gptQuestionEntity.getAnswerGuide())
                                    .orElse("")
                                    .split("@")
                    );

                    return GptQuestionResponse.builder()
                            .question(gptQuestionEntity.getQuestion())
                            .id(gptQuestionEntity.getId())
                            .answerGuide(answerGuideList)
                            .sectionId(gptQuestionEntity.getSection().getId())
                            .build();
                }).orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST));
    }

    public static GptQuestionEntity toEntity(SectionEntity sectionEntity, String question, String answerGuide){
        return Optional.ofNullable(sectionEntity)
                .map(it->{
                    return GptQuestionEntity.builder()
                            .section(sectionEntity)
                            .question(question)
                            .answerGuide(answerGuide)
                            .build();
                }).orElseThrow(()-> new ApiException(ErrorCode.BAD_REQUEST));
    }
}
