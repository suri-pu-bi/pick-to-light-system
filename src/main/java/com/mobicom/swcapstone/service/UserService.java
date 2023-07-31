package com.mobicom.swcapstone.service;

import com.mobicom.swcapstone.domain.Role;
import com.mobicom.swcapstone.domain.User;
import com.mobicom.swcapstone.dto.request.LoginRequest;
import com.mobicom.swcapstone.dto.response.LoginResponse;
import com.mobicom.swcapstone.jwt.JwtProvider;
import com.mobicom.swcapstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor // 차이점 정리
@Transactional // 쓰는 이유 공부
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // 회원 가입
    public boolean register(LoginRequest request) throws Exception{
        try{
            User user = User.builder()
                    .userId(request.getUserId())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.OPERATOR)
                    .build();

            userRepository.save(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }

        return true;
    }


    // 로그인
    public LoginResponse login(LoginRequest request) throws Exception {
        User user = userRepository.findByUserId(request.getUserId()).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));

        // 비밀번호와 매치
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }

        return LoginResponse.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .password(user.getPassword())
                .role(user.getRole())
                .token(jwtProvider.createToken(user.getUserId(), user.getRole()))
                .build();
    }

    // 유저 정보 가져오기
    public LoginResponse getUserInfo(String userId, HttpServletRequest req) throws Exception {
        User user = userRepository.findByUserId(userId)
                .orElseThrow( ()-> new Exception("계정을 찾을 수 없습니다."));

        return LoginResponse.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .password(user.getPassword())
                .role(user.getRole())
                .token(jwtProvider.resolveToken(req))
                .build();






    }
}
