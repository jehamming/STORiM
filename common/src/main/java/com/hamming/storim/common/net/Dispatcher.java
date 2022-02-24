package com.hamming.storim.common.net;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

import java.util.ArrayDeque;
import java.util.Deque;

public class Dispatcher implements Runnable {
    private Deque<ProtocolDTO> dispatchQueue;
    private boolean running = true;
    private ProtocolReceiver receiver;

    public Dispatcher(ProtocolReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void run() {
        dispatchQueue = new ArrayDeque<>();
        while (running) {
            if (dispatchQueue.isEmpty()) {
                try {
                    synchronized (this) {
                        this.wait();
                    }
                } catch (InterruptedException e) {
                    System.out.println(this.getClass().getName() + ":" + "Exception : method wait was interrupted!");
                }
            }
            while (!dispatchQueue.isEmpty()) {
                ProtocolDTO protocolDTO = dispatchQueue.removeFirst();
                receiver.receiveDTO(protocolDTO);
            }
        }
    }


    public void dispatch(ProtocolDTO dto) {
        dispatchQueue.addLast(dto);
        synchronized (this) {
            this.notify();
        }
    }
}