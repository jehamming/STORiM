package com.hamming.storim.server.common;


import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;
import com.hamming.storim.common.dto.protocol.request.ClientTypeDTO;
import com.hamming.storim.common.net.ProtocolObjectSender;
import com.hamming.storim.common.net.ResponseContainer;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.action.Action;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

//  ClientConnection, able to handle Async traffic and Sync actions
public abstract class ClientConnection<T extends ServerWorker> implements Runnable {

    private Socket socket;
    private ObjectInputStream in;
    private boolean running = true;
    private ProtocolObjectSender protocolObjectSender;
    private ProtocolHandler<Action> protocolHandler;
    private ClientTypeDTO clientTypeDTO;
    private T serverWorker;
    private ResponseContainer responseContainer;

    public ClientConnection(ClientTypeDTO clientTypeDTO, Socket s, ObjectInputStream in, ObjectOutputStream out, T serverWorker) {
        this.socket = s;
        this.in = in;
        this.clientTypeDTO = clientTypeDTO;
        protocolObjectSender = new ProtocolObjectSender(out);
        protocolHandler = new ProtocolHandler();
        this.serverWorker = serverWorker;
        responseContainer = new ResponseContainer();
        addActions();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Object read = in.readObject();
                ProtocolDTO dto = (ProtocolDTO) read;
                if ( dto instanceof ResponseDTO ) {
                    ResponseDTO response = (ResponseDTO) dto;
                    responseContainer.setResponse(response);
                    synchronized (responseContainer) {
                        responseContainer.notify();
                    }
                } else {
                    Action action = protocolHandler.getAction(dto);
                    System.out.println("(" + getClass().getSimpleName() + ") FROM " + clientTypeDTO.getName() + ":" + dto);
                    if (action != null) {
                        action.setDTO(dto);
                        serverWorker.addAction(action);
                    } else {
                        System.out.println(clientTypeDTO.getName() + ": NOT HANDLED:" + dto.getClass().getSimpleName());
                    }
                }
            } catch (EOFException e) {
                System.out.println(this.getClass().getName() + ":" + "EOF" + e.getClass().getSimpleName());
                running = false;
            } catch (IOException e) {
                System.out.println(this.getClass().getName() + ":" + "IO Error:" + e.getClass().getSimpleName());
                e.printStackTrace();
                running = false;
            } catch (ClassNotFoundException e) {
                System.out.println(this.getClass().getName() + ":" + "ClassNotFoundException:" + e.getMessage());
                e.printStackTrace();
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
        }
        running = false;
        System.out.println(clientTypeDTO.getName() + ":" + "Client Socket closed");
        connectionClosed();
    }

    public abstract void connectionClosed();

    public abstract void addActions();

    public void send(ProtocolDTO dto) {
        protocolObjectSender.send(dto);
    }

    public <T extends ResponseDTO> T sendReceive(ProtocolDTO requestDTO, Class<T> responseClass) {
        return responseClass.cast( _sendReceive(requestDTO, responseClass));
    }

    private ResponseDTO _sendReceive(ProtocolDTO requestResponseDTO, Class responseClass) {
        synchronized (responseContainer) {
            responseContainer.setResponse(null);
            responseContainer.setResponseClass(responseClass);
            System.out.println(this.getClass().getName() + ":" + "SendReceive:" + requestResponseDTO + ", waiting for: " + responseClass.getSimpleName());
        }
        return protocolObjectSender.sendReceive(requestResponseDTO, responseContainer);
    }

    public ProtocolHandler getProtocolHandler() {
        return protocolHandler;
    }

    public T getServerWorker() {
        return serverWorker;
    }

    public String getSource() {
        return clientTypeDTO.getName() + socket.getInetAddress().toString();
    }

    public ClientTypeDTO getClientType() {
        return clientTypeDTO;
    }
}
