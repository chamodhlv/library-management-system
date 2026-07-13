package com.chamodh.library_management_system.service;

import com.chamodh.library_management_system.dto.MemberRequestDto;
import com.chamodh.library_management_system.dto.MemberResponseDto;
import com.chamodh.library_management_system.entity.Member;
import com.chamodh.library_management_system.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    // Injected just like a repository - Spring finds the bean we defined
    // in SecurityConfig and wires it in automatically

    public MemberResponseDto registerMember(MemberRequestDto requestDto) {
        Member member = new Member();
        member.setName(requestDto.getName());
        member.setEmail(requestDto.getEmail());
        member.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        // NEVER store requestDto.getPassword() directly - always hash first.
        // encode() takes the raw password and returns a one-way BCrypt hash,
        // e.g. "mypassword123" becomes something like "$2a$10$N9qo8uLOickgx2ZMRZoMy..."
        member.setPhoneNumber(requestDto.getPhoneNumber());
        member.setMembershipDate(LocalDate.now());
        // Business rule: membershipDate is always "today" on registration,
        // never sent by the client

        Member saved = memberRepository.save(member);
        return mapToResponseDto(saved);
    }

    public MemberResponseDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
        return mapToResponseDto(member);
    }

    public List<MemberResponseDto> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public MemberResponseDto updateMember(Long id, MemberRequestDto requestDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));

        member.setName(requestDto.getName());
        member.setEmail(requestDto.getEmail());
        member.setPhoneNumber(requestDto.getPhoneNumber());
        // NOTE: password update is handled separately (below) - a generic
        // "update my details" call shouldn't silently reset your password

        Member updated = memberRepository.save(member);
        return mapToResponseDto(updated);
    }

    public void changePassword(Long id, String newRawPassword) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));

        member.setPassword(passwordEncoder.encode(newRawPassword));
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new RuntimeException("Member not found with id: " + id);
        }
        memberRepository.deleteById(id);
    }

    private MemberResponseDto mapToResponseDto(Member member) {
        return new MemberResponseDto(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getMembershipDate()
        );
        // Notice: password is NEVER included, even hashed -
        // MemberResponseDto simply has no password field at all
    }
}