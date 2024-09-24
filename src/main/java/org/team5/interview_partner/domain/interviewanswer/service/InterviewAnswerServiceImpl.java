package org.team5.interview_partner.domain.interviewanswer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerListResponse;
import org.team5.interview_partner.domain.interviewanswer.dto.InterviewAnswerRequest;
import org.team5.interview_partner.domain.interviewanswer.mapper.InterviewAnswerMapper;
import org.team5.interview_partner.entity.gptquestion.GptQeustionRepository;
import org.team5.interview_partner.entity.gptquestion.GptQuestionEntity;
import org.team5.interview_partner.entity.interviewanswer.InterviewAnswerEntity;
import org.team5.interview_partner.entity.interviewanswer.InterviewAnswerRepository;

@Service
@RequiredArgsConstructor
public class InterviewAnswerServiceImpl implements InterviewAnswerService{
    private final InterviewAnswerRepository interviewAnswerRepository;
    private final GptQeustionRepository gptQeustionRepository;
    @Override
    public void addInterviewAnswer(int gptQuestionId, InterviewAnswerRequest interviewAnswerRequest) {
        GptQuestionEntity gptQuestionEntity = gptQeustionRepository.findById(gptQuestionId)
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST,"gpt question id에 해당한느 값이 없습니다."));
        //TODO GPT 대답 넣기
        InterviewAnswerEntity interviewAnswerEntity = InterviewAnswerMapper.toEntity(interviewAnswerRequest);

        interviewAnswerEntity.setGptQuestion(gptQuestionEntity);

        interviewAnswerRepository.save(interviewAnswerEntity);

    }

    @Override
    public InterviewAnswerListResponse interviewAnswerList(int gptQuestionId) {
        return null;
    }
}
