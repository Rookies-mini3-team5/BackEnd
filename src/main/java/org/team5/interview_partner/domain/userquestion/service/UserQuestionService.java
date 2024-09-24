package org.team5.interview_partner.domain.userquestion.service;

import org.team5.interview_partner.domain.userquestion.dto.UserQuestionListResponse;
import org.team5.interview_partner.domain.userquestion.dto.UserQuestionRequest;
import org.team5.interview_partner.domain.userquestion.dto.UserQuestionResponse;

import java.util.List;

public interface UserQuestionService {
    void addUserQuestion(int sectionId, UserQuestionRequest userQuestionRequest);
    UserQuestionListResponse userQuestionList(int sectionId);
    UserQuestionResponse userQeustion(int userQuestionId);
}
