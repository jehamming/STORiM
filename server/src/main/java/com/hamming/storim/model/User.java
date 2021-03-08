package com.hamming.storim.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends BasicObject implements Serializable {

    private String username;
    private String password;
    private String email = "";
    private Location location;
    private List<Room> rooms;
    private List<Thing> inventory;
    private List<Verb> verbs;


    public User( Long id) {
        super(id);
        rooms = new ArrayList<Room>();
        inventory = new ArrayList<Thing>();
        verbs = new ArrayList<Verb>();
    }

    public void removeRoom(Room room) {
        if (rooms.contains(room) ) {
            rooms.remove(room);
        }
    }

    public void addRoom(Room room) {
        if (!rooms.contains(room) ) {
            rooms.add(room);
        }
    }

    public void addVerb(Verb verb) {
        if (!verbs.contains(verb) ) {
            verbs.add(verb);
        }
    }

    public void removeVerb(Verb verb) {
        if (verbs.contains(verb) ) {
            verbs.remove(verb);
        }
    }


    public void addToInventory(Thing thing) {
        if (!inventory.contains(thing) ) {
            inventory.add(thing);
        }
    }


    public void removeFromInventory(Thing thing) {
        if (inventory.contains(thing) ) {
            inventory.remove(thing);
        }
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

    public List<Verb> getVerbs() {
        return verbs;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Thing> getInventory() {
        return inventory;
    }

    public static User valueOf(Long id, String name, String username, String password, String email ) {
        final User u = new User(id);
        u.setPassword(password);
        u.setName(name);
        u.setUsername(username);
        u.setEmail(email);
        return u;
    }

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
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", " + super.toString() +
                '}';
    }


}
