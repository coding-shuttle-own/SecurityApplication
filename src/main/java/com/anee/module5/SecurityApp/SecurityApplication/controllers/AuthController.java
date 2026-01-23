package com.anee.module5.SecurityApp.SecurityApplication.controllers;

import com.anee.module5.SecurityApp.SecurityApplication.dto.LoginDto;
import com.anee.module5.SecurityApp.SecurityApplication.dto.SignUpDto;
import com.anee.module5.SecurityApp.SecurityApplication.dto.UserDto;
import com.anee.module5.SecurityApp.SecurityApplication.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpDto signUpDto) {
        UserDto userDto = authService.signUp(signUpDto);
        return ResponseEntity.ok(userDto);

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        String token = authService.login(loginDto);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(token);
    }

}
