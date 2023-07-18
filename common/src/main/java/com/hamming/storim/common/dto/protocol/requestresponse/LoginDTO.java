package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class LoginDTO extends ProtocolDTO {


    private String username;
    private String password;

    private Long roomID;

    public LoginDTO(String username, String password, Long roomID){
        this.username = username;
        this.password = password;
        this.roomID = roomID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Long getRoomID() {
        return roomID;
    }

    @Override
    public String toString() {
        return "LoginDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roomID=" + roomID +
                '}';
    }
}
