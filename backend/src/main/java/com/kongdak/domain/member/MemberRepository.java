package com.kongdak.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndOAuthProvider(String email, OAuthProvider provider);

    boolean existsByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.isActive = true AND m.couple IS NULL")
    List<Member> findActiveUnconnectedMembers();

}
