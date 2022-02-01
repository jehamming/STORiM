package com.hamming.storim.common.net;

import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetClient<T extends ResponseDTO> implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ProtocolObjectSender protocolObjectSender;
    private Dispatcher dispatcher;
    private boolean running = false;
    private ConnectionController connectionController;
    private Map<Class,ResponseContainer> responseContainers;

    public NetClient(ConnectionController connectionController) {
        this.connectionController = connectionController;
        responseContainers = new HashMap<>();
        dispatcher = new Dispatcher(connectionController);
        Thread t = new Thread(dispatcher);
        t.start();
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

    public String connect(String ip, int port) {
        String retval = null;
        try {
            socket = new Socket(ip, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            protocolObjectSender = new ProtocolObjectSender(out);
            in = new ObjectInputStream(socket.getInputStream());
            Thread clientThread = new Thread(this);
            clientThread.setName("Client Connection");
            clientThread.setDaemon(true);
            clientThread.start();
        } catch (IOException e) {
            System.out.println(this.getClass().getName() + ":" + "ERROR:" + e.getMessage());
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
                ProtocolDTO dto = (ProtocolDTO) read;
                System.out.println(this.getClass().getName() + ":RECEIVED:" + dto.toString());
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
                // System.out.println(this.getClass().getName() + ":" + "Error:" + e.getMessage());
                // e.printStackTrace();
                running = false;
            } catch (ClassNotFoundException e) {
                System.out.println(this.getClass().getName() + ":" + "Error:" + e.getMessage());
                e.printStackTrace();
            }
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
        System.out.println(this.getClass().getName() + ":" + "NetClient finished");
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
            System.out.println(this.getClass().getName() + ":" + "SendReceive:" + requestResponseDTO + ", waiting for: " + responseClass.getSimpleName());
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
