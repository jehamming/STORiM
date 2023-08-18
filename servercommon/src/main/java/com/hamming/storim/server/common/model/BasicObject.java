package com.hamming.storim.server.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Everything extends a BasicObject
public class BasicObject implements Serializable {

    private Long id;
    private Long creatorId;
    private Long ownerId;
    private List<Long> editors = new ArrayList<>();
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Long> getEditors() {
        return editors;
    }

    public void setEditors(List<Long> editors) {
        this.editors = editors;
    }
}
