package com.hamming.storim.net;

import com.hamming.storim.game.Protocol;
import com.hamming.storim.model.dto.DTO;
import com.hamming.storim.model.dto.protocol.ProtocolDTO;

public interface NetCommandReceiver<T extends ProtocolDTO> {

    public void receiveDTO(T dto);
}
