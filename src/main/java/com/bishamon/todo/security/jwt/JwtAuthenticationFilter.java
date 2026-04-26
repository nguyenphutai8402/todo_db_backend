package com.bishamon.todo.security.jwt;

import com.bishamon.todo.enumeration.TokenType;
import com.bishamon.todo.security.user.CustomUserDetailsService;
import com.bishamon.todo.service.impl.BlackListedAccessTokenServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    JwtTokenProvider jwtTokenProvider;
    CustomUserDetailsService userDetailsService;
    BlackListedAccessTokenServiceImpl blackListedAccessTokenServiceImpl;

    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = resolveToken(request);
            if(!StringUtils.hasText(token)){
                filterChain.doFilter(request, response);
                return;
            }
            if(!jwtTokenProvider.validateToken(token)){
                filterChain.doFilter(request, response);
                return;
            }
            Claims claims = jwtTokenProvider.parseClaims(token);
            if (jwtTokenProvider.getTokenType(claims) != TokenType.ACCESS) {
                filterChain.doFilter(request, response);
                return;
            }
            String jti = jwtTokenProvider.getJti(claims);
            if(blackListedAccessTokenServiceImpl.isBlacklisted(jti)){
                filterChain.doFilter(request, response);
                return;
            }
            if(SecurityContextHolder.getContext().getAuthentication() == null){
                String email = jwtTokenProvider.getSubject(claims);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception e){
            log.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
