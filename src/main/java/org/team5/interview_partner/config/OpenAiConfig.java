package org.team5.interview_partner.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Log4j2
public class OpenAiConfig {
//    @Value("${openai.api.key}")
//    private String openAiKey;
//    @Bean
//    public RestTemplate template(){
//        log.info(openAiKey);
////        RestTemplate restTemplate = new RestTemplate();
////        restTemplate.getInterceptors().add((request, body, execution) -> {
////            request.getHeaders().add("Authorization", "Bearer " + openAiKey);
////            return execution.execute(request, body);
////        });
////        return restTemplate;
//        return null;
//    }
}