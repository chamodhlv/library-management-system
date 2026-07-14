package com.chamodh.library_management_system.security;

import com.chamodh.library_management_system.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // OncePerRequestFilter guarantees this filter runs exactly ONCE per request,
    // even in complex servlet environments where a request might otherwise
    // pass through the filter chain multiple times

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        // Clients send the token like: "Authorization: Bearer eyJhbGc..."

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No token, or wrong format - just let the request continue.
            // Spring Security will later block it if the endpoint requires auth
            // (we configure WHICH endpoints require auth in the next step)
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        // Strip the "Bearer " prefix (7 characters) to get the raw token

        String email;
        try {
            email = jwtUtil.extractUsername(token);
        } catch (Exception e) {
            // Token is malformed, expired, or signature invalid -
            // treat it as "no valid auth", let Spring Security block it downstream
            filterChain.doFilter(request, response);
            return;
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Only authenticate if: we got a valid email from the token,
            // AND there's no existing authentication already set for this request
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                // null = no credentials needed here, since we already trust the JWT signature -
                // this isn't a password-based login, it's re-confirming an existing session

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                // THIS is the critical line - it tells Spring Security
                // "this request is authenticated as this user, with these roles"
                // Every controller downstream can now see this via @AuthenticationPrincipal
                // or SecurityContextHolder, and role checks (hasRole("LIBRARIAN")) will work
            }
        }

        filterChain.doFilter(request, response);
        // ALWAYS continue the filter chain, whether we authenticated or not -
        // Spring Security's own rules (configured next) decide whether
        // an unauthenticated request gets blocked
    }
}