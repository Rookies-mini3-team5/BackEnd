package org.team5.interview_partner.domain.gpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.domain.gpt.dto.GptRequest;
import org.springframework.beans.factory.annotation.Value;
import org.team5.interview_partner.domain.gpt.dto.GptResponse;
import org.team5.interview_partner.domain.gpt.dto.Message;
import org.team5.interview_partner.entity.gptquestion.GptQuestionEntity;
import org.team5.interview_partner.entity.gptquestion.GptQuestionRepository;
import org.team5.interview_partner.entity.interviewanswer.InterviewAnswerEntity;
import org.team5.interview_partner.entity.interviewanswer.InterviewAnswerRepository;
import org.team5.interview_partner.entity.job.JobEntity;
import org.team5.interview_partner.entity.job.JobRepository;
import org.team5.interview_partner.entity.section.SectionEntity;
import org.team5.interview_partner.entity.section.SectionRepository;
import org.team5.interview_partner.entity.user.UsersEntity;
import org.team5.interview_partner.entity.userquestion.UserQuestionEntity;
import org.team5.interview_partner.entity.userquestion.UserQuestionRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GptApiService {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String url;

    private final RestTemplate restTemplate;

    private final GptQuestionRepository gptQuestionRepository;
    private final UserQuestionRepository userQuestionRepository;
    private final SectionRepository sectionRepository;
    private final JobRepository jobRepository;
    private final InterviewAnswerRepository interviewAnswerRepository;

    //면접 질문 fine tuning
    public List<Message> fineTuning(int setctionId){
        //섹션 정보 가져오기
        SectionEntity sectionEntity = sectionRepository.findById(setctionId)
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST));

        //직군 및 직업 정보 가져오기
        JobEntity jobEntity = jobRepository.findById(sectionEntity.getJob().getId())
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST));

        //gpt 파인튜닝
        List<Message> messageList = new ArrayList<>();

        //직군 직무 추가
        String fineTuning = "나는 "+jobEntity.getOccupational().getOccupationalName()+" 직군에 "+jobEntity.getJobName()+" 직무로 지원을 할거야";
        String fineTuning_answer = jobEntity.getOccupational().getOccupationalName()+" 직군에 " + jobEntity.getJobName()+" 직무에 지원하시는군요. 추후 대답에 적용하겠습니다.";
        Message user = new Message("user",fineTuning);
        Message gpt = new Message("system",fineTuning_answer);
        messageList.add(user);
        messageList.add(gpt);

        //이력 및 강조점 추가
        if(!sectionEntity.getResume().isEmpty() && !sectionEntity.getEmphasize().isEmpty()){
            fineTuning = "나는 "+sectionEntity.getResume()+" 한 이력이 있고 "+sectionEntity.getEmphasize()+" 한 점을 강조하고 싶다.";
            fineTuning_answer = sectionEntity.getResume()+"한 이력과 "+ sectionEntity.getEmphasize()+" 한 점을 강조 하고 싶으시군요. 참고해서 추후 대답에 적용하겠습니다.";
        }else if(!sectionEntity.getResume().isEmpty()){
            fineTuning = "나는 "+sectionEntity.getResume()+" 한 이력이 있어";
            fineTuning_answer = sectionEntity.getResume()+"한 이력이 있으니군요 참고해서 추후 대답에 적용하겠습니다.";
        }else if(!sectionEntity.getEmphasize().isEmpty()){
            fineTuning = "나는 "+sectionEntity.getEmphasize()+" 한 점을 강조하고 싶다.";
            fineTuning_answer = sectionEntity.getEmphasize()+" 한 점을 강조 하고 싶으시군요. 참고해서 추후 대답에 적용하겠습니다.";
        }

        user = new Message("user",fineTuning);
        gpt = new Message("system",fineTuning_answer);
        messageList.add(user);
        messageList.add(gpt);


        return messageList;
    }

    //gpt 예상질문 생성
    public List<String> expectedQuestion(int setctionId){

        List<Message> messageList = fineTuning(setctionId);
        String fineTuning = "당신은 이제 면접 예상질문을 생성할 것 입니다. 면접 예상 질문과 질문에 대해 어떻게 답변해야 하는지에 대한 예시 답변 가이드를 함께 제공해 주세요" +
                "\n" +
                "    1. 질문/질문에 대한 답변 가이드/2. 질문/질문에 대한 답변 가이드/3. 질문/질문에 대한 답변 가이드/4. 질문/질문에 대한 답변 가이드/5. 질문/질문에 대한 답변 가이드/6. 질문/질문에 대한 답변 가이드  이렇게 질문 뒤에 /, 질문에 대한 답변 가이드 뒤에 /로 구분하게 만든 문자열로 만들어주세요";
        String fineTuning_answer = "네 알겠습니다.";

        Message user = new Message("user",fineTuning);
        Message gpt = new Message("system",fineTuning_answer);
        messageList.add(user);
        messageList.add(gpt);

        //예상 질문 생성
        Message userQuestioin = Message.builder()
                .role("user")
                .content("사용자는 면접 예상 질문을 요청합니다. 총 6개의 답변을 생성하세요.")
                .build();
        messageList.add(userQuestioin);

        GptRequest gptRequest = GptRequest.builder()
                .model(model)
                .messages(messageList)
                .build();
        GptResponse gptResponse = restTemplate.postForObject(url,gptRequest, GptResponse.class);
        log.info(gptResponse.getChoices().get(0).getMessage().getContent());
        List<String> question = Arrays.asList(gptResponse.getChoices().get(0).getMessage().getContent().split("/"));
        //List의 홀수 index는 예상 면접 질문 짝수 index는 답변 가이드
        return question;
    }

    //유저 질문
    public GptResponse userQeustion(String question, SectionEntity sectionEntity){

        List<UserQuestionEntity> userQuestionEntities = userQuestionRepository.findAllBySectionEntity(sectionEntity);
        List<Message> messageList = new ArrayList<>();
        userQuestionEntities.forEach(it->{
            Message user = new Message("user", it.getQuestion());
            Message gpt = new Message("system", it.getAnswer());
            messageList.add(user);
            messageList.add(gpt);
        });
        Message userQuestioin = new Message("user", question);
        messageList.add(userQuestioin);
        GptRequest gptRequest = GptRequest.builder()
                .model(model)
                .messages(messageList)
                .build();
        GptResponse gptResponse = restTemplate.postForObject(url,gptRequest, GptResponse.class);

        return gptResponse;
    }

    //gpt 예상 질문에 대한 유저 답변을 통해 gpt의 피드백 받기
    public GptResponse userAnswer(String answer, int gptQuestionId){
        //gpt 면접 질문 정보
        GptQuestionEntity gptQuestionEntity = gptQuestionRepository.findById(gptQuestionId)
                .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST));

        //이력 강조점 파인튜닝
        List<Message> messageList = fineTuning(gptQuestionEntity.getSection().getId());

        //대화내용 적용
        List<InterviewAnswerEntity> interviewAnswerEntities = interviewAnswerRepository.findAllByGptQuestion(gptQuestionEntity);
        String fineTuning = "";
        String fineTuning_answer = "";

        interviewAnswerEntities.forEach(it->{
            Message user = new Message("user",it.getAnswer());
            Message gpt = new Message("system",it.getFeedback());
            messageList.add(user);
            messageList.add(gpt);
        });


        if(interviewAnswerEntities.isEmpty()){
            fineTuning = "이제 \""+gptQuestionEntity.getQuestion()+"\"에 대해 대답을 할게 내가 대답한 후에 내가 잘 대답했는지 피드백 해줘";
            fineTuning_answer = "네 알겠습니다. 면접 질문에 대한 대답을 피드백 해드리겠습니다.";
            Message user = new Message("user",fineTuning);
            Message gpt = new Message("system",fineTuning_answer);
            messageList.add(user);
            messageList.add(gpt);
        }

        Message userAnswer = new Message("user", answer);
        messageList.add(userAnswer);
        GptRequest gptRequest = GptRequest.builder()
                .model(model)
                .messages(messageList)
                .build();
        GptResponse gptResponse = restTemplate.postForObject(url,gptRequest, GptResponse.class);

        return gptResponse;


    }
}
