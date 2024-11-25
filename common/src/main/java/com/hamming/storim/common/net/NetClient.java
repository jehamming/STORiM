package com.hamming.storim.common.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hamming.storim.common.dto.protocol.ClientIdentificationDTO;
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

public abstract class NetClient<T extends ResponseDTO> implements Runnable {
    private ProtocolObjectSender protocolObjectSender;
    private Dispatcher dispatcher;
    private boolean running = false;
    private boolean silent = false;
    private Client client;
    private Map<Class, ResponseContainer> responseContainers;
    private ConnectionListener connectionListener;
    private ProtocolReceiver protocolReceiver;
    private Thread dispatcherThread;

    public NetClient(Client client, ConnectionListener connectionListener, ProtocolReceiver protocolReceiver) {
        this.connectionListener = connectionListener;
        this.protocolReceiver = protocolReceiver;
        this.client = client;
        initialize();
    }

    private void initialize() {
        this.responseContainers = new HashMap<>();
        this.dispatcher = new Dispatcher(protocolReceiver);
        this.protocolObjectSender = new ProtocolObjectSender(client, this);
    }


    private void start() {
        dispatcherThread = new Thread(dispatcher);
        dispatcherThread.start();
        protocolObjectSender.start();
    }

    private void stop() {
        protocolObjectSender.stop();
        dispatcherThread.interrupt();
        running = false;
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


    public abstract ProtocolDTO _getDTOFromConnection();

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                ProtocolDTO dto = _getDTOFromConnection();
                if ( dto != null ) {
                    Logger.info(this, client.getId() + "-Received:" + dto.toString());
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
                }
            } catch (Exception e) {
                Logger.error(this, "Error:" + e.getMessage());
                running = false;
                e.printStackTrace();
            }
        }
        if ( !silent ) {
            connectionListener.disconnected();
        }
    }

    public void send(ProtocolDTO pDTO) {
        protocolObjectSender.send(pDTO);
    }

    public abstract boolean _send(ProtocolDTO pDTO);


    public ResponseDTO sendReceive(ProtocolDTO requestResponseDTO, Class responseClass) {
        ResponseContainer responseContainer = new ResponseContainer();
        synchronized (responseContainer) {
            responseContainer.setResponse(null);
            responseContainer.setResponseClass(responseClass);
            addResponseContainer(responseContainer);
            //Logger.info(this, "SendReceive:" + requestResponseDTO + ", waiting for: " + responseClass.getSimpleName());
        }
        return protocolObjectSender.sendReceive(requestResponseDTO, responseContainer);
    }


    public void connected(){
        start();
        connectionListener.connected();
    }

    public void disconnected(){
        stop();
        connectionListener.disconnected();
    }

    public boolean isConnected() {
        return running;
    }

    public Client getClient() {
        return client;
    }
}
