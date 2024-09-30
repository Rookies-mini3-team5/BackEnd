package org.team5.interview_partner.domain.userquestion.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.domain.gpt.GptApiService;
import org.team5.interview_partner.domain.gpt.dto.GptResponse;
import org.team5.interview_partner.domain.user.dto.CustomUserDetail;
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
@Log4j2
public class UserQuestionServiceImpl implements UserQuestionService {
    private final UserQuestionRepository userQuestionRepository;
    private final SectionRepository sectionRepository;
    private final GptApiService gptApiService;

    //유저 질문
    @Override
    public UserQuestionResponse addUserQuestion(int sectionId, UserQuestionRequest userQuestionRequest,Authentication authentication) {
        UserQuestionEntity userQuestionEntity = UserQuestionMapper.toEntity(userQuestionRequest);
        var sectionEntity = sectionRepository.findById(sectionId).
                orElseThrow(()->new ApiException(ErrorCode.NULL_POINT,"section id에 해당하는 값이 없습니다."));
        verification(authentication,sectionEntity);
        GptResponse gptResponse = gptApiService.userQuestion(userQuestionRequest.getQuestion(),sectionEntity);
        userQuestionEntity.setSectionEntity(sectionEntity);
        String question = gptResponse.getChoices().get(0).getMessage().getContent();
        question = question.replace("\n","").replace("--","").replace("\\","").replace("/","");
        userQuestionEntity.setAnswer(question);
        UserQuestionEntity newUserQuestionEntity = userQuestionRepository.save(userQuestionEntity);
        UserQuestionResponse userQuestionResponse = UserQuestionMapper.toResponse(newUserQuestionEntity);
        return userQuestionResponse;
    }

    //질문 답변 목록 조회
    @Override
    public UserQuestionListResponse userQuestionList(int sectionId,Authentication authentication) {
        SectionEntity sectionEntity = sectionRepository.findById(sectionId)
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST,"section id에 해당하는 값이 없습니다."));
        verification(authentication,sectionEntity);
        List<UserQuestionEntity> userQuestionEntityList = userQuestionRepository.findAllBySectionEntity(sectionEntity);

        List<UserQuestionResponse> userQuestionResponseList = userQuestionEntityList.stream().
                map(UserQuestionMapper::toResponse).collect(Collectors.toList());

        UserQuestionListResponse userQuestionListResponse = UserQuestionListResponse.builder()
                .userQuestionList(userQuestionResponseList)
                .build();
        return userQuestionListResponse;
    }

    @Override
    public UserQuestionResponse userQuestion(int userQuestionId,Authentication authentication) {
        UserQuestionEntity userQuestionEntity = userQuestionRepository.findById(userQuestionId)
                .orElseThrow(()-> new ApiException(ErrorCode.BAD_REQUEST,"user question id에 해당하는 값이 없습니다."));
        verification(authentication,userQuestionEntity.getSectionEntity());
        UserQuestionResponse userQuestionResponse = UserQuestionMapper.toResponse(userQuestionEntity);
        return userQuestionResponse;
    }

    //사용자 검증
    public void verification(Authentication authentication,SectionEntity sectionEntity){
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        if (!sectionEntity.getUser().getUsername().equals(userDetails.getUsername())) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "해당 사용자는 접근할 수 없습니다.");
        }
    }


}
