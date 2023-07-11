package com.hamming.userdataserver;

public class Session {

    private String source;
    private String token;
    private Long userId;

    public Session(String source, String token, Long userId) {
        this.source = source;
        this.token = token;
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }
}
