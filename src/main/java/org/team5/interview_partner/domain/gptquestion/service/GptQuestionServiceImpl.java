package org.team5.interview_partner.domain.gptquestion.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.domain.gptquestion.dto.GptQuestionListResponse;
import org.team5.interview_partner.domain.gptquestion.dto.GptQuestionResponse;
import org.team5.interview_partner.domain.gptquestion.mapper.GptQuestionMapper;
import org.team5.interview_partner.domain.user.dto.CustomUserDetail;
import org.team5.interview_partner.entity.gptquestion.GptQuestionRepository;
import org.team5.interview_partner.entity.gptquestion.GptQuestionEntity;
import org.team5.interview_partner.entity.section.SectionEntity;
import org.team5.interview_partner.entity.section.SectionRepository;
import org.team5.interview_partner.entity.user.UsersEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GptQuestionServiceImpl implements GptQuestionService{
    private final GptQuestionRepository gptQuestionRepository;
    private final SectionRepository sectionRepository;
    @Override
    public GptQuestionListResponse gptQuestionList(Authentication authentication,int sectionId) {
        SectionEntity sectionEntity = verification(authentication,sectionId);

        List<GptQuestionEntity> gptQuestionEntityList = gptQuestionRepository.findAllBySection(sectionEntity);
        List<GptQuestionResponse> gptQuestionResponse = gptQuestionEntityList.stream()
                .map(GptQuestionMapper::toResponse).collect(Collectors.toList());

        GptQuestionListResponse gptQuestionListResponse = GptQuestionListResponse.builder()
                .gptQuestionList(gptQuestionResponse)
                .build();
        return gptQuestionListResponse;
    }

    @Override
    public GptQuestionResponse gptQuestion(Authentication authentication,int gptQuestionId) {

        GptQuestionEntity gptQuestionEntity = gptQuestionRepository.findById(gptQuestionId)
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST,"sectionId에 대한 데이터가 없습니다."));
        verification(authentication,gptQuestionEntity.getSection().getId());

        GptQuestionResponse gptQuestionResponse = GptQuestionMapper.toResponse(gptQuestionEntity);
        return gptQuestionResponse;
    }

    //사용자 검증
    public SectionEntity verification(Authentication authentication, int sectionId){
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        SectionEntity sectionEntity = sectionRepository.findById(sectionId)
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST, "section id에 해당하는 값이 없습니다."));
        if (!sectionEntity.getUser().getUsername().equals(userDetails.getUsername())) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "해당 사용자는 접근할 수 없습니다.");
        }
        return sectionEntity;
    }
}
