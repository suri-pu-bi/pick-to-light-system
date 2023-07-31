package com.mobicom.swcapstone.config;

import com.mobicom.swcapstone.domain.Role;
import com.mobicom.swcapstone.jwt.JwtProvider;
import com.mobicom.swcapstone.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable() // HTTP BASIC 인증 비활성화 -> jwt 토큰 인증 방식이므로
                .csrf().disable() // CSRF(Cross Site Request Forgery) 보호 기능 비활성화, API에서는 보통 필요하지 않음
                .cors().and() // CORS(Cross-Origin Resource Sharing) 활성화 -> 다른 도메인과의 통신 필요, 보안 강화
                // and(): 설정을묶어줌
                .headers().frameOptions().disable().and() // 웹 페이지가 다른 웹 페이지 내부의 프레임에 로드될 수 있게 됨
                // 세션 관리 정책 설정: jwt 토큰 기반 인증방식이므로 무상태 세션 설정
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // Spring Security에서 로그인을 진행해주는 filter (UsernamePasswordAuthenticationFilter)에 가기전에
                // JWT Token을 추출한 후 해당 토큰이 유효한지 체크하는 JwtTokenFilter를 거치게 함
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()// 요청에 대한 접근 권한 설정을 위한 시작점
                .antMatchers("/user/home/info").permitAll()
                .antMatchers("/user/signup").permitAll()
                .antMatchers("/user/login").permitAll() // '/user/login' 경로에 대해서는 인증없이 허용
                .antMatchers("/user/manager/**").hasAuthority(Role.MANAGER.name()) // 'user/manager'로 시작하는 경로에 대해서는 사용자의 권한이 'manager'와 일치해야 접근 가능
                .antMatchers("/user/operator/**").hasAuthority(Role.OPERATOR.name())
                .anyRequest().authenticated().and()// 나머지 모든 요청에 대해서도 인증된 사용자만 접근 가능
                // 에러 핸들링
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        // 권한 문제가 발생했을 때 이 부분을 호출한다.
                        response.setStatus(403);
                        response.setCharacterEncoding("utf-8");
                        response.setContentType("text/html; charset=UTF-8");
                        response.getWriter().write("권한이 없는 사용자입니다.");
                    }
                })
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        // 인증 문제가 발생했을 때 이 부분을 호출
                        response.setStatus(401);
                        response.setCharacterEncoding("utf-8");
                        response.setContentType("text/html; charset=UTF-8");
                        response.getWriter().write("인증되지 않은 사용자입니다.");
                    }
                }).and()

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // password 앞에 Encoding 방식이 붙은 채로 저장되어 암호화 방식을 지정하여 저장할 수 있다 -> 비밀번호 안전하게 저장하기 위해
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
