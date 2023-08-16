package com.hamming.storim.client.listitem;

public class TileSetEditorListItem {

    private Long id;
    private String name;

    public TileSetEditorListItem(Long id, String name) {
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
        return "(" + id + ") " + name;
    }
}
