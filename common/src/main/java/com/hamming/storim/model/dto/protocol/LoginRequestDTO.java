package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;

public class LoginRequestDTO implements DTO {


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
