package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class MovementRequestDTO extends ProtocolDTO {
    private long sequence;
    private boolean forward, back, left, right;


    public MovementRequestDTO(long sequence, boolean forward, boolean back, boolean left, boolean right) {
        this.sequence = sequence;
        this.forward = forward;
        this.back = back;
        this.left =left;
        this.right = right;
    }

    public long getSequence() {
        return sequence;
    }

    public boolean isForward() {
        return forward;
    }

    public boolean isBack() {
        return back;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "MovementProtocolDTO{" +
                "sequence=" + sequence +
                ", forward=" + forward +
                ", back=" + back +
                ", left=" + left +
                ", right=" + right +
                '}';
    }
}
