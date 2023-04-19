package com.hamming.storim.common.dto.protocol;

public class ClientIdentificationDTO extends ProtocolDTO {

    private String clientID;
    private String clientType;


    public ClientIdentificationDTO(String clientID, String clientType) {
        this.clientID = clientID;
        this.clientType = clientType;
    }

    public String getClientID() {
        return clientID;
    }

    public String getClientType() {
        return clientType;
    }
}
