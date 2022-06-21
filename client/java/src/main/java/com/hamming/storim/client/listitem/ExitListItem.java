package com.hamming.storim.client.listitem;

import com.hamming.storim.common.dto.ExitDto;
import com.hamming.storim.common.dto.ThingDto;

public class ExitListItem {


    private ExitDto exitDto;

    public ExitListItem(ExitDto exitDto) {
        this.exitDto = exitDto;
    }

    public ExitDto getExitDto() {
        return exitDto;
    }

    public void setExitDto(ExitDto exitDto) {
        this.exitDto = exitDto;
    }

    @Override
    public String toString() {
        return exitDto.getId() + "-" + exitDto.getName();
    }


}
