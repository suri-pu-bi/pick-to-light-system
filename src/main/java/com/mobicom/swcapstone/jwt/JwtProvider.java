package com.mobicom.swcapstone.jwt;

import com.mobicom.swcapstone.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secretKey;


    @Value("${jwt.access.expiration}")
    private long accessTokenValidityInSeconds;

    private final UserDetailsService userDetailsService;


    @PostConstruct // 빈의 초기화 작업 설정
    protected void init() { // secretkey 인코딩
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // 유저 정보와 역할을 통하여 토큰 생성
    public String createToken(String userId, Role role) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("role", role);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 발급시간
                // 만료시간 설정, 현재 시간으로부터 [설정한 시간(밀리초) * 1000]초후 토큰 만료
                .setExpiration(new Date(now.getTime() + accessTokenValidityInSeconds * 1000 ))
                .signWith(SignatureAlgorithm.HS256, secretKey) // secret키로 암호화
                .compact(); // 문자열로 변환
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(token));
        // Spring Security에서 사용자 인증 정보를 나타내기 위해 사용
        // Jwt 기반의 인증에서는 사용자가 실제로 패스워드를 입력하는 것이 아니라 토큰을 통해 인증되므로 패스워드필드를 빈문자열 ""로 표현
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰을 이용하여 userId를 얻기
    public String getUserId(String token) {
        log.info(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject());
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Header를 통해 받아온 토큰에서 필요 없는 부분을 잘라낸다
    public String resolveToken(HttpServletRequest req) {
        String myToken = req.getHeader("Authorization");

        if(myToken == null) {
            return null;
        }

        String realToken = myToken.substring("Bearer".length()); // substring: 문자열에서 특정 위치 이후의 부분 추출
        realToken = realToken.split(" ")[1].trim(); // 토큰 앞 공백 제거
        return realToken;
    }

    // 토큰의 유효기간이 지났는지 아닌지 확인한다.
    public boolean validateToken(String jwtToken) {
        try {
            // Jws: Json Web Signature: Json Web Token을 생성하는데 사용되는 서명기술
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            // 만료시간이 현재 시간 이후라면 true 반환
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
