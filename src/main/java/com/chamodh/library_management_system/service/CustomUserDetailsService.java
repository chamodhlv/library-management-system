package com.chamodh.library_management_system.service;

import com.chamodh.library_management_system.entity.Member;
import com.chamodh.library_management_system.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Spring Security calls this method automatically during login -
        // "username" here is really our email, since that's what Member.getUsername() returns
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found with email: " + email));
        // Member implements UserDetails, so we can return it directly -
        // no separate mapping needed
    }
}