package com.hamming.storim.common.net;

import com.hamming.storim.common.Protocol;
import com.hamming.storim.common.ProtocolHandler;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.VersionCheckDTO;

import java.io.*;
import java.net.Socket;

public class NetClient implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean open = true;
    private ProtocolHandler protocolHandler;
    private NetCommandReceiver receiver;

    public NetClient(NetCommandReceiver receiver) {
        this.receiver = receiver;
        protocolHandler = new ProtocolHandler();
    }

    public String connect(String ip, int port) {
        String retval = null;
        try {
            socket = new Socket(ip, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            // Do Protocol handshake
            doProtocolHandshake();

            Thread clientThread = new Thread(this);
            clientThread.setName("Client Connection");
            clientThread.setDaemon(true);
            clientThread.start();
            open = true;
        } catch (IOException e) {
            System.out.println(this.getClass().getName() + ":" + "ERROR:" + e.getMessage());
            retval = e.getMessage();
            //e.printStackTrace();
        }
        return retval;
    }

    private void doProtocolHandshake() throws IOException {
        VersionCheckDTO response = null;
        try {
            send(new VersionCheckDTO(Protocol.version));
            response = (VersionCheckDTO) in.readObject();
            if (response != null) {
                if (!response.isVersionCompatible()) {
                    String failure = Protocol.version + " != " + response.getServerVersion();
                    throw new IOException(failure);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            String failure = e.getMessage();
            throw new IOException(failure);
        }

    }

    @Override
    public void run() {
        while (open) {
            try {
                Object read = in.readObject();
                ProtocolDTO dto = (ProtocolDTO) read;
                System.out.println(this.getClass().getName() + "RECEIVED:" + dto.toString());
                received(dto);
            } catch (IOException e) {
               // System.out.println(this.getClass().getName() + ":" + "Error:" + e.getMessage());
               // e.printStackTrace();
                open = false;
            } catch (ClassNotFoundException e) {
                System.out.println(this.getClass().getName() + ":" + "Error:" + e.getMessage());
                e.printStackTrace();
            }
        }

        if ( socket != null ) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
        System.out.println(this.getClass().getName() + ":" + "NetClient finished");
    }

    public void send(ProtocolDTO pDTO) {
        try {
            System.out.println(this.getClass().getName() + ":" + "Send:" + pDTO );
            out.writeObject(pDTO);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void received(ProtocolDTO dto) {
        receiver.receiveDTO(dto);
    }


    public void dispose() {
        try {
            if (open) {
                open = false;
                closeConnection();
            }
            socket = null;
            in = null;
            out = null;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void closeConnection() throws IOException {
        try {
            out.close();
        } finally {
            try {
                in.close();
            } finally {
                socket.close();
            }
        }

    }


    public boolean isConnected() {
        return open;
    }

}
