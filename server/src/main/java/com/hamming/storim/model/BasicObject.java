package com.hamming.storim.model;

import java.io.Serializable;

// Everything extends a BasicObject
public class BasicObject implements Serializable {

    private Long id;
    private User creator;
    private User owner;
    private String name;

    public BasicObject(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return  getClass().getSimpleName() + "{ id=" + id +
                ", creator=" + creator +
                ", owner=" + owner +
                ", name='" + name + "}";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
