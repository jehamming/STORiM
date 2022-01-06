package com.hamming.storim.server.common;


import com.hamming.storim.common.dto.protocol.ClientTypeDTO;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.action.Action;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

//  ClientConnection, able to handle Async traffic and Sync actions
public abstract class ClientConnection<T extends ServerWorker> implements Runnable {

    private Socket socket;
    private ObjectInputStream in;
    private boolean running = true;
    private ProtocolHandler<Action> protocolHandler;
    private ObjectOutputStream out;
    private ClientTypeDTO clientTypeDTO;
    private T serverWorker;

    public ClientConnection(ClientTypeDTO clientTypeDTO, Socket s, ObjectInputStream in, ObjectOutputStream out, T serverWorker) {
        this.socket = s;
        this.in = in;
        this.clientTypeDTO = clientTypeDTO;
        this.out = out;
        protocolHandler = new ProtocolHandler();
        this.serverWorker = serverWorker;
        addActions();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Object read = in.readObject();
                ProtocolDTO dto = (ProtocolDTO) read;
                Action action = protocolHandler.getAction(dto);
                System.out.println("("+getClass().getSimpleName() +") FROM " + clientTypeDTO.getName() + ":" + dto);
                if (action != null) {
                    action.setDTO(dto);
                    // Check for Async or Sync behavior
                    switch (dto.getType()) {
                        case ProtocolDTO.SYNC:
                            ProtocolDTO result = executeAction(action);
                            System.out.println("RESPONSE :" + result);
                            out.writeObject(result);
                            break;
                        case ProtocolDTO.ASYNC:
                            serverWorker.addAction(action);
                            break;
                    }
                } else {
                    System.out.println(clientTypeDTO.getName() + ": NOT HANDLED:" + dto.getClass().getSimpleName());
                }
            } catch (IOException e) {
                System.out.println(this.getClass().getName() + ":" + "IO Error:" + e.getClass().getSimpleName());
                //e.printStackTrace();
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
        try {
            out.writeObject(dto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ProtocolHandler getProtocolHandler() {
        return protocolHandler;
    }

    public ProtocolDTO executeAction(Action action) {
        serverWorker.addAction(action);
        synchronized (action) {
            try {
                action.wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return action.getResult();
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
