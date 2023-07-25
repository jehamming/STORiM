package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class UpdateUserDto extends ProtocolDTO {

    private String name;
    private Long id;
    private String email;
    private Long avatarID;
    private String username;
    private String password;

    public UpdateUserDto(Long id, String username, String password, String name, String email, Long avatarID){
        this.name = name;
        this.id = id;
        this.email = email;
        this.avatarID = avatarID;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Long getAvatarID() {
        return avatarID;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "UpdateUserDto{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", avatarID=" + avatarID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
