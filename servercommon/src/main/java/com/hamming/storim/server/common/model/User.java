package com.hamming.storim.server.common.model;

import com.hamming.storim.common.dto.UserDto;

import java.io.Serializable;
import java.util.Objects;

public class User extends BasicObject implements Serializable {

    private String username;
    private String password;
    private String email = "";
    private Avatar currentAvatar;


    public Avatar getCurrentAvatar() {
        return currentAvatar;
    }

    public void setCurrentAvatar(Avatar currentAvatar) {
        this.currentAvatar = currentAvatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail(){return this.email;}

    public void setEmail(String email) {this.email = email;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "User{" +
                " id=" + getId() +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static User valueOf(UserDto dto) {
        User u = new User();
        u.setId(dto.getId());
        u.setName(dto.getName());
        u.setEmail(dto.getEmail());
        u.setOwnerId(dto.getOwnerID());
        u.setCreatorId(dto.getCreatorID());
        return u;
    }


}
