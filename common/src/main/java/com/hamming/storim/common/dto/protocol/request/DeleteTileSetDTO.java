package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class DeleteTileSetDTO extends ProtocolDTO {

    private Long id;

    public DeleteTileSetDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "DeleteTileSetDTO{" +
                "id=" + id +
                '}';
    }
}
