package com.hamming.storim.server.common.model;

public class VerbOutput extends BasicObject   {

    private Verb verb;
    private Long callerId;
    private String toCaller;
    private String toLocation;

    public String getToCaller() {
        return toCaller;
    }

    public void setToCaller(String toCaller) {
        this.toCaller = toCaller;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public void setVerb(Verb verb) {
        this.verb = verb;
    }

    public Long getCallerId() {
        return callerId;
    }

    public void setCallerId(Long callerId) {
        this.callerId = callerId;
    }
}
