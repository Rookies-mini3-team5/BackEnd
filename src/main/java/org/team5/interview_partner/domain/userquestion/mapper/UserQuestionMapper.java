package org.team5.interview_partner.domain.userquestion.mapper;

import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.domain.userquestion.dto.UserQuestionRequest;
import org.team5.interview_partner.domain.userquestion.dto.UserQuestionResponse;
import org.team5.interview_partner.entity.userquestion.UserQuestionEntity;

import java.util.Optional;

public class UserQuestionMapper {
    //UserQuestionRequest to UserQuestionEntity
    public static UserQuestionEntity toEntity(UserQuestionRequest userQuestionRequest){
        return Optional.ofNullable(userQuestionRequest)
                .map(it->{
                    return UserQuestionEntity.builder()
                            .question(userQuestionRequest.getQuestion())
                            .build();
                }).orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST));
    }

    //UserQuestionEntity to UserQuestionResponse
    public static UserQuestionResponse toResponse(UserQuestionEntity userQuestionEntity){
        return Optional.ofNullable(userQuestionEntity)
                .map(it->{
                    return UserQuestionResponse.builder()
                            .id(userQuestionEntity.getId())
                            .answer(userQuestionEntity.getAnswer())
                            .question(userQuestionEntity.getQuestion())
                            .sectionId(userQuestionEntity.getSectionEntity().getId())
                            .build();
                }).orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST));
    }


}
