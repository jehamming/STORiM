package com.hamming.storim.server.common.dto.protocol.dataserver.user;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class ValidateUserRequestDTO extends ProtocolDTO {

    private String source;
    private String username;
    private String password;

    public ValidateUserRequestDTO(String source, String username, String password) {
        this.username = username;
        this.password = password;
        this.source = source;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "ValidateUserRequestDTO{" +
                "source='" + source + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
