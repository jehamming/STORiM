package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.List;

public class GetThingsForUserResponseDTO extends ResponseDTO {

    private final List<Long> things;

    public GetThingsForUserResponseDTO(List<Long> things) {
        this.things = things;
    }

    public List<Long> getThings() {
        return things;
    }


    @Override
    public String toString() {
        return "GetThingsForUserResponseDTO{" +
                "things=" + things +
                '}';
    }
}
