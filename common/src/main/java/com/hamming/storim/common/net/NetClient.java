package com.hamming.storim.common.net;

import com.google.gson.*;
import com.hamming.storim.common.dto.DTO;
import com.hamming.storim.common.dto.protocol.Protocol;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.util.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class NetClient<T extends ResponseDTO> implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ProtocolObjectSender protocolObjectSender;
    private Dispatcher dispatcher;
    private boolean running = false;
    private String id;
    private Map<Class,ResponseContainer> responseContainers;
    private ConnectionListener connectionListener;
    private Gson gson;

    public NetClient(String id, ConnectionListener connectionListener, ProtocolReceiver protocolReceiver, String ip, int port) {
        initialize(id, connectionListener, protocolReceiver);
        connect(ip, port);
    }

    public NetClient(String id, ConnectionListener connectionListener, ProtocolReceiver protocolReceiver, Socket s) {
        this.socket = s;
        initialize(id, connectionListener, protocolReceiver);
        registerStreams();
    }

    private void initialize(String id, ConnectionListener connectionListener, ProtocolReceiver protocolReceiver) {
        this.id = id;
        this.connectionListener = connectionListener;
        this.responseContainers = new HashMap<>();

        // JSON
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ProtocolDTO.class, new ProtocolObjectSerializer<ProtocolDTO>());
        builder.registerTypeAdapter(ResponseDTO.class, new ProtocolObjectSerializer<ResponseDTO>());
        gson = builder.create();

        this.dispatcher = new Dispatcher(protocolReceiver);
        Thread t = new Thread(dispatcher);
        t.start();
    }

    public String getId() {
        String clientId = getClass().getSimpleName() + "-" +  id;
        return clientId;
    }

    private void addResponseContainer(ResponseContainer responseContainer) {
        responseContainers.put(responseContainer.getResponseClass(), responseContainer);
    }
    private void removeResponseContainer(ResponseContainer responseContainer) {
        responseContainers.remove(responseContainer.getResponseClass());
    }

    private ResponseContainer getResponseContainer(Class clazz) {
        return responseContainers.get(clazz);
    }

    private void registerStreams() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            String idText = id;
            if ( id == null ) {
                idText = connectionListener.getClass().getSimpleName();
            }
            protocolObjectSender = new ProtocolObjectSender(idText, out);
            in = new ObjectInputStream(socket.getInputStream());
            Thread clientThread = new Thread(this);
            clientThread.setName("Client Connection");
            clientThread.setDaemon(true);
            clientThread.start();
        } catch (IOException e) {
            Logger.info(this, "(" + getId() + ") ERROR:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private String connect(String ip, int port) {
        String retval = null;
        try {
            socket = new Socket(ip, port);
            registerStreams();
            connectionListener.connected();
        } catch (IOException e) {
            Logger.info(this, "(" + getId() + ") ERROR:" + e.getMessage());
            retval = e.getMessage();
            //e.printStackTrace();
        }
        return retval;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Object read = in.readObject();

                //READ JSON
                String json = (String)read;
                //Logger.info(this, "Received JSON:" + json);

                ProtocolDTO dto = gson.fromJson(json, ProtocolDTO.class);

                Logger.info(this, "Received DTO:" + dto.toString());
                if (dto instanceof ResponseDTO) {
                    ResponseDTO response = (ResponseDTO) dto;
                    ResponseContainer responseContainer = getResponseContainer(response.getClass());
                    if (responseContainer != null ) {
                        removeResponseContainer(responseContainer);
                        responseContainer.setResponse(response);
                        synchronized (responseContainer) {
                            responseContainer.notify();
                        }
                   }
                }
                dispatcher.dispatch(dto);
            } catch (IOException e) {
                running = false;
            } catch (ClassNotFoundException e) {
                Logger.info(this, "Error:" + e.getMessage());
                e.printStackTrace();
            }
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
        connectionListener.disconnected();
    }

    public void send(ProtocolDTO pDTO) {
        protocolObjectSender.send(pDTO);
    }

    public ResponseDTO sendReceive(ProtocolDTO requestResponseDTO, Class responseClass) {
        ResponseContainer responseContainer = new ResponseContainer();
        synchronized (responseContainer) {
            responseContainer.setResponse(null);
            responseContainer.setResponseClass(responseClass);
            addResponseContainer(responseContainer);
            Logger.info(this, "SendReceive:" + requestResponseDTO + ", waiting for: " + responseClass.getSimpleName());
        }
        return protocolObjectSender.sendReceive(requestResponseDTO, responseContainer);
    }

    public void dispose() {
        try {
            if (running) {
                running = false;
                closeConnection();
            }
            socket = null;
            in = null;
            protocolObjectSender.stopSending();
            protocolObjectSender = null;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void closeConnection() throws IOException {
        try {
            protocolObjectSender.stopSending();;
        } finally {
            try {
                in.close();
            } finally {
                socket.close();
            }
        }

    }

    public boolean isConnected() {
        return running;
    }

}
