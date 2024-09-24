package org.team5.interview_partner.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UsersEntity,Integer> {
    UsersEntity findByUsername(String username);
}