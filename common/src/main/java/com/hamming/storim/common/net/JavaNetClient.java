package com.hamming.storim.common.net;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;
import com.hamming.storim.common.interfaces.Client;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.util.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class JavaNetClient<T extends ResponseDTO> extends NetClient {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public JavaNetClient(Client client, ConnectionListener connectionListener, ProtocolReceiver protocolReceiver) {
        super(client, connectionListener, protocolReceiver);
    }


    public void disconnect() {
        if (socket != null) {
            try {
                in.close();
                socket.close();
            } catch (IOException e) {
                Logger.error(this, "Error:" + e.getMessage());
                e.printStackTrace();
            }
        }
        socket = null;
        in = null;
        out = null;
        disconnected();
    }

    public void connect(Socket s) {
        this.socket = s;
        registerStreamsAndStart();
    }

    private void registerStreamsAndStart() {
        registerStreams();
        Thread clientThread = new Thread(this);
        clientThread.setName("Client Connection");
        clientThread.setDaemon(true);
        clientThread.start();
        connected();
    }

    public String connect(String ip, int port) {
        String retval = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 1000);
            registerStreamsAndStart();
        } catch (IOException e) {
            Logger.error(this, getClient().getId()+":" + e.getMessage());
            retval = e.getMessage();
        }
        return retval;
    }


    private void registerStreams() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            Logger.error(this, getClient().getId()+ ":" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public ProtocolDTO _getDTOFromConnection() {
        ProtocolDTO received = null;
        try {
            if ( in != null ) { // Netclient could be in the process of disconnecting..
                Object read = in.readObject();

                // Check for DTO type and if so,
                if (read instanceof ProtocolDTO) {
                    received = (ProtocolDTO) read;
                } else {
                    Logger.error(this, "Could not deserialize to ProtocolDTO:" + read.getClass().getName());
                }
            }

        } catch (ClassNotFoundException e) {
            Logger.error(this, e.getClass().getSimpleName()+ ":" + e.getMessage());
        } catch (IOException e) {
            Logger.info(this, e.getClass().getSimpleName()+ ": disconnect(ed/ing)");
            disconnect();
        }
        return received;
    }


    @Override
    public boolean _send(ProtocolDTO dto) {
        boolean success = true;
        try {
            Logger.info(this, getClient().getId(), "Send DTO" + dto);
            out.writeObject(dto);
            out.flush();
        } catch (InvalidClassException e) {
            Logger.error(this, "_send():"+ e.getClass().getSimpleName() + "-" + e.getMessage());
            e.printStackTrace();
        } catch (NotSerializableException e) {
            Logger.error(this,"_send():"+ e.getClass().getSimpleName() + "-" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Logger.error(this,"_send():"+ e.getClass().getSimpleName() + "-" + e.getMessage());
            e.printStackTrace();
            success = false;
        }
        return success;
    }
}
