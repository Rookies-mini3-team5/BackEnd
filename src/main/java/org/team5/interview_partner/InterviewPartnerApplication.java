package org.team5.interview_partner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  // JPA Auditing 활성화
public class InterviewPartnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewPartnerApplication.class, args);
    }

}
