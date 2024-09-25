package org.team5.interview_partner.domain.interviewanswer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.domain.gpt.GptApiService;
import org.team5.interview_partner.domain.gpt.dto.GptResponse;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerListResponse;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerRequest;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerResponse;
import org.team5.interview_partner.domain.interviewanswer.mapper.InterviewAnswerMapper;
import org.team5.interview_partner.entity.gptquestion.GptQuestionRepository;
import org.team5.interview_partner.entity.gptquestion.GptQuestionEntity;
import org.team5.interview_partner.entity.interviewanswer.InterviewAnswerEntity;
import org.team5.interview_partner.entity.interviewanswer.InterviewAnswerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewAnswerServiceImpl implements InterviewAnswerService{
    private final InterviewAnswerRepository interviewAnswerRepository;
    private final GptQuestionRepository gptQeustionRepository;
    private final GptApiService gptApiService;
    @Override
    public void addInterviewAnswer(int gptQuestionId, InterviewAnswerRequest interviewAnswerRequest) {
        GptQuestionEntity gptQuestionEntity = gptQeustionRepository.findById(gptQuestionId)
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST,"gpt question id에 해당한는 값이 없습니다."));
        InterviewAnswerEntity interviewAnswerEntity = InterviewAnswerMapper.toEntity(interviewAnswerRequest);

        //GPT
        GptResponse gptResponse = gptApiService.userAnswer(interviewAnswerRequest.getAnswer(),gptQuestionId);
        String feedback = gptResponse.getChoices().get(0).getMessage().getContent();
        interviewAnswerEntity.setFeedback(feedback);
        interviewAnswerEntity.setGptQuestion(gptQuestionEntity);

        interviewAnswerRepository.save(interviewAnswerEntity);

    }

    @Override
    public InterviewAnswerListResponse interviewAnswerList(int gptQuestionId) {
        GptQuestionEntity gptQuestionEntity = gptQeustionRepository.findById(gptQuestionId)
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST,"gpt question id에 해당한느 값이 없습니다."));

        List<InterviewAnswerEntity> interviewAnswerEntityList = interviewAnswerRepository.findAllByGptQuestion(gptQuestionEntity);

        List<InterviewAnswerResponse> interviewAnswerResponse = interviewAnswerEntityList.stream()
                .map(InterviewAnswerMapper::toResponse).collect(Collectors.toList());

        InterviewAnswerListResponse interviewAnswerListResponse = InterviewAnswerListResponse.builder()
                .interviewAnswerList(interviewAnswerResponse)
                .build();

        return interviewAnswerListResponse;
    }
}
