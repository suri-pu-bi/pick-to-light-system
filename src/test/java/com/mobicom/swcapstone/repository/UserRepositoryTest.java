package com.mobicom.swcapstone.repository;

import com.mobicom.swcapstone.domain.Role;
import com.mobicom.swcapstone.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.slf4j.MDC.clear;


@SpringBootTest
@Transactional
// 각 메서드를 실행하는 도중 메서드 값을 수정/삭제하려는 시도가 들어와도 값의 신뢰성 보장
// 연산 도중 오류가 발생해도 rollback해서 DB에 해당 결과가 반영되지 않도록 할 수 있음 (원자성 보장)
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;


    @AfterEach
    // 각 테스트가 종료될 때마다 이 기능 실행
    private void after(){
        em.clear();
    }

    @Test
    public void 성공_회원저장() throws Exception{
        // given
        User user = User.builder().userId("userId").password("12345678").role(Role.MANAGER).build();

        // when
        User saveUser = userRepository.save(user);

        // then
        User findUser = userRepository.findById(saveUser.getId()).orElseThrow(() -> new RuntimeException("저장된 회원이 없습니다."));

        // () 괄호까지 쳐야 단축키 나와!
        assertThat(findUser).isSameAs(saveUser);
        assertThat(findUser).isSameAs(user);
    }

    @Test
    public void 오류_회원가입시_아이디가_없음() throws Exception{
        // given
        User user = User.builder().password("12345678").role(Role.MANAGER).build();

        // when, then
        assertThrows(Exception.class, () -> userRepository.save(user));
    }

    @Test
    public void 오류_회원가입시_중복된_아이디가_있음() throws Exception{
        // given
        User user1 = User.builder().userId("userId").password("12345678").role(Role.OPERATOR).build();
        User user2 = User.builder().userId("userId").password("1223333").role(Role.MANAGER).build();

        userRepository.save(user1);
        assertThrows(Exception.class, () -> userRepository.save(user2));

    }

    @Test
    public void 성공_회원수정() throws Exception{
        // given
        User user1 = User.builder().userId("userId").password("12345678").role(Role.OPERATOR).build();
        userRepository.save(user1);
        clear();

        String updatePassword = "098767654";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // when
        User findUser = userRepository.findById(user1.getId()).orElseThrow(() -> new Exception());
        findUser.updatePassword(passwordEncoder, updatePassword);
        em.flush();

        // then
        User findUpdateUser = userRepository.findById(findUser.getId()).orElseThrow(() -> new Exception());

        assertThat(passwordEncoder.matches(updatePassword, findUpdateUser.getPassword()));
    }

    @Test
    public void 성공_회원삭제() throws Exception{
        // given
        User user1 = User.builder().userId("userId").password("12345678").role(Role.OPERATOR).build();
        userRepository.save(user1);
        clear();

        // when
        userRepository.delete(user1);
        clear();

        // then
        assertThrows(Exception.class, () -> userRepository.findById(user1.getId()).orElseThrow(() -> new Exception()));

    }

}