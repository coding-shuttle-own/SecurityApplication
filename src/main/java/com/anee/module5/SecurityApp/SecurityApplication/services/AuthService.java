package com.anee.module5.SecurityApp.SecurityApplication.services;

import com.anee.module5.SecurityApp.SecurityApplication.dto.LoginDto;
import com.anee.module5.SecurityApp.SecurityApplication.dto.LoginResponseDto;
import com.anee.module5.SecurityApp.SecurityApplication.dto.SignUpDto;
import com.anee.module5.SecurityApp.SecurityApplication.dto.UserDto;
import com.anee.module5.SecurityApp.SecurityApplication.entities.User;
import com.anee.module5.SecurityApp.SecurityApplication.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public UserDto signUp(SignUpDto signUpDto) {

        // to check if user with email already exists
        Optional<User> user = userRepository.findByEmail(signUpDto.getEmail());
        if (user.isPresent()) {
            throw new BadCredentialsException("User with email already exists" + signUpDto.getEmail());
        }

        // create new user
        User newUser = mapper.map(signUpDto, User.class);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        User savedUser = userRepository.save(newUser);
        return mapper.map(savedUser, UserDto.class);

    }

    public LoginResponseDto login(LoginDto loginDto) {

        // authenticate user credentials by authentication manager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        // get authenticated user details
        User user = (User) authentication.getPrincipal();

        // generate jwt access and refresh tokens
        String accessToken =  jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponseDto(user.getId(), accessToken, refreshToken);
    }

    public LoginResponseDto refreshToken(String refreshToken) {

        // validate refresh token and get user id from it
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userService.getUserById(userId);

        // generate new access token with the given refresh token
        String accessToken = jwtService.generateAccessToken(user);

        return new LoginResponseDto(user.getId(), accessToken, refreshToken);
    }
}
