package com.hamming.storim.common.dto.protocol.login;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ProtocolSyncRequestDTO;

public class LoginRequestDTO extends ProtocolSyncRequestDTO {


    private String username;
    private String password;

    public LoginRequestDTO(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "LoginRequestDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
