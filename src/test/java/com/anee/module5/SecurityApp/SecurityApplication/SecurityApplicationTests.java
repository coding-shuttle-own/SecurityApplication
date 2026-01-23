package com.anee.module5.SecurityApp.SecurityApplication;

import com.anee.module5.SecurityApp.SecurityApplication.entities.User;
import com.anee.module5.SecurityApp.SecurityApplication.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecurityApplicationTests {

    @Autowired
    private JwtService jwtService;

	@Test
	void contextLoads() {
        User user = new User(4L, "anee@gmail.com", "password123", "Anee");

        String token = jwtService.generateToken(user);
        System.out.println("Generated Token: " + token);

        Long userId = jwtService.getUserIdFromToken(token);
        System.out.println("Extracted User ID: " + userId);
	}

}
