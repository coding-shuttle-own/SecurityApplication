package com.anee.module5.SecurityApp.SecurityApplication.handlers;

import com.anee.module5.SecurityApp.SecurityApplication.entities.User;
import com.anee.module5.SecurityApp.SecurityApplication.services.JwtService;
import com.anee.module5.SecurityApp.SecurityApplication.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;

    @Value("${deploy.env}")
    private String deployEnv;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // Extract user details from OAuth2AuthenticationToken
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) token.getPrincipal();

        // Get user email from OAuth2User attributes
        String email = oAuth2User.getAttribute("email");
        // Check if user already exists in the database
        User user = userService.getUserByEmail(email);

        // If user does not exist, create a new user record
        if(user == null) {
            User newUser = User.builder()
                    .name(oAuth2User.getAttribute("name"))
                    .email(email)
                    .build();

            // Save the new user to the database
            user = userService.save(newUser);
        }

        // generate jwt access and refresh tokens
        String accessToken =  jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Set refresh token in HttpOnly cookie
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv)); // Set to true in production only, means cookie is only sent over HTTPS
        response.addCookie(cookie);

        // Redirect to front-end with access token as a query parameter
        String frontEndUrl = "http://localhost:8080/home.html?token="+accessToken;
//        getRedirectStrategy().sendRedirect(request, response, frontEndUrl);

        // Alternative way to redirect
        response.sendRedirect(frontEndUrl);

    }
}
