package com.hamming.storim.server.common.dto.protocol.dataserver.user;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class UpdateUserDto implements ProtocolDTO {

    private String name;
    private Long id;
    private String email;
    private Long avatarID;

    public UpdateUserDto(Long id, String name, String email, Long avatarID){
        this.name = name;
        this.id = id;
        this.email = email;
        this.avatarID = avatarID;
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

    @Override
    public String toString() {
        return "UpdateUserDto{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", avatarID=" + avatarID +
                '}';
    }
}
