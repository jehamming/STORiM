package com.hamming.storim.server;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.Queue;

public class ClientSender implements Runnable {


    private ObjectOutputStream out;
    boolean running = false;
    private Queue<ProtocolDTO> itemsToSend;
    private static int INTERVAL = 50; // Milliseconds, 20Hz

    public ClientSender(ObjectOutputStream out) {
        this.out = out;
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        itemsToSend = new ArrayDeque<ProtocolDTO>();
        running = true;
        while (running) {
            long start = System.currentTimeMillis();
            while (!itemsToSend.isEmpty()) {
                ProtocolDTO dto = itemsToSend.remove();
                try {
                    System.out.println("SEND:" + dto);
                    out.writeObject(dto);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            long stop = System.currentTimeMillis();
            long timeSpent = start - stop;
            if (timeSpent < INTERVAL ) {
                try {
                    Thread.sleep(INTERVAL - timeSpent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stopSending() {
        running = false;
    }

    public void enQueue(ProtocolDTO dto) {
        synchronized (itemsToSend) {
            itemsToSend.add(dto);
        }
    }
}
