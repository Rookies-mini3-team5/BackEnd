package org.team5.interview_partner;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing  // JPA Auditing 활성화
public class InterviewPartnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewPartnerApplication.class, args);
    }

    // 애플리케이션 시작 시 타임존을 설정
    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
