package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.model.Session;
import com.dekhokaun.mindarobackend.payload.request.SessionRequest;
import com.dekhokaun.mindarobackend.payload.response.SessionResponse;
import com.dekhokaun.mindarobackend.repository.SessionRepository;
import com.dekhokaun.mindarobackend.utils.ObjectMapperUtils;
import com.dekhokaun.mindarobackend.exception.InvalidRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public SessionResponse createSession(SessionRequest request) {
        Session session = ObjectMapperUtils.map(request, Session.class);
        sessionRepository.save(session);
        return ObjectMapperUtils.map(session, SessionResponse.class);
    }

    public List<SessionResponse> getUserSessions(String userId) {
        return sessionRepository.findByUserid(UUID.fromString(userId)).stream()
                .map(session -> ObjectMapperUtils.map(session, SessionResponse.class))
                .collect(Collectors.toList());
    }

    public List<SessionResponse> getAllSessions() {
        return sessionRepository.findAll().stream()
                .map(session -> ObjectMapperUtils.map(session, SessionResponse.class))
                .collect(Collectors.toList());
    }

    public SessionResponse getSessionById(String sessionId) {
        Session session = sessionRepository.findById(UUID.fromString(sessionId))
                .orElseThrow(() -> new InvalidRequestException("Session not found"));
        return ObjectMapperUtils.map(session, SessionResponse.class);
    }

    public void endSession(String sessionId) {
        sessionRepository.deleteById(UUID.fromString(sessionId));
    }

    public void updateLogoutTime(String sessionId) {
        Session session = sessionRepository.findById(UUID.fromString(sessionId))
                .orElseThrow(() -> new RuntimeException("Session not found"));
        session.setLogouttime(LocalDateTime.now());
        sessionRepository.save(session);
    }

    public SessionResponse updateSession(String sessionId, SessionRequest request) {
        Session session = sessionRepository.findById(UUID.fromString(sessionId))
                .orElseThrow(() -> new InvalidRequestException("Session not found"));

        session.setBrowser(request.getBrowser());
        session.setGps(request.getGps());
        session.setIp(request.getIp());
        session.setLogintype(request.getLogintype());

        Session updated = sessionRepository.save(session);
        return ObjectMapperUtils.map(updated, SessionResponse.class);
    }
}
