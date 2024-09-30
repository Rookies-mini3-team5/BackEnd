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
import org.team5.interview_partner.entity.userquestion.UserQuestionEntity;
import org.team5.interview_partner.entity.userquestion.UserQuestionRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public List<Message> fineTuning(int sectionId) {
        //섹션 정보 가져오기
        SectionEntity sectionEntity = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST));

        //직군 및 직업 정보 가져오기
        JobEntity jobEntity = jobRepository.findById(sectionEntity.getJob().getId())
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST));

        //gpt 파인튜닝
        List<Message> messageList = new ArrayList<>();

        //직군 직무 추가
        String fineTuning = "나는 " + jobEntity.getOccupational().getOccupationalName() + " 직군에 " + jobEntity.getJobName() + " 직무로 지원을 할거야";
        String fineTuning_answer = jobEntity.getOccupational().getOccupationalName() + " 직군에 " + jobEntity.getJobName() + " 직무에 지원하시는군요. 추후 대답에 적용하겠습니다.";
        Message user = new Message("user", fineTuning);
        Message gpt = new Message("system", fineTuning_answer);
        messageList.add(user);
        messageList.add(gpt);

        //이력 및 강조점 추가
        if (sectionEntity.getResume() != null && sectionEntity.getEmphasize() != null) {
            fineTuning = "나는 " + sectionEntity.getResume() + " 한 이력이 있고 " + sectionEntity.getEmphasize() + " 한 점을 강조하고 싶다.";
            fineTuning_answer = sectionEntity.getResume() + "한 이력과 " + sectionEntity.getEmphasize() + " 한 점을 강조 하고 싶으시군요. 참고해서 추후 대답에 적용하겠습니다.";
        } else if (sectionEntity.getResume() != null) {
            fineTuning = "나는 " + sectionEntity.getResume() + " 한 이력이 있어";
            fineTuning_answer = sectionEntity.getResume() + "한 이력이 있으시군요. 참고해서 추후 대답에 적용하겠습니다.";
        } else if (sectionEntity.getEmphasize() != null) {
            fineTuning = "나는 " + sectionEntity.getEmphasize() + " 한 점을 강조하고 싶다.";
            fineTuning_answer = sectionEntity.getEmphasize() + " 한 점을 강조 하고 싶으시군요. 참고해서 추후 대답에 적용하겠습니다.";
        }

        user = new Message("user", fineTuning);
        gpt = new Message("system", fineTuning_answer);
        messageList.add(user);
        messageList.add(gpt);

        return messageList;
    }

    //gpt 예상질문 생성
    public List<String> expectedQuestion(int sectionId) {

        List<Message> messageList = fineTuning(sectionId);
        String fineTuning = "당신은 이제 면접 예상질문을 생성할 것 입니다. 면접 예상 질문과 질문에 대해 어떻게 답변해야 하는지에 대한 답변 가이드를 함께 제공해 주세요. 면접 예상 질문을 생성할 때에는 사용자가 강조하고 싶은 점이 있어도 질문에 반영하지 마세요." +
                "\n" +
                "    1. 질문/질문에 대한 답변 가이드/2. 질문/질문에 대한 답변 가이드/3. 질문/질문에 대한 답변 가이드/4. 질문/질문에 대한 답변 가이드/5. 질문/질문에 대한 답변 가이드/6. 질문/질문에 대한 답변 가이드  이렇게 질문 뒤에 /, 질문에 대한 답변 가이드 뒤에 /로 구분하게 만든 문자열로 만들어주세요" +
                "\n각 답변 가이드는 여러 문장으로 제공하고 답변 가이드 내 각 문장의 구분은 '@'를 사용해 구분하세요. 답변 가이드에만 '@'를 사용한 구분을 사용하세요." +
                "\n답변 가이드 내 각 문장은 완전한 문장으로 제공하세요." +
                "\n최종적으로 다음과 같은 형식으로 제공되어야 합니다: 1. 질문/질문에 대한 답변 가이드1@질문에 대한 답변 가이드2/2. 질문/질문에 대한 답변 가이드......" +
                "\n중요: 답변 가이드를 제공할 때에는 답변 예시가 아닌 답변 가이드를 제공하세요. 답변을 어떻게 하면 좋은지에 관한 조언을 제공하세요.";
        String fineTuning_answer = "네 알겠습니다.";

        Message user = new Message("user", fineTuning);
        Message gpt = new Message("system", fineTuning_answer);
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
        GptResponse gptResponse = restTemplate.postForObject(url, gptRequest, GptResponse.class);
        log.info(gptResponse.getChoices().get(0).getMessage().getContent());
        List<String> question = Arrays.asList(gptResponse.getChoices().get(0).getMessage().getContent().split("/"));
        //List의 홀수 index는 예상 면접 질문 짝수 index는 답변 가이드
        return question;
    }

    //유저 질문
    public GptResponse userQuestion(String question, SectionEntity sectionEntity) {

        List<UserQuestionEntity> userQuestionEntities = userQuestionRepository.findAllBySectionEntity(sectionEntity);
        List<Message> messageList = fineTuning(sectionEntity.getId());
        userQuestionEntities.forEach(it -> {
            Message user = new Message("user", it.getQuestion());
            Message gpt = new Message("system", it.getAnswer());
            messageList.add(user);
            messageList.add(gpt);
        });
        String condition = "";
        if(!sectionEntity.getEmphasize().isEmpty() && !sectionEntity.getResume().isEmpty()){
            condition = "면접질문에 대해 저의 이력과 강조점을 참고해서";
        }else if(!sectionEntity.getEmphasize().isEmpty()){
            condition = "면접질문에 대해 저의 강조점을 참고해서";
        }else if(!sectionEntity.getResume().isEmpty()) {
            condition = "면접질문에 대해 이력을 참고해서";
        }
        question = question+"에 대한 "+condition+" 면접 질문에 대한 대답을 만들어 주세요. 만약 이력과 강조점이 없다면 임의로 대답을 만들어 주세요. 마크다운과 줄바꿈은 사용하지 말고 대답해주세요. 만약 면접 질문이 없거나 면접 질문이 이상하다면 \"면접 질문을 다시 적어주세요\"라고 대답해 주세요.";

        Message userQuestion = new Message("user", question);
        messageList.add(userQuestion);
        GptRequest gptRequest = GptRequest.builder()
                .model(model)
                .messages(messageList)
                .build();
        GptResponse gptResponse = restTemplate.postForObject(url, gptRequest, GptResponse.class);

        return gptResponse;
    }

    //gpt 예상 질문에 대한 유저 답변을 통해 gpt의 피드백 받기
    public GptResponse userAnswer(String answer, int gptQuestionId) {
        //gpt 면접 질문 정보
        GptQuestionEntity gptQuestionEntity = gptQuestionRepository.findById(gptQuestionId)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST));

        //이력 강조점 파인튜닝
        List<Message> messageList = fineTuning(gptQuestionEntity.getSection().getId());

        //대화내용 적용
        List<InterviewAnswerEntity> interviewAnswerEntities = interviewAnswerRepository.findAllByGptQuestion(gptQuestionEntity);
        String fineTuning = "";
        String fineTuning_answer = "";

        interviewAnswerEntities.forEach(it -> {
            Message user = new Message("user", it.getAnswer());
            Message gpt = new Message("system", it.getFeedback());
            messageList.add(user);
            messageList.add(gpt);
        });


        if (interviewAnswerEntities.isEmpty()) {
            fineTuning = "이제 \"" + gptQuestionEntity.getQuestion() + "\"에 대해 대답을 할게 내가 대답한 후에 내가 잘 대답했는지 피드백 해줘" +
                    "앞으로 대답 예시는 전부 다 " +
                    "1. 면접에 대한 피드백##2. ##3. ##4. ##5. ##6.  이렇게 질문에 대한 답변 가이드 뒤에 ##로 피드백을 구분하고 6개의 답변만 만들어주세요 문장에 줄바꿈은 필요 없습니다.";
            fineTuning_answer = "네 알겠습니다. 앞으로 모든 대답에 대해 대답 예시를 참고하여 면접 대답에 대한 피드백을 해준뒤 ##을 통해 각 피드백을 구분하고 6개의 답변을 만들은 뒤 줄바꿈없이하여 면접 질문에 대한 대답을 피드백 해드리겠습니다.";
            Message user = new Message("user", fineTuning);
            Message gpt = new Message("system", fineTuning_answer);
            messageList.add(user);
            messageList.add(gpt);
        }
        answer = "\""+gptQuestionEntity.getQuestion() + "\"에 대한 대답으로 "+answer+"이라 할게 내가 대답한 후에 내가 잘 대답했는지 피드백 해줘" +
                "대답 예시는 " +
                "1. 면접에 대한 피드백##2. ##3. ##4. ##5. ##6.  이렇게 질문에 대한 답변 가이드 뒤에 ##로 피드백을 구분하고 6개의 답변만 만들어주세요 문장에 줄바꿈은 필요 없습니다.";;
        Message userAnswer = new Message("user", answer);
        messageList.add(userAnswer);
        GptRequest gptRequest = GptRequest.builder()
                .model(model)
                .messages(messageList)
                .build();
        GptResponse gptResponse = restTemplate.postForObject(url, gptRequest, GptResponse.class);

        return gptResponse;
    }

    // 강조점 수정 시 답변 가이드 재생성
    public List<String> regenerateAnswerGuides(int sectionId, List<String> questions) {

        List<Message> messageList = fineTuning(sectionId);

        // Instruction for generating new answer guides
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("주어지는 면접 질문들에 대한 답변 가이드를 제공할 것입니다.\n");
        promptBuilder.append("각 질문에 대해 어떻게 답변해야 하는지에 대한 답변 가이드를 제공해 주세요.\n");
        promptBuilder.append("사용자가 강조하고 싶은 점과 이력이 있다면 반영해서 답변 가이드를 만들어 주세요.\n\n");

        // Append the questions
        for (int i = 0; i < questions.size(); i++) {
            promptBuilder.append((i + 1) + ". " + questions.get(i) + "\n");
        }

        promptBuilder.append("답변 가이드는 다음과 같은 형식으로만 제공해 주세요:\n");
        promptBuilder.append("[질문 번호]. [답변 가이드]\n");
        promptBuilder.append("질문 번호 뒤에 바로 답변 가이드를 적어주세요. 형식은 동일하게 유지해 주세요.\n");
        promptBuilder.append("답변 가이드 내용에 질문은 포함하지 마세요.\n");
        promptBuilder.append("각 답변 가이드는 '!!'로 구분하세요.\n");
        promptBuilder.append("답변 가이드는 여러 문장으로 제공하고 각 문장의 구분은 '@'를 사용해 구분하세요.\n");
        promptBuilder.append("답변 가이드 내 각 문장은 완전한 문장으로 제공하세요.");
        promptBuilder.append("최종적으로 다음과 같은 형식으로 제공되어야 합니다: 1. 답변 가이드1@답변가이드2!!2. 답변 가이드1@......\n");
        promptBuilder.append("중요: 당신이 제공해야 하는 것은 답변 가이드입니다. 답변 예시가 아닌 답변 가이드를 제공하세요. 답변을 어떻게 하면 좋은지에 관한 조언입니다.\n");
        promptBuilder.append("사용자는 당신의 답변이 답변 가이드라는 것을 이미 알고 있습니다. 답변 가이드라고 명시하지 마세요.");

        Message userMessage = new Message("user", promptBuilder.toString());
        messageList.add(userMessage);

        // Build the GPT request
        GptRequest gptRequest = GptRequest.builder()
                .model(model)
                .messages(messageList)
                .build();

        // Call the GPT API
        GptResponse gptResponse = restTemplate.postForObject(url, gptRequest, GptResponse.class);

        // Parse the response to extract the answer guides
        String responseContent = gptResponse.getChoices().get(0).getMessage().getContent();
        log.info("GPT response content:\n" + responseContent);

        // Split the response by lines
        String[] lines = responseContent.split("!!");

        List<String> answerGuides = new ArrayList<>();

        // Extract answer guides
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                // Remove the numbering
                String answerGuide = line.replaceFirst("\\d+\\.\\s+", "").trim();
                answerGuides.add(answerGuide);
            }
        }

        // Ensure the number of answer guides matches the number of questions
        if (answerGuides.size() != questions.size()) {
            log.error("Expected " + questions.size() + " answer guides but got " + answerGuides.size());
            throw new ApiException(ErrorCode.BAD_REQUEST, "답변 가이드 생성에 실패했습니다.");
        }

        return answerGuides;
    }
}
