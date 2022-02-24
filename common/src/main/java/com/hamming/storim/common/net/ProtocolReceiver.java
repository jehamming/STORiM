package com.hamming.storim.common.net;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public interface ProtocolReceiver<T extends ProtocolDTO> {

    public void receiveDTO(T dto);
}
