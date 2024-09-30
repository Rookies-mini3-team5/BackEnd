package org.team5.interview_partner.domain.userquestion.service;

import org.springframework.security.core.Authentication;
import org.team5.interview_partner.domain.userquestion.dto.UserQuestionListResponse;
import org.team5.interview_partner.domain.userquestion.dto.UserQuestionRequest;
import org.team5.interview_partner.domain.userquestion.dto.UserQuestionResponse;

import java.util.List;

public interface UserQuestionService {
    void addUserQuestion(int sectionId, UserQuestionRequest userQuestionRequest, Authentication authentication);
    UserQuestionListResponse userQuestionList(int sectionId,Authentication authentication);
    UserQuestionResponse userQuestion(int userQuestionId,Authentication authentication);
}
