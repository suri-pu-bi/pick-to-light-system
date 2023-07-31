package com.mobicom.swcapstone.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

// Spring Security는 유저 인증과정에서 UserDetails를 참고하여 인증 진행
// UserDetails를 아래와 같이 상속하여 인증을 진행하도록 설정
// User Entity에 바로 상속할 경우 엔티티가 오염되어 향후 User 엔티티를 사용하기 어려워지기 때문에 따로 클래스를 만들어줌
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public final User getUser() {
        return user;
    }

    // 사용자가 가지고 있는 권한 반환 -> 이 정보를 기반으로 Spring Security는 사용자의 권한에 따라 접근 허용하거나 거부
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // SimpleGrantedAuthority 객체는 Spring Security에서 권한을 표현하는 객체
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
