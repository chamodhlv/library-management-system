package com.chamodh.library_management_system.service;

import com.chamodh.library_management_system.dto.MemberRequestDto;
import com.chamodh.library_management_system.dto.MemberResponseDto;
import com.chamodh.library_management_system.entity.Member;
import com.chamodh.library_management_system.exception.ResourceNotFoundException;
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

    public MemberResponseDto registerMember(MemberRequestDto requestDto) {
        Member member = new Member();
        member.setName(requestDto.getName());
        member.setEmail(requestDto.getEmail());
        member.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        member.setPhoneNumber(requestDto.getPhoneNumber());
        member.setMembershipDate(LocalDate.now());

        Member saved = memberRepository.save(member);
        return mapToResponseDto(saved);
    }

    public MemberResponseDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
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
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));

        member.setName(requestDto.getName());
        member.setEmail(requestDto.getEmail());
        member.setPhoneNumber(requestDto.getPhoneNumber());

        Member updated = memberRepository.save(member);
        return mapToResponseDto(updated);
    }

    public void changePassword(Long id, String newRawPassword) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));

        member.setPassword(passwordEncoder.encode(newRawPassword));
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new ResourceNotFoundException("Member not found with id: " + id);
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
    }
}