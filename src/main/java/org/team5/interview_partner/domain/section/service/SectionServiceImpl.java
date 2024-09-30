package org.team5.interview_partner.domain.section.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.common.utils.JwtUtils;
import org.team5.interview_partner.domain.gpt.GptApiService;
import org.team5.interview_partner.domain.gptquestion.mapper.GptQuestionMapper;
import org.team5.interview_partner.domain.section.dto.*;
import org.team5.interview_partner.domain.section.mapper.SectionMapper;
import org.team5.interview_partner.entity.gptquestion.GptQuestionEntity;
import org.team5.interview_partner.entity.gptquestion.GptQuestionRepository;
import org.team5.interview_partner.entity.interviewanswer.InterviewAnswerRepository;
import org.team5.interview_partner.entity.job.JobEntity;
import org.team5.interview_partner.entity.job.JobRepository;
import org.team5.interview_partner.entity.occupational.OccupationalEntity;
import org.team5.interview_partner.entity.occupational.OccupationalRepository;
import org.team5.interview_partner.entity.section.SectionEntity;
import org.team5.interview_partner.entity.section.SectionRepository;
import org.team5.interview_partner.entity.user.UserRepository;
import org.team5.interview_partner.entity.user.UsersEntity;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final SectionRepository sectionRepository;
    private final JobRepository jobRepository;
    private final OccupationalRepository occupationalRepository;
    private final GptApiService gptApiService;
    private final GptQuestionRepository gptQuestionRepository;
    private final InterviewAnswerRepository interviewAnswerRepository;

    // DateTimeFormatter를 통해 YYMMDD 형식으로 포맷
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");

    @Override
    public List<SectionInfoResponse> sectionInfoList(String authorization) {
        // 유저 정보 추출
        String token = authorization.substring(7);
        String username = jwtUtils.getSubjectFromToken(token);
        UsersEntity user = userRepository.findByUsername(username);

        List<SectionEntity> sections = sectionRepository.findByUser(user);

        // SectionEntity를 SectionInfoResponse로 변환하여 반환
        return sections.stream().map(SectionMapper::toSectionInfoResponse).collect(Collectors.toList());
    }

    @Override
    public AddSectionResponse addSection(String authorization, AddSectionRequest addSectionRequest) {
        // 유저 정보 추출
        String token = authorization.substring(7);
        String username = jwtUtils.getSubjectFromToken(token);
        UsersEntity user = userRepository.findByUsername(username);

        String errorMsg = "";

        Optional<JobEntity> job = jobRepository.findById(addSectionRequest.getJob());
        Optional<OccupationalEntity> occupational = occupationalRepository.findById(addSectionRequest.getOccupational());
        if (job.isPresent() && occupational.isPresent()) {
            JobEntity jobEntity = job.get();
            OccupationalEntity occupationalEntity = occupational.get();

            // 기본 name 설정: "occupationalName/jobName"
            String baseName = occupationalEntity.getOccupationalName() + "-" + jobEntity.getJobName();
            String sectionName = baseName;

            // 중복된 이름이 있는지 확인
            List<SectionEntity> existingSections = sectionRepository.findAllByNameStartingWith(baseName);

            if (!existingSections.isEmpty()) {
                int maxSuffix = 1;
                for (SectionEntity existingSection : existingSections) {
                    String existingName = existingSection.getName();
                    // 숫자가 붙어 있는지 확인
                    if (existingName.length() > baseName.length()) {
                        try {
                            // 숫자를 추출해서 비교
                            String suffix = existingName.substring(baseName.length());
                            int suffixValue = Integer.parseInt(suffix);
                            if (suffixValue > maxSuffix) {
                                maxSuffix = suffixValue;
                            }
                        } catch (NumberFormatException ignored) {
                            // 숫자가 아닌 경우는 무시
                        }
                    }
                }
                // 숫자를 1 증가시켜 새로운 이름 설정
                sectionName = baseName + (maxSuffix + 1);
            }

            SectionEntity sectionEntity = SectionMapper.toEntity(user, jobEntity, occupationalEntity, sectionName);
            SectionEntity section = sectionRepository.save(sectionEntity);

            return AddSectionResponse.builder()
                    .id(section.getId())
                    .build();
        } else if (job.isEmpty()) {
            errorMsg = "존재하지 않는 직업입니다.";
        } else {
            errorMsg = "존재하지 않는 직군입니다.";
        }

        throw new ApiException(ErrorCode.BAD_REQUEST, errorMsg);
    }

    @Override
    public List<AddSectionResumeResponse> addSectionResume(String authorization, AddSectionResumeRequest addSectionResumeRequest, int sectionId) {
        // 유저 정보 추출
        String token = authorization.substring(7);
        String username = jwtUtils.getSubjectFromToken(token);
        UsersEntity user = userRepository.findByUsername(username);

        Optional<SectionEntity> section = sectionRepository.findById(sectionId);
        if (section.isPresent()) {
            SectionEntity sectionEntity = section.get();
            if (user != sectionEntity.getUser()) {
                throw new ApiException(ErrorCode.UNAUTHORIZED, "권한이 없습니다.");
            }

            if (addSectionResumeRequest.getResume() != null) {
                sectionEntity.setResume(addSectionResumeRequest.getResume());
            }
            if (addSectionResumeRequest.getEmphasize() != null) {
                sectionEntity.setEmphasize(addSectionResumeRequest.getEmphasize());
            }

            SectionEntity savedSection = sectionRepository.save(sectionEntity);

            List<String> questionList = gptApiService.expectedQuestion(sectionId);

            // Initialize the list to hold the AddSectionResumeResponse objects
            List<AddSectionResumeResponse> addSectionResume = new ArrayList<>();

            // Iterate over the questionList and map the questions and answer guides
            for (int i = 0; i < questionList.size(); i += 2) {
                String expectedQuestion = questionList.get(i).trim(); // Get the question
                String answerGuide = "";

                // Check if the answer guide exists at the next index
                if (i + 1 < questionList.size()) {
                    answerGuide = questionList.get(i + 1).trim(); // Get the answer guide
                }

                GptQuestionEntity gptQuestionEntity = GptQuestionMapper.toEntity(savedSection, expectedQuestion, answerGuide);
                GptQuestionEntity savedGptQuestionEntity = gptQuestionRepository.save(gptQuestionEntity);
                List<String> answerGuideList = Arrays.asList(answerGuide.split("@"));

                // Create an AddSectionResumeResponse object and add it to the list
                AddSectionResumeResponse response = AddSectionResumeResponse.builder()
                        .questionId(savedGptQuestionEntity.getId())
                        .expectedQuestion(expectedQuestion)
                        .answerGuide(answerGuideList)
                        .build();

                addSectionResume.add(response);
            }

            return addSectionResume;
        } else {
            throw new ApiException(ErrorCode.BAD_REQUEST, "유효하지 않은 sectionId");
        }
    }

    @Override
    public void deleteSection(String authorization, int sectionId) {
        // 유저 정보 추출
        String token = authorization.substring(7);
        String username = jwtUtils.getSubjectFromToken(token);
        UsersEntity user = userRepository.findByUsername(username);

        Optional<SectionEntity> section = sectionRepository.findById(sectionId);
        if (section.isPresent()) {
            SectionEntity sectionEntity = section.get();
            if (user != sectionEntity.getUser()) {
                throw new ApiException(ErrorCode.UNAUTHORIZED, "권한이 없습니다.");
            }
            sectionRepository.delete(sectionEntity);
        } else {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }
    }

    @Override
    public GetSectionInfoResponse getSectionInfo(String authorization, int sectionId) {
        // 유저 정보 추출
        String token = authorization.substring(7);
        String username = jwtUtils.getSubjectFromToken(token);
        UsersEntity user = userRepository.findByUsername(username);

        Optional<SectionEntity> section = sectionRepository.findById(sectionId);
        if (section.isPresent()) {
            SectionEntity sectionEntity = section.get();
            if (user != sectionEntity.getUser()) {
                throw new ApiException(ErrorCode.UNAUTHORIZED, "권한이 없습니다.");
            }

            return GetSectionInfoResponse.builder()
                    .sectionId(sectionId)
                    .sectionName(sectionEntity.getName())
                    .occupational(sectionEntity.getOccupational().getOccupationalName())
                    .job(sectionEntity.getJob().getJobName())
                    .resume(sectionEntity.getResume())
                    .emphasize(sectionEntity.getEmphasize())
                    .build();
        } else {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }
    }

    @Override
    public void updateSection(String authorization, int sectionId, UpdateSectionRequest updateSectionRequest) {
        // Extract user information
        String token = authorization.substring(7);
        String username = jwtUtils.getSubjectFromToken(token);
        UsersEntity user = userRepository.findByUsername(username);

        // Fetch the section
        Optional<SectionEntity> section = sectionRepository.findById(sectionId);
        if (section.isPresent()) {
            SectionEntity sectionEntity = section.get();

            // Check if the user owns the section
            if (!user.equals(sectionEntity.getUser())) {
                throw new ApiException(ErrorCode.UNAUTHORIZED, "권한이 없습니다.");
            }

            boolean resumeChanged = false;
            boolean emphasizeChanged = false;

            // Check for changes in 'resume'
            if (updateSectionRequest.getResume() != null &&
                    !updateSectionRequest.getResume().equals(sectionEntity.getResume())) {
                sectionEntity.setResume(updateSectionRequest.getResume());
                resumeChanged = true;
            }

            // Check for changes in 'emphasize'
            if (updateSectionRequest.getEmphasize() != null &&
                    !updateSectionRequest.getEmphasize().equals(sectionEntity.getEmphasize())) {
                sectionEntity.setEmphasize(updateSectionRequest.getEmphasize());
                emphasizeChanged = true;
            }

            // Update 'name' if provided
            if (updateSectionRequest.getName() != null) {
                sectionEntity.setName(updateSectionRequest.getName());
            }

            // Save the updated section
            SectionEntity savedSection = sectionRepository.save(sectionEntity);

            // Handle 'resume' changes
            if (resumeChanged) {
                // Delete all existing GPT questions associated with the section
                interviewAnswerRepository.deleteByGptQuestion_Section_Id(sectionId);
                gptQuestionRepository.deleteBySectionId(sectionId);

                // Generate new GPT questions and answer guides
                List<String> questionList = gptApiService.expectedQuestion(sectionId);

                // Save the new GPT questions
                for (int i = 0; i < questionList.size(); i += 2) {
                    String expectedQuestion = questionList.get(i).trim(); // Get the question
                    String answerGuide = "";

                    // Check if the answer guide exists at the next index
                    if (i + 1 < questionList.size()) {
                        answerGuide = questionList.get(i + 1).trim(); // Get the answer guide
                    }

                    GptQuestionEntity gptQuestionEntity = GptQuestionMapper.toEntity(sectionEntity, expectedQuestion, answerGuide);
                    gptQuestionRepository.save(gptQuestionEntity);
                }
            } else if (emphasizeChanged) {
                // Handle 'emphasize' changes
                // Fetch existing GPT questions
                List<GptQuestionEntity> gptQuestions = gptQuestionRepository.findAllBySection(savedSection);

                // Extract the questions
                List<String> questions = gptQuestions.stream()
                        .map(GptQuestionEntity::getQuestion)
                        .collect(Collectors.toList());

                // Regenerate answer guides based on new 'emphasize'
                List<String> newAnswerGuides = gptApiService.regenerateAnswerGuides(sectionId, questions);

                // Update GPT questions with new answer guides
                for (int i = 0; i < gptQuestions.size(); i++) {
                    GptQuestionEntity gptQuestion = gptQuestions.get(i);
                    String newAnswerGuide = newAnswerGuides.get(i);
                    gptQuestion.setAnswerGuide(newAnswerGuide);
                    gptQuestionRepository.save(gptQuestion);
                }
            }

        } else {
            throw new ApiException(ErrorCode.BAD_REQUEST, "유효하지 않은 sectionId");
        }
    }
}
