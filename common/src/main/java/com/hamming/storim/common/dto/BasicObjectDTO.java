package com.hamming.storim.common.dto;

import java.io.Serializable;
import java.util.List;

public class BasicObjectDTO extends DTO {

    private Long id;
    private Long creatorID;
    private Long ownerID;
    private String name;
    private List<Long> editors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(Long creatorID) {
        this.creatorID = creatorID;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getEditors() {
        return editors;
    }

    public void setEditors(List<Long> editors) {
        this.editors = editors;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +"{" +
                "id=" + id +
                ", creatorID=" + creatorID +
                ", ownerID=" + ownerID +
                ", name='" + name + '\'' +
                ", editors=" + editors +
                '}';
    }
}
