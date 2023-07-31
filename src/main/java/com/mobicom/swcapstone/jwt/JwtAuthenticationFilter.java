package com.mobicom.swcapstone.jwt;

import com.mobicom.swcapstone.domain.User;
import com.mobicom.swcapstone.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// OncePerRequestFilter: 단 한번의 요청에 단 한번만 동작하도록 보장된 필터
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // Jwt Token을 추출해 통과하면 권한을 부여하고, 실패하면 권한을 부여하지 않고 다음필터로 진행시킴


    private final JwtProvider jwtProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Header의 Authorization 값이 비어있으면 -> Jwt Token을 전송하지 않음
        if (authorizationHeader ==  null) {
            filterChain.doFilter(request, response); // 다음 필터로 이동
            return;
        }

        // token 추출
        String token = jwtProvider.resolveToken(request);

        log.info(token);

        if (token != null && jwtProvider.validateToken(token)) {

            // 권한 정보 가져오기
            Authentication auth = jwtProvider.getAuthentication(token);
            // 권한 부여
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);



    }
}
