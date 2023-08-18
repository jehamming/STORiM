package com.hamming.storim.server;

import java.util.List;

public class AuthorisationDelta {

    private List<Long> added;
    private List<Long> removed;

    public AuthorisationDelta(List<Long> added, List<Long> removed) {
        this.added = added;
        this.removed = removed;
    }

    public List<Long> getAdded() {
        return added;
    }

    public List<Long> getRemoved() {
        return removed;
    }
}
