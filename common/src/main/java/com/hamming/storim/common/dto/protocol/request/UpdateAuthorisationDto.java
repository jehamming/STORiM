package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

import java.util.List;

public class UpdateAuthorisationDto extends ProtocolDTO {

    private Long id;
    private String dtoClassName;
    private List<Long> newEditors;

    public UpdateAuthorisationDto(Long id, String dtoClassName, List<Long> newEditors){
        this.id = id;
        this.dtoClassName = dtoClassName;
        this.newEditors = newEditors;
    }

    public Long getId() {
        return id;
    }

    public String getDtoClassName() {
        return dtoClassName;
    }

    public List<Long> getNewEditors() {
        return newEditors;
    }

    @Override
    public String toString() {
        return "UpdateAuthorisationDto{" +
                "id=" + id +
                ", dtoClassName=" + dtoClassName +
                ", newEditors=" + newEditors +
                '}';
    }
}
