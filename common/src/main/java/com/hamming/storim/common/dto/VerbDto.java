package com.hamming.storim.common.dto;

public class VerbDto extends BasicObjectDTO {


    public VerbDto(Long id, String name){
        setId(id);
        setName(name);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
