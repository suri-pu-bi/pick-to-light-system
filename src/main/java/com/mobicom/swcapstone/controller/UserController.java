package com.mobicom.swcapstone.controller;

import com.mobicom.swcapstone.domain.User;
import com.mobicom.swcapstone.dto.request.LoginRequest;
import com.mobicom.swcapstone.dto.response.LoginResponse;
import com.mobicom.swcapstone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/home/info")
    public ResponseEntity<LoginResponse> getUserInfo(@RequestParam String userId, HttpServletRequest req) throws Exception {
        return new ResponseEntity<>(userService.getUserInfo(userId, req), HttpStatus.OK);
    }


}
