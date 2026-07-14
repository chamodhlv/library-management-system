package com.chamodh.library_management_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member implements UserDetails {
    // implements UserDetails - this is what lets Spring Security treat
    // a Member as something it can authenticate and authorize

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phoneNumber;

    @Column(nullable = false)
    private LocalDate membershipDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.MEMBER;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<BorrowRecord> borrowRecords = new ArrayList<>();

    public enum Role {
        MEMBER, LIBRARIAN
    }

    // --- UserDetails interface methods below ---
    // Spring Security calls these internally during authentication -
    // you don't call them yourself anywhere

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security expects roles prefixed with "ROLE_" by convention
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        // Spring Security's concept of "username" - we use email since
        // that's what members log in with
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Not implementing account expiry for this project
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Not implementing account locking for this project
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Not implementing password expiry for this project
    }

    @Override
    public boolean isEnabled() {
        return true; // Not implementing account disabling for this project
    }
}