package com.hamming.storim.common.dto.protocol;

public class ClientTypeDTO extends ProtocolResponseDTO {

    public final static int TYPE_SERVER = 0;
    public final static int TYPE_CLIENT = 1;

    private String name;
    private int type;

    public ClientTypeDTO(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }


    @Override
    public String toString() {
        return "ClientTypeDTO{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
