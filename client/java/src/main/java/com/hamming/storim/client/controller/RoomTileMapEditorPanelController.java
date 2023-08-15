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
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.common.dto.protocol.serverpush.*;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;
import com.hamming.storim.common.view.Action;

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
    private ComboBoxModel<TileSetListItem> backgroundTileSetsModel = new DefaultComboBoxModel<>();
    private ComboBoxModel<TileSetListItem> foregroundTileSetsModel = new DefaultComboBoxModel<>();
    private TileSet bgTileSet;
    private TileSet fgTileSet;
    private boolean addTileToForeground = false;


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
        connectionController.registerReceiver(RoomUpdatedDTO.class, (ProtocolReceiver<RoomUpdatedDTO>) dto -> roomUpdated(dto.getRoom()));
        connectionController.registerReceiver(LoginResultDTO.class, (ProtocolReceiver<LoginResultDTO>) dto -> loginSuccess(dto.isLoginSucceeded()));
    }

    private void loginSuccess(boolean loginSucceeded) {
        if (loginSucceeded) {
            //Get the Tilesets
            GetTileSetsDTO getTileSetsDTO = new GetTileSetsDTO();
            GetTileSetsResponseDTO getTileSetsResponseDTO = connectionController.sendReceive(getTileSetsDTO, GetTileSetsResponseDTO.class);
            if (getTileSetsResponseDTO.getTileSets() != null) {
                for (Long tileSetId : getTileSetsResponseDTO.getTileSets()) {
                    TileSetDto tileSetDto = getTileSetFromServer(tileSetId);
                    addTileSet(tileSetDto);
                }
            }
        }
    }

    private TileSetDto getTileSetFromServer(Long tileSetId) {
        GetTileSetDTO getTileSetDTO = new GetTileSetDTO(tileSetId);
        GetTileSetResultDTO getTileSetResultDTO = connectionController.sendReceive(getTileSetDTO, GetTileSetResultDTO.class);
        return getTileSetResultDTO.getTileSetDto();
    }

    private void roomUpdated(RoomDto room) {
        setRoom(room);
    }

    private void addTileSet(TileSetDto tileSetDto) {
        TileSet tileSet = new TileSet(tileSetDto);
        TileSetListItem tileSetListItem = new TileSetListItem(tileSet);
        panel.getCmbBackgroundTileset().addItem(tileSetListItem);
        panel.getCmbForegroundTileset().addItem(tileSetListItem);
    }


    public void setup() {
        panel.getCmbForegroundTileset().setModel(foregroundTileSetsModel);
        panel.getCmbBackgroundTileset().setModel(backgroundTileSetsModel);
        panel.getBtnApplyAll().addActionListener(e -> {
            applyTileToAll();
        });
        panel.getCmbBackgroundTileset().addItemListener(e -> {
            int state = e.getStateChange();
            if (state == ItemEvent.SELECTED) {
                TileSetListItem tileSetListItem = (TileSetListItem) e.getItem();
                selectBackgroundTileSet(tileSetListItem);
            }
        });
        panel.getCmbForegroundTileset().addItemListener(e -> {
            int state = e.getStateChange();
            if (state == ItemEvent.SELECTED) {
                TileSetListItem tileSetListItem = (TileSetListItem) e.getItem();
                selectForegroundTileSet(tileSetListItem);
            }
        });
        panel.getCbGrid().setSelected(true);
        panel.getCbGrid().addActionListener(e -> roomTileMapEditorView.setGrid(panel.getCbGrid().isSelected()));

        panel.getBtnSaveToServer().addActionListener(e -> {
            saveToServer();
        });
        panel.getChkBackground().setSelected(true);
        panel.getChkBackground().addActionListener(e -> {
            backgroundChecked(panel.getChkBackground().isSelected());
        });
        panel.getChkForeground().setSelected(true);
        panel.getChkForeground().addActionListener(e -> {
            foregroundChecked(panel.getChkForeground().isSelected());
        });
        panel.getCmbForegroundTiles().addActionListener(e -> {
            setForegroundMode(true);
        });
        panel.getCmbBackgroundTiles().addActionListener(e -> {
            setForegroundMode(false);
        });
        panel.getRadioBG().setSelected(!addTileToForeground);
        panel.getRadioBG().addActionListener(e -> {
            setForegroundMode(!panel.getRadioBG().isSelected());
        });
        panel.getRadioFG().setSelected(addTileToForeground);
        panel.getRadioFG().addActionListener(e -> {
            setForegroundMode(panel.getRadioFG().isSelected());
        });
    }

    private void setForegroundMode(boolean mode) {
        addTileToForeground = mode;
        panel.getRadioFG().setSelected(addTileToForeground);
        panel.getRadioBG().setSelected(!addTileToForeground);
    }

    private void foregroundChecked(boolean isSelected) {
        roomTileMapEditorView.scheduleAction(() -> {
            roomTileMapEditorView.setDrawForeGround(isSelected);
        });
    }

    private void backgroundChecked(boolean isSelected) {
        roomTileMapEditorView.scheduleAction(() -> {
            roomTileMapEditorView.setDrawBackground(isSelected);
        });
    }

    private void undo() {
        JOptionPane.showMessageDialog(panel, "Not implemented Yet, sorry ;-)");
    }

    private void selectForegroundTileSet(TileSetListItem tileSetListItem) {
        fgTileSet = tileSetListItem.getTileSet();
        roomTileMapEditorView.scheduleAction(() -> {
            roomTileMapEditorView.setForegroundTileSet(fgTileSet);
        });

        // Load the tiles
        SwingUtilities.invokeLater(() -> {
            panel.getCmbForegroundTiles().removeAllItems();
            for (Image image : fgTileSet.getTiles()) {
                Image iconImage = ImageUtils.resize(image, 60, 60);
                panel.getCmbForegroundTiles().addItem(new ImageIcon(iconImage));
            }
        });

    }


    private void selectBackgroundTileSet(TileSetListItem tileSetListItem) {
        bgTileSet = tileSetListItem.getTileSet();
        roomTileMapEditorView.scheduleAction(() -> {
            roomTileMapEditorView.setBackgroundTileSet(bgTileSet);
        });

        // Load the tiles
        SwingUtilities.invokeLater(() -> {
            panel.getCmbBackgroundTiles().removeAllItems();
            for (Image image : bgTileSet.getTiles()) {
                Image iconImage = ImageUtils.resize(image, 60, 60);
                panel.getCmbBackgroundTiles().addItem(new ImageIcon(iconImage));
            }
        });


    }

    private void saveToServer() {
        int[][] newBackgroundTilemap = roomTileMapEditorView.getBackgroundTileMap();
        int[][] newForegroundTilemap = roomTileMapEditorView.getForegroundTileMap();
        Long roomID = roomDto.getId();
        Long bgTileSetId = null;
        Long fgTileSetId = null;
        if ( bgTileSet != null ) bgTileSetId =  bgTileSet.getId();
        if ( fgTileSet != null )fgTileSetId = fgTileSet.getId();

        UpdateRoomDto updateRoomDto = new UpdateRoomDto(roomID, null, -1, -1, bgTileSetId, newBackgroundTilemap, fgTileSetId, newForegroundTilemap);
        connectionController.send(updateRoomDto);
    }

    private void applyTileToAll() {
        // Only use with background
        int tileIndex = panel.getCmbBackgroundTiles().getSelectedIndex();
        roomTileMapEditorView.scheduleAction(() -> {
            roomTileMapEditorView.applyTileToAllBackground(tileIndex);
        });
    }

    public void applyBackgroundTile(boolean delete, int row, int col) {
        int tileIndex;
        if ( !delete ) {
            tileIndex = panel.getCmbBackgroundTiles().getSelectedIndex();
        } else {
            tileIndex = -1;
        }

        roomTileMapEditorView.scheduleAction(() -> {
            roomTileMapEditorView.applyBackgroundTile(tileIndex, row, col);
        });
    }

    public void applyForegroundTile(boolean delete, int row, int col) {
        int tileIndex;
        if ( !delete ) {
             tileIndex = panel.getCmbForegroundTiles().getSelectedIndex();
        } else {
            tileIndex = -1;
        }
        roomTileMapEditorView.scheduleAction(() -> {
            roomTileMapEditorView.applyForegroundTile(tileIndex, row, col);
        });
    }

    private void setRoom(RoomDto roomDto) {
        this.roomDto = roomDto;
        if (roomDto != null) {
            if (roomDto.getBackTileSetId() != null) {
                Long bgTileSetId = roomDto.getBackTileSetId();
                bgTileSet = getTileSetFromCombobox(panel.getCmbBackgroundTileset(), bgTileSetId);
                setCmbSelectedItem(panel.getCmbBackgroundTileset(), bgTileSetId);
            } else {
                bgTileSet = null;
            }
            if (roomDto.getFrontTileSetId() != null) {
                Long fgTileSetId = roomDto.getFrontTileSetId();
                fgTileSet = getTileSetFromCombobox(panel.getCmbForegroundTileset(), fgTileSetId);
                setCmbSelectedItem(panel.getCmbForegroundTileset(), fgTileSetId);
            } else {
                fgTileSet = null;
            }
        } else {
            fgTileSet = null;
            bgTileSet = null;
        }
        roomTileMapEditorView.scheduleAction(() -> {
            roomTileMapEditorView.setBackgroundTileSet(bgTileSet);
            roomTileMapEditorView.setForegroundTileSet(fgTileSet);
            roomTileMapEditorView.setRoom(roomDto);
        });


    }

    private TileSet getTileSetFromCombobox(JComboBox<TileSetListItem> cmb, Long tileSetId) {
        TileSet found = null;
        for (int i = 0; i < cmb.getItemCount(); i++) {
            TileSetListItem tileSetListItem = cmb.getItemAt(i);
            if (tileSetListItem.getTileSet().getId().equals(tileSetId)) {
                found = tileSetListItem.getTileSet();
            }
        }
        return found;
    }

    private void setCmbSelectedItem(JComboBox<TileSetListItem> cmb, Long tileSetId) {
        int index = -1;
        for (int i = 0; i < cmb.getItemCount(); i++) {
            TileSetListItem tileSetListItem = cmb.getItemAt(i);
            if (tileSetListItem.getTileSet().getId().equals(tileSetId)) {
                index = i;
            }
        }
        if (index > -1 ) cmb.setSelectedIndex(index);
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
            panel.getCmbBackgroundTiles().removeAllItems();
            panel.getCmbBackgroundTileset().removeAllItems();
            panel.getCmbForegroundTiles().removeAllItems();
            panel.getCmbForegroundTileset().removeAllItems();
            setRoom(null);
            fgTileSet = null;
            bgTileSet = null;
        });
    }

    private void setEditable(boolean editable) {
        SwingUtilities.invokeLater(() -> {
        });
    }

    public void applyTile(boolean delete, int row, int col) {
        // Check foreGround or Background
        if (addTileToForeground) {
            applyForegroundTile(delete, row, col);
        } else {
            applyBackgroundTile(delete, row, col);
        }
    }

    public void reset() {
        setRoom(roomDto);
    }
}
