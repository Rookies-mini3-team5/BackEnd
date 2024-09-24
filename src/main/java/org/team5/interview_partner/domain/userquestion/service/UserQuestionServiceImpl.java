package org.team5.interview_partner.domain.userquestion.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.domain.userquestion.dto.UserQuestionListResponse;
import org.team5.interview_partner.domain.userquestion.dto.UserQuestionRequest;
import org.team5.interview_partner.domain.userquestion.dto.UserQuestionResponse;
import org.team5.interview_partner.domain.userquestion.mapper.UserQuestionMapper;
import org.team5.interview_partner.entity.section.SectionEntity;
import org.team5.interview_partner.entity.section.SectionRepository;
import org.team5.interview_partner.entity.userquestion.UserQuestionEntity;
import org.team5.interview_partner.entity.userquestion.UserQuestionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserQuestionServiceImpl implements UserQuestionService {
    private final UserQuestionRepository userQuestionRepository;
    private final SectionRepository sectionRepository;

    //유저 질문
    @Override
    public void addUserQuestion(int sectionId, UserQuestionRequest userQuestionRequest) {
        UserQuestionEntity userQuestionEntity = UserQuestionMapper.toEntity(userQuestionRequest);
        var sectionEntity = sectionRepository.findById(sectionId).
                orElseThrow(()->new ApiException(ErrorCode.NULL_POINT,"section id에 해당하는 값이 없습니다."));
        //TODO GPT API 활용해서 대답 받기
        userQuestionEntity.setSectionEntity(sectionEntity);
        userQuestionRepository.save(userQuestionEntity);

    }

    //질문 답변 목록 조회
    @Override
    public UserQuestionListResponse userQuestionList(int sectionId) {
        SectionEntity sectionEntity = sectionRepository.findById(sectionId)
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST,"section id에 해당하는 값이 없습니다."));

        List<UserQuestionEntity> userQuestionEntityList = userQuestionRepository.findAllBySectionEntity(sectionEntity);

        List<UserQuestionResponse> userQuestionResponseList = userQuestionEntityList.stream().
                map(UserQuestionMapper::toResponse).collect(Collectors.toList());

        UserQuestionListResponse userQuestionListResponse = UserQuestionListResponse.builder()
                .userQuestionList(userQuestionResponseList)
                .build();
        return userQuestionListResponse;
    }

    @Override
    public UserQuestionResponse userQeustion(int userQuestionId) {
        UserQuestionEntity userQuestionEntity = userQuestionRepository.findById(userQuestionId)
                .orElseThrow(()-> new ApiException(ErrorCode.BAD_REQUEST,"user question id에 해당하는 값이 없습니다."));
        UserQuestionResponse userQuestionResponse = UserQuestionMapper.toResponse(userQuestionEntity);
        return userQuestionResponse;
    }




}
