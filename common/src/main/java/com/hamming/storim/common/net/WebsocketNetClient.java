package com.hamming.storim.common.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;
import com.hamming.storim.common.interfaces.Client;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.util.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class WebsocketNetClient<T extends ResponseDTO> extends NetClient implements Runnable {

    private Gson gson;

    public WebsocketNetClient(Client client, ConnectionListener connectionListener, ProtocolReceiver protocolReceiver) {
        super(client, connectionListener, protocolReceiver);
        initialize();
    }

    private void initialize() {
        // JSON
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ProtocolDTO.class, new ProtocolObjectSerializer<ProtocolDTO>());
        builder.registerTypeAdapter(ResponseDTO.class, new ProtocolObjectSerializer<ResponseDTO>());
        gson = builder.create();
    }


    private String toJson(ProtocolDTO dto) {
        JsonElement element = gson.toJsonTree(dto);
        if (element.isJsonObject()) {
            element.getAsJsonObject().addProperty(ProtocolObjectSerializer.CLASS_PROPERTY_NAME, dto.getClass().getSimpleName());
        }
        String json = gson.toJson(element);
        return json;
    }






    @Override
    public ProtocolDTO _getDTOFromConnection() {
        ProtocolDTO received = null;
        //ProtocolDTO dto = gson.fromJson(json, ProtocolDTO.class);
        return received;
    }


    @Override
    public boolean _send(ProtocolDTO pDTO) {
        return false;
    }





}
