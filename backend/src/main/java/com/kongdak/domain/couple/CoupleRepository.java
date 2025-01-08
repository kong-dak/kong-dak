package com.kongdak.domain.couple;

import com.kongdak.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoupleRepository extends JpaRepository<Couple, Long> {
    Optional<Couple> findByMember1OrMember2(Member member1, Member member2);

    @Query("SELECT c FROM Couple c WHERE c.member1.id = :memberId OR c.member2.id = :memberId")
    Optional<Couple> findByMemberId(@Param("memberId") Long memberId);

    boolean existsByMember1OrMember2(Member member1, Member member2);

}