package com.hamming.storim.common.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;
import com.hamming.storim.common.interfaces.Client;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.transformers.LoginTransformer;
import com.hamming.storim.common.util.Logger;
import org.java_websocket.WebSocket;

import java.nio.ByteBuffer;
import java.util.*;

public class WebsocketNetClient<T extends ResponseDTO> extends NetClient {

    private Gson gson;
    private WebSocket webSocket;
    private List<String> receivedMessages;
    private Map<String, JSONProtocolTransformer> transformers;

    public WebsocketNetClient(Client client, WebSocket webSocket, ConnectionListener connectionListener, ProtocolReceiver protocolReceiver) {
        super(client, connectionListener, protocolReceiver);
        this.webSocket = webSocket;
        initialize();
    }

    private void initialize() {
        // JSON
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ProtocolDTO.class, new ProtocolObjectSerializer<ProtocolDTO>());
        builder.registerTypeAdapter(ResponseDTO.class, new ProtocolObjectSerializer<ResponseDTO>());
        gson = builder.create();

        receivedMessages = Collections.synchronizedList(new ArrayList<>());
        transformers = new HashMap<>();
        registerProtocolTransformers();
    }

    private void registerProtocolTransformers() {
        transformers.put( JSONProtocolTransformer.commands.LOGIN.name(), new LoginTransformer(gson));
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
    public void connected() {
        super.connected();
        Thread clientThread = new Thread(this);
        clientThread.setName("WSClient Connection");
        clientThread.setDaemon(true);
        clientThread.start();
    }

    @Override
    public ProtocolDTO _getDTOFromConnection() {
        ProtocolDTO received = null;
        synchronized (receivedMessages) {
            if (!receivedMessages.isEmpty()) {
                String jsonString = receivedMessages.get(0);
                Logger.info(this, getClient().getId() + ":Received:" + jsonString );
                received = JSON2Protocol(jsonString);
                receivedMessages.remove(0);
            }
            receivedMessages.notify();
        }
        return received;
    }

    private ProtocolDTO JSON2Protocol(String text) {
        ProtocolDTO dto = null;
        JsonObject jsonObject = gson.fromJson(text, JsonObject.class);
        String command = jsonObject.get(JSONProtocolTransformer.COMMAND).getAsString();
        if ( command != null ) {
            JSONProtocolTransformer transformer = transformers.get(command);
            dto = transformer.transform(jsonObject);
        }
        return dto;
    }


    @Override
    public boolean _send(ProtocolDTO pDTO) {
        String json = toJson(pDTO);
        webSocket.send(json);
        return true;
    }


    public void onMessage(String message) {
        synchronized (receivedMessages) {
            receivedMessages.add(message);
            receivedMessages.notify();
        }
    }
    public void onMessage(ByteBuffer message) {

    }
}
