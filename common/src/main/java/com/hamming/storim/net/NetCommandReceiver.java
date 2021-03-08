package com.hamming.storim.net;

import com.hamming.storim.game.Protocol;
import com.hamming.storim.model.dto.DTO;

public interface NetCommandReceiver<T extends DTO> {

    public void receiveDTO(T dto);
}
