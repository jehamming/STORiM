package com.hamming.storim.common.dto;

import java.util.Objects;

public class UserDto extends DTO {

    private Long currentAvatarID;
    private String email;

    public UserDto(Long id, String name, String email, Long currentAvatarID){
        setId(id);
        setName(name);
        this.email = email;
        this.currentAvatarID = currentAvatarID;
    }

    public String getEmail() {
        return email;
    }

    public Long getCurrentAvatarID() {
        return currentAvatarID;
    }


    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + getId() +
                ", currentAvatarID=" + currentAvatarID +
                ", name='" + getName() + '\'' +
                ", email='" + email + '\'' +
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
