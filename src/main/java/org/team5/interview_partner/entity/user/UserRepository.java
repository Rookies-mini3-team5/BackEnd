package org.team5.interview_partner.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team5.interview_partner.entity.user.enums.Role;

public interface UserRepository extends JpaRepository<UsersEntity,Integer> {
    UsersEntity findByUsername(String username);
    UsersEntity findByUsernameAndRole(String username, Role role);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}