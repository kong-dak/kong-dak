package com.kongdak.domain.couple.repository;

import com.kongdak.domain.couple.entity.Couple;
import com.kongdak.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoupleRepository extends JpaRepository<Couple, Long> {

    @Query("SELECT m.couple FROM Member m WHERE m.id = :memberId")
    Optional<Couple> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Member m WHERE m IN (:members) AND m.couple IS NOT NULL")
    boolean existsByMemberIn(@Param("members") List<Member> members);
}