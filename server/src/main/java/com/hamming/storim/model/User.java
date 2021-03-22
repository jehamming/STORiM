package com.hamming.storim.model;

import com.hamming.storim.factories.AvatarFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends BasicObject implements Serializable {

    private String username;
    private String password;
    private String email = "";
    private Location location;
    private Avatar currentAvatar;


    public User( Long id) {
        super(id);
        currentAvatar = null;
    }

    public Avatar getCurrentAvatar() {
        return currentAvatar;
    }

    public void setCurrentAvatar(Avatar currentAvatar) {
        this.currentAvatar = currentAvatar;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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
                ", " + super.toString() +
                '}';
    }


}
