package com.chamodh.library_management_system.repository;

import com.chamodh.library_management_system.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    // Critical for login later - Spring Security will use this to find
    // a member by the email they type in
}