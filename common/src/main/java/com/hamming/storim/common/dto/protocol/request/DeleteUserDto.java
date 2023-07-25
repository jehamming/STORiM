package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class DeleteUserDto extends ProtocolDTO {

    private Long id;

    public DeleteUserDto(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "DeleteUserDto{" +
                "id=" + id +
                '}';
    }
}
