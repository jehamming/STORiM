package com.hamming.storim.client.controller;

import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.client.listitem.TileSetListItem;
import com.hamming.storim.client.panels.RoomTileEditorPanel;
import com.hamming.storim.client.view.RoomTileMapEditorView;
import com.hamming.storim.client.view.TileSet;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.TileSetDto;
import com.hamming.storim.common.dto.protocol.request.UpdateRoomDto;
import com.hamming.storim.common.dto.protocol.serverpush.*;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RoomTileMapEditorPanelController implements ConnectionListener {

    private ConnectionController connectionController;
    private RoomTileEditorPanel panel;
    private RoomTileMapEditorView roomTileMapEditorView;
    private RoomDto roomDto;
    private TileSet tileSet;

    private int[][] oldTileMap;

    private ComboBoxModel<TileSetListItem> tileSetsModel = new DefaultComboBoxModel<>();


    public RoomTileMapEditorPanelController(ConnectionController connectionController) {
        this.connectionController = connectionController;
        connectionController.addConnectionListener(this);
        registerReceivers();
    }

    public void setRoomTileMapEditorView(RoomTileMapEditorView roomTileMapEditorView) {
        this.roomTileMapEditorView = roomTileMapEditorView;
    }

    public void setPanel(RoomTileEditorPanel panel) {
        this.panel = panel;
    }

    private void registerReceivers() {
        connectionController.registerReceiver(SetRoomDTO.class, (ProtocolReceiver<SetRoomDTO>) dto -> setRoom(dto.getRoom()));
        connectionController.registerReceiver(TileSetAddedDTO.class, (ProtocolReceiver<TileSetAddedDTO>) dto -> addTileSet(dto.getTileSetDto()));
    }

    private void addTileSet(TileSetDto tileSetDto) {
        TileSetListItem tileSetListItem = new TileSetListItem(tileSetDto);
        panel.getCmbTileset().addItem(tileSetListItem);
    }


    public void setup() {
        panel.getCmbTileset().setModel(tileSetsModel);
        panel.getBtnApply().addActionListener(e -> {
            applyTile();
        });
        panel.getBtnApplyAll().addActionListener(e -> {
            applyTileToAll();
        });
        panel.getCmbTileset().addItemListener(e -> {
            int state = e.getStateChange();
            if (state == ItemEvent.SELECTED) {
                TileSetListItem tileSetListItem = (TileSetListItem) e.getItem();
                selectTileSet(tileSetListItem);
            }
        });
        panel.getCbGrid().setSelected(true);
        panel.getCbGrid().addActionListener(e -> roomTileMapEditorView.setGrid(panel.getCbGrid().isSelected()));
        panel.getBtnRevert().addActionListener(e -> {
            revert();
        });
        panel.getBtnSaveToServer().addActionListener(e -> {
            saveToServer();
        });
    }

    private void saveToServer() {
        int[][] newTilemap = roomTileMapEditorView.getTileMap();
        Long tileSetId = tileSet.getTileSetId();
        Long roomID = roomDto.getId();
        UpdateRoomDto updateRoomDto = new UpdateRoomDto(roomID, null, -1, -1, tileSetId, newTilemap);
        connectionController.send(updateRoomDto);
    }

    private void revert() {
        roomTileMapEditorView.scheduleAction(() -> {
            roomTileMapEditorView.setTileMap(oldTileMap);
        });
    }

    private void applyTileToAll() {
        int tileIndex = panel.getCmbTiles().getSelectedIndex();
        roomTileMapEditorView.scheduleAction(() -> {
            roomTileMapEditorView.applyToAll(tileIndex);
        });
    }

    private void applyTile() {
        int row = Integer.valueOf( panel.getTxtRow().getText() );
        int col = Integer.valueOf( panel.getTxtCol().getText() );
        int tileIndex = panel.getCmbTiles().getSelectedIndex();
        roomTileMapEditorView.scheduleAction(() -> {
            roomTileMapEditorView.applyTile(tileIndex, row, col);
        });
    }

    private void selectTileSet(TileSetListItem tileSetListItem) {
        TileSetDto dto = tileSetListItem.getTileSet();
        setTileSet(dto);
    }
    private void setRoom(RoomDto roomDto) {
        oldTileMap = roomDto.getTileMap();
        this.roomDto = roomDto;
        roomTileMapEditorView.scheduleAction(() -> {
            roomTileMapEditorView.setRoom(roomDto);
        });
    }
    private void setTileSet(TileSetDto dto) {
        tileSet = new TileSet(dto);
        roomTileMapEditorView.scheduleAction(() -> {
            roomTileMapEditorView.setTileSet(tileSet);
        });

        // Load the tiles
        SwingUtilities.invokeLater(() -> {
            panel.getCmbTiles().removeAllItems();
            for (Image image : tileSet.getTiles()) {
                panel.getCmbTiles().addItem(new ImageIcon(image));
            }
        });

    }


    @Override
    public void connected() {
        empty(true);
    }

    @Override
    public void disconnected() {
        empty(true);
        setEditable(false);
    }


    private void empty(boolean thorough) {
        SwingUtilities.invokeLater(() -> {
            panel.getCmbTiles().removeAllItems();
            panel.getCmbTileset().removeAllItems();
        });
    }

    private void setEditable(boolean editable) {
        SwingUtilities.invokeLater(() -> {
        });
    }

    public void setRowCol(int row, int col) {
        panel.getTxtRow().setText("" + row);
        panel.getTxtCol().setText("" + col);
    }
}
