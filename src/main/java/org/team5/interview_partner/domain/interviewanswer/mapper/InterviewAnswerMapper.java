package org.team5.interview_partner.domain.interviewanswer.mapper;

import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerRequest;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerResponse;
import org.team5.interview_partner.entity.interviewanswer.InterviewAnswerEntity;

import java.util.Optional;

public class InterviewAnswerMapper {
    public static InterviewAnswerEntity toEntity(InterviewAnswerRequest interviewAnswerRequest){
        return Optional.ofNullable(interviewAnswerRequest)
                .map(it->{
                    return InterviewAnswerEntity.builder()
                            .answer(interviewAnswerRequest.getAnswer())
                            .build();
                }).orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST));
    }

    public static InterviewAnswerResponse toResponse(InterviewAnswerEntity interviewAnswerEntity){
        return Optional.ofNullable(interviewAnswerEntity)
                .map(it->{
                    return InterviewAnswerResponse.builder()
                            .answer(interviewAnswerEntity.getAnswer())
                            .id(interviewAnswerEntity.getId())
                            .feedback(interviewAnswerEntity.getFeedback())
                            .gptQuestionId(interviewAnswerEntity.getGptQuestion().getId())
                            .build();
                }).orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST));
    }
}
