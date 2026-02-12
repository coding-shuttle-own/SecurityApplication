package com.anee.module5.SecurityApp.SecurityApplication.config;

import com.anee.module5.SecurityApp.SecurityApplication.entities.enums.Permissions;
import com.anee.module5.SecurityApp.SecurityApplication.entities.enums.Role;
import com.anee.module5.SecurityApp.SecurityApplication.filters.JwtAuthFilter;
import com.anee.module5.SecurityApp.SecurityApplication.handlers.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.anee.module5.SecurityApp.SecurityApplication.entities.enums.Permissions.*;
import static com.anee.module5.SecurityApp.SecurityApplication.entities.enums.Role.ADMIN;
import static com.anee.module5.SecurityApp.SecurityApplication.entities.enums.Role.CREATOR;

@Configuration
@EnableWebSecurity // by adding this annotation we are telling Spring Boot that we are configuring SpringSecurityFilterChain
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler successHandler;

    private static final String[] publicRoutes = {"/posts", "/error", "/auth/**", "/home.html"};

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicRoutes).permitAll()
                        .requestMatchers("/posts/**").authenticated()
                        .anyRequest().authenticated())
                .csrf(csrfConfig -> csrfConfig.disable()) // disabling CSRF for testing purpose
                .sessionManagement(sessionConfig -> sessionConfig
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2config -> oauth2config
                        .failureUrl("/login?error=true")
                        .successHandler(successHandler));
//                .formLogin(Customizer.withDefaults()) ;// mandatory login form

        return httpSecurity.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
