package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

import java.util.Arrays;
import java.util.List;

public class AddRoomDto extends ProtocolDTO {

    private String name;
    private int rows, cols;
    private List<Long> editors;

    public AddRoomDto(String name, int rows, int cols, List<Long> editors){
        this.name = name;
        this.rows = rows;
        this.cols = cols;
        this.editors = editors;
    }


    public String getName() {
        return name;

    }

    public List<Long> getEditors() {
        return editors;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }


    @Override
    public String toString() {
        return "AddRoomDto{" +
                "name='" + name + '\'' +
                ", rows=" + rows +
                ", cols=" + cols +
                ", editors=" + editors +
                '}';
    }
}
