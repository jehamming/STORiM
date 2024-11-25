package com.hamming.storim.common.net;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.util.Logger;

import java.util.ArrayDeque;
import java.util.Deque;

public class Dispatcher implements Runnable {
    private Deque<ProtocolDTO> dispatchQueue;
    private final ProtocolReceiver receiver;

    public Dispatcher(ProtocolReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void run() {
        boolean running = true;
        dispatchQueue = new ArrayDeque<>();
        while (running) {
            if (dispatchQueue.isEmpty()) {
                try {
                    synchronized (this) {
                        this.wait();
                    }
                } catch (InterruptedException e) {
                    Logger.info(this, ":" + "interrupted!");
                    running = false;
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