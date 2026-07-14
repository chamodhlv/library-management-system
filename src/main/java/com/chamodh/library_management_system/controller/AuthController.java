package com.chamodh.library_management_system.controller;

import com.chamodh.library_management_system.dto.LoginRequestDto;
import com.chamodh.library_management_system.dto.LoginResponseDto;
import com.chamodh.library_management_system.dto.MemberRequestDto;
import com.chamodh.library_management_system.dto.MemberResponseDto;
import com.chamodh.library_management_system.entity.Member;
import com.chamodh.library_management_system.security.JwtUtil;
import com.chamodh.library_management_system.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> register(@Valid @RequestBody MemberRequestDto requestDto) {
        MemberResponseDto created = memberService.registerMember(requestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
        // Reuses the EXACT same MemberService logic already built -
        // password hashing, membershipDate defaulting, all handled there already
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword())
        );
        // This line does the REAL work: it calls our CustomUserDetailsService
        // to fetch the Member by email, then uses BCryptPasswordEncoder to check
        // if the raw password matches the stored hash. Throws an exception if not.

        Member member = (Member) authentication.getPrincipal();
        // "principal" = the authenticated user object - since Member implements
        // UserDetails, we can safely cast it back

        String token = jwtUtil.generateToken(member);

        LoginResponseDto response = new LoginResponseDto(
                token,
                member.getEmail(),
                member.getRole().name()
        );

        return ResponseEntity.ok(response);
    }
}