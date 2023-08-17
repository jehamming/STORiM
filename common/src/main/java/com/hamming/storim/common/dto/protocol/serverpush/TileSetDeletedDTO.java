package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class TileSetDeletedDTO extends ResponseDTO {

    private Long id;

    public TileSetDeletedDTO(Long id) {
        super(true, null);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TileSetDeletedDTO{" +
                "id=" + id +
                '}';
    }
}
