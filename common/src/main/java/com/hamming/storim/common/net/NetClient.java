package com.hamming.storim.common.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hamming.storim.common.dto.protocol.ClientIdentificationDTO;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.util.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class NetClient<T extends ResponseDTO> implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ProtocolObjectSender protocolObjectSender;
    private Dispatcher dispatcher;
    private boolean running = false;
    private String id = "UNKNOWN";
    private Map<Class, ResponseContainer> responseContainers;
    private ConnectionListener connectionListener;
    private ProtocolReceiver protocolReceiver;
    private Gson gson;

    public NetClient(ConnectionListener connectionListener, ProtocolReceiver protocolReceiver) {
        this.connectionListener = connectionListener;
        this.protocolReceiver = protocolReceiver;
        initialize();
    }

    private void initialize() {
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

    public void connect(Socket s) {
        this.socket = s;
        registerStreams();
        connectionListener.connected();
    }

    public String connect(String ip, int port) {
        String retval = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 1000);
            registerStreams();
            connectionListener.connected();
        } catch (IOException e) {
            Logger.error(this, "(" + id + ") ERROR:" + e.getMessage());
            retval = e.getMessage();
            //e.printStackTrace();
        }
        return retval;
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
            if (id == null) {
                idText = connectionListener.getClass().getSimpleName();
            }
            protocolObjectSender = new ProtocolObjectSender(idText, out);
            in = new ObjectInputStream(socket.getInputStream());
            Thread clientThread = new Thread(this);
            clientThread.setName("Client Connection");
            clientThread.setDaemon(true);
            clientThread.start();
        } catch (IOException e) {
            Logger.error(this, "(" + id + ") ERROR:" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Object read = in.readObject();
                //READ JSON
                String json = (String) read;
                //Logger.info(this, "Received JSON:" + json);
                ProtocolDTO dto = gson.fromJson(json, ProtocolDTO.class);

                Logger.info(this, "Received DTO:" + dto.toString());
                if (dto instanceof ResponseDTO) {
                    ResponseDTO response = (ResponseDTO) dto;
                    ResponseContainer responseContainer = getResponseContainer(response.getClass());
                    if (responseContainer != null) {
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
                Logger.error(this, "Error:" + e.getMessage());
                e.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                in.close();
                socket.close();
            } catch (IOException e) {
                Logger.error(this, "Error:" + e.getMessage());
                e.printStackTrace();
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
        protocolObjectSender.stopSending();
        protocolObjectSender = null;
        running = false;
        socket = null;
        in = null;
    }

    public boolean isConnected() {
        return running;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
