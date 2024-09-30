package org.team5.interview_partner.domain.interviewanswer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.domain.gpt.GptApiService;
import org.team5.interview_partner.domain.gpt.dto.GptResponse;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerListResponse;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerRequest;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerResponse;
import org.team5.interview_partner.domain.interviewanswer.mapper.InterviewAnswerMapper;
import org.team5.interview_partner.domain.user.dto.CustomUserDetail;
import org.team5.interview_partner.entity.gptquestion.GptQuestionRepository;
import org.team5.interview_partner.entity.gptquestion.GptQuestionEntity;
import org.team5.interview_partner.entity.interviewanswer.InterviewAnswerEntity;
import org.team5.interview_partner.entity.interviewanswer.InterviewAnswerRepository;
import org.team5.interview_partner.entity.section.SectionEntity;
import org.team5.interview_partner.entity.section.SectionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewAnswerServiceImpl implements InterviewAnswerService{
    private final SectionRepository sectionRepository;
    private final InterviewAnswerRepository interviewAnswerRepository;
    private final GptQuestionRepository gptQuestionRepository;
    private final GptApiService gptApiService;
    @Override
    public void addInterviewAnswer(int gptQuestionId, InterviewAnswerRequest interviewAnswerRequest,Authentication authentication) {
        GptQuestionEntity gptQuestionEntity = gptQuestionRepository.findById(gptQuestionId)
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST,"gpt question id에 해당하는 값이 없습니다."));
        verification(authentication,gptQuestionEntity.getSection());
        InterviewAnswerEntity interviewAnswerEntity = InterviewAnswerMapper.toEntity(interviewAnswerRequest);

        //GPT
        GptResponse gptResponse = gptApiService.userAnswer(interviewAnswerRequest.getAnswer(),gptQuestionId);
        String feedback = gptResponse.getChoices().get(0).getMessage().getContent();
        interviewAnswerEntity.setFeedback(feedback);
        interviewAnswerEntity.setGptQuestion(gptQuestionEntity);

        interviewAnswerRepository.save(interviewAnswerEntity);
    }

    @Override
    public InterviewAnswerListResponse interviewAnswerList(int gptQuestionId,Authentication authentication) {
        GptQuestionEntity gptQuestionEntity = gptQuestionRepository.findById(gptQuestionId)
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST,"gpt question id에 해당하는 값이 없습니다."));
        verification(authentication,gptQuestionEntity.getSection());
        List<InterviewAnswerEntity> interviewAnswerEntityList = interviewAnswerRepository.findAllByGptQuestion(gptQuestionEntity);

        List<InterviewAnswerResponse> interviewAnswerResponse = interviewAnswerEntityList.stream()
                .map(InterviewAnswerMapper::toResponse).collect(Collectors.toList());

        InterviewAnswerListResponse interviewAnswerListResponse = InterviewAnswerListResponse.builder()
                .interviewAnswerList(interviewAnswerResponse)
                .build();

        return interviewAnswerListResponse;
    }

    @Override
    public InterviewAnswerResponse interviewAnswer(int interviewAnswerId,Authentication authentication) {

        InterviewAnswerEntity interviewAnswerEntity = interviewAnswerRepository.findById(interviewAnswerId)
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST,"interviewAnswerId에 해당하는 데이터가 없습니다."));
        verification(authentication,interviewAnswerEntity.getGptQuestion().getSection());

        InterviewAnswerResponse interviewAnswerResponse = InterviewAnswerMapper.toResponse(interviewAnswerEntity);
        return interviewAnswerResponse;
    }

    //사용자 검증
    public void verification(Authentication authentication, SectionEntity sectionEntity){
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        if (!sectionEntity.getUser().getUsername().equals(userDetails.getUsername())) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "해당 사용자는 접근할 수 없습니다.");
        }
    }
}
