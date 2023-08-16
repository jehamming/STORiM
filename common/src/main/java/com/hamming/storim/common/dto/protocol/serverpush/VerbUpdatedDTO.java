package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class VerbUpdatedDTO extends ResponseDTO {

    private VerbDto verb;

    public VerbUpdatedDTO(VerbDto verb) {
        super(true, null);
        this.verb = verb;
    }

    public VerbDto getVerb() {
        return verb;
    }

    @Override
    public String toString() {
        return "VerbUpdatedDTO{" +
                "verb=" + verb +
                '}';
    }
}
                                                                                                                                          