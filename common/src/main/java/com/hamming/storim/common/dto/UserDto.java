package com.hamming.storim.common.dto;

import java.util.Objects;

public class UserDto extends DTO {

    private Long currentAvatarID;
    private String email;
    private String username;
    private String password;

    public UserDto(){

    }

    public UserDto(Long id, String name, String username, String password, String email, Long currentAvatarID){
        setId(id);
        setName(name);
        this.email = email;
        this.currentAvatarID = currentAvatarID;
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public Long getCurrentAvatarID() {
        return currentAvatarID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCurrentAvatarID(Long currentAvatarID) {
        this.currentAvatarID = currentAvatarID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", currentAvatarID=" + currentAvatarID +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(getId(), userDto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
