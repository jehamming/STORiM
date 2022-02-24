package com.hamming.storim.client.listitem;

import com.hamming.storim.common.dto.VerbDto;

public class VerbListItem {
    private Long id;
    private String name;
    public VerbListItem(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
