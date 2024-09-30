package org.team5.interview_partner.entity.section;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team5.interview_partner.entity.user.UsersEntity;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<SectionEntity,Integer> {

    List<SectionEntity> findByUser(UsersEntity user);

    // 주어진 name 값으로 Section을 찾는 메서드
    Optional<SectionEntity> findByName(String name);

    // 주어진 name 값이 prefix로 시작하는 모든 Section을 찾는 메서드
    List<SectionEntity> findAllByNameStartingWith(String name);
}
