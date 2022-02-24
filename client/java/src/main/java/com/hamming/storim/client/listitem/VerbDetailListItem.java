package com.hamming.storim.client.listitem;

import com.hamming.storim.common.dto.VerbDetailsDTO;

public class VerbDetailListItem {
    private VerbDetailsDTO verb;
    public VerbDetailListItem(VerbDetailsDTO verb) {
        this.verb = verb;
    }

    public VerbDetailsDTO getVerb() {
        return verb;
    }

    public void setVerb(VerbDetailsDTO verb) {
        this.verb = verb;
    }

    @Override
    public String toString() {
        return verb.getName();
    }
}
