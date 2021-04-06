package com.hamming.storim.server.model;

public class Avatar extends ImageObject   {

    public Avatar(Long id) {
        super(id);
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "id=" + getId() +
                '}';
    }
}
