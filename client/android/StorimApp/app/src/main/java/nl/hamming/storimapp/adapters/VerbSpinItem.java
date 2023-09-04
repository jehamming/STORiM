package nl.hamming.storimapp.adapters;

import androidx.annotation.NonNull;

public class VerbSpinItem {

    private Long verbId;
    private String name;
    public VerbSpinItem( Long verbId, String name ) {
        this.name = name;
        this.verbId = verbId;
    }

    public Long getVerbId() {
        return verbId;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
