package com.hamming.storim.model;

public class VerbOutput extends BasicObject   {

    private Verb verb;
    private User caller;
    private String toCaller;
    private String toLocation;

    public VerbOutput(Long id) {
        super(id);
    }


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

    public Verb getCommand() {
        return verb;
    }

    public void setCommand(Verb verb) {
        this.verb = verb;
    }

    public User getCaller() {
        return caller;
    }

    public void setCaller(User caller) {
        this.caller = caller;
    }


}
