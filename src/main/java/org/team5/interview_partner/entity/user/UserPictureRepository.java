package org.team5.interview_partner.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPictureRepository extends JpaRepository<UserPictureEntity, Integer> {
    UserPictureEntity findByUserId(int userId);
}