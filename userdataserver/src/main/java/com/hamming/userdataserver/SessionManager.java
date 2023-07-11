package com.hamming.userdataserver;

import com.hamming.storim.common.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    private Map<Long, Session> sessions;

    public SessionManager() {
        sessions = new HashMap<>();
    }

    public Session createSession(Long userId, String source) {
        String token = StringUtils.generateNewToken();
        Session session = new Session(source, token, userId);
        sessions.put(userId, session);
        return session;
    }

    public Session getSession(Long userId) {
       return sessions.get(userId);
    }

    public void deleteSession(Long userId) {
        sessions.remove(userId);
    }
}
