package org.team5.interview_partner.domain.gptquestion.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.domain.gptquestion.dto.GptQuestionListResponse;
import org.team5.interview_partner.domain.gptquestion.dto.GptQuestionResponse;
import org.team5.interview_partner.domain.gptquestion.mapper.GptQuestionMapper;
import org.team5.interview_partner.entity.gptquestion.GptQuestionRepository;
import org.team5.interview_partner.entity.gptquestion.GptQuestionEntity;
import org.team5.interview_partner.entity.section.SectionEntity;
import org.team5.interview_partner.entity.section.SectionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GptQuestionServiceImpl implements GptQuestionService{
    private final GptQuestionRepository gptQeustionRepository;
    private final SectionRepository sectionRepository;
    @Override
    public GptQuestionListResponse gptQuestionList(int sectionId) {
        SectionEntity sectionEntity = sectionRepository.findById(sectionId)
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST, "section id에 해당하는 값이 없습니다."));

        List<GptQuestionEntity> gptQuestionEntityList = gptQeustionRepository.findAllBySection(sectionEntity);
        List<GptQuestionResponse> gptQuestionResponse = gptQuestionEntityList.stream()
                .map(it-> GptQuestionMapper.toResponse(it)).collect(Collectors.toList());

        GptQuestionListResponse gptQuestionListResponse = GptQuestionListResponse.builder()
                .gptQuestionList(gptQuestionResponse)
                .build();
        return gptQuestionListResponse;
    }

    @Override
    public GptQuestionResponse gptQuestion(int sectionId) {
        GptQuestionEntity gptQuestionEntity = gptQeustionRepository.findById(sectionId)
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST,"sectionId에 대한 데이터가 없습니다."));
        GptQuestionResponse gptQuestionResponse = GptQuestionMapper.toResponse(gptQuestionEntity);
        return gptQuestionResponse;
    }
}
