package com.kongdak.domain.couple;

import com.kongdak.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoupleRepository extends JpaRepository<Couple, Long> {
    Optional<Couple> findByMember1OrMember2(Member member1, Member member2);

    boolean existsByMember1OrMember2(Member member1, Member member2);

}
