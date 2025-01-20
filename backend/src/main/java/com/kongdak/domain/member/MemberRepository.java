package com.kongdak.domain.member;

import com.kongdak.domain.couple.Couple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndOauthProvider(String email, OAuthProvider provider);

    boolean existsByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.isActive = true AND m.couple IS NULL")
    List<Member> findActiveUnconnectedMembers();

    @Query("SELECT m.id FROM Member m WHERE m.couple = :couple AND m.id != :memberId")
    Optional<Long> findPartnerIdByCoupleAndMemberIdNot(
            @Param("couple") Couple couple,
            @Param("memberId") Long memberId
    );

    // 특정 커플에 속한 모든 멤버 조회
    @Query("SELECT m FROM Member m WHERE m.couple = :couple")
    List<Member> findByCouple(@Param("couple") Couple couple);

    // 특정 커플의 특정 멤버를 제외한 파트너 조회
    @Query("SELECT m FROM Member m WHERE m.couple = :couple AND m.id != :memberId")
    Optional<Member> findPartnerByCouple(
            @Param("couple") Couple couple,
            @Param("memberId") Long memberId
    );
}
