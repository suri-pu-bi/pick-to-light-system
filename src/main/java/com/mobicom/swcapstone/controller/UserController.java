package com.mobicom.swcapstone.controller;

import com.mobicom.swcapstone.domain.User;
import com.mobicom.swcapstone.dto.request.LoginRequest;
import com.mobicom.swcapstone.dto.response.LoginResponse;
import com.mobicom.swcapstone.dto.response.UserResponse;
import com.mobicom.swcapstone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Boolean> signUp(@RequestBody LoginRequest request) throws Exception {
        return new ResponseEntity<>(userService.register(request), HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login (@RequestBody LoginRequest request) throws Exception {
        return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
    }
    // @AuthenticationPrincipal: 현재 사용자의 인증된 정보를 가져올 수 있음
    @GetMapping("/home/info")
    public ResponseEntity<UserResponse> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        String userId = userDetails.getUsername();
        return new ResponseEntity<>(userService.getUserInfo(userId), HttpStatus.OK);
    }


}
