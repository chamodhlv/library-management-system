package com.chamodh.library_management_system.controller;

import com.chamodh.library_management_system.dto.MemberRequestDto;
import com.chamodh.library_management_system.dto.MemberResponseDto;
import com.chamodh.library_management_system.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponseDto> registerMember(@Valid @RequestBody MemberRequestDto requestDto) {
        MemberResponseDto created = memberService.registerMember(requestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMemberById(@PathVariable Long id) {
        MemberResponseDto member = memberService.getMemberById(id);
        return ResponseEntity.ok(member);
    }

    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getAllMembers() {
        List<MemberResponseDto> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberResponseDto> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberRequestDto requestDto) {
        MemberResponseDto updated = memberService.updateMember(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody String newPassword) {
        // Deliberately a SEPARATE endpoint from updateMember - changing a
        // password is a distinct, more sensitive action than editing name/phone
        memberService.changePassword(id, newPassword);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}