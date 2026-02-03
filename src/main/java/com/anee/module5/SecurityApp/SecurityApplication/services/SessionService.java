package com.anee.module5.SecurityApp.SecurityApplication.services;

import com.anee.module5.SecurityApp.SecurityApplication.entities.Session;
import com.anee.module5.SecurityApp.SecurityApplication.entities.User;
import com.anee.module5.SecurityApp.SecurityApplication.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final int SESSION_LIMIT = 2;

    public void generateNewSession(User user, String refreshToken) {
        // Extract existing sessions for the user
        List<Session> userSessions = sessionRepository.findByUser(user);

        // if user has reached session limit, remove the oldest session
        if (userSessions.size() == SESSION_LIMIT) {
            // Sort sessions by last used time
            userSessions.sort(Comparator.comparing(Session::getLastUsedAt));

            // Remove the least recently used session
            Session leastRecentlyUsedSession = userSessions.getFirst();
            sessionRepository.delete(leastRecentlyUsedSession);
        }

        // Create and save the new session
        Session newSession = Session.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();
        sessionRepository.save(newSession);
    }

    public void validateSession(String refreshToken) {
        // Fetch session by refresh token
        Session session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new SessionAuthenticationException("Session not found for refreshToken: " + refreshToken));

        // Update last used timestamp
        session.setLastUsedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }
}
