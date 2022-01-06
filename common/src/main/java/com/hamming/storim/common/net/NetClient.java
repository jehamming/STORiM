package com.hamming.storim.common.net;

import com.hamming.storim.common.ProtocolHandler;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetClient implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean running = false;
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

            Thread clientThread = new Thread(this);
            clientThread.setName("Client Connection");
            clientThread.setDaemon(true);
            clientThread.start();
            running = true;
        } catch (IOException e) {
            System.out.println(this.getClass().getName() + ":" + "ERROR:" + e.getMessage());
            retval = e.getMessage();
            //e.printStackTrace();
        }
        return retval;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Object read = in.readObject();
                ProtocolDTO dto = (ProtocolDTO) read;
                System.out.println(this.getClass().getName() + "RECEIVED:" + dto.toString());
                received(dto);
            } catch (IOException e) {
               // System.out.println(this.getClass().getName() + ":" + "Error:" + e.getMessage());
               // e.printStackTrace();
                running = false;
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
            if (running) {
                running = false;
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
        return running;
    }

}
