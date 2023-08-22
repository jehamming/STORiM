package com.hamming.storim.client.controller.admin;

import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.client.listitem.RoomListItem;
import com.hamming.storim.client.listitem.ShortUserListItem;
import com.hamming.storim.client.listitem.TileSetEditorListItem;
import com.hamming.storim.client.listitem.TileSetListItem;
import com.hamming.storim.client.panels.ServerConfigurationPanel;
import com.hamming.storim.client.view.TileSet;
import com.hamming.storim.common.MicroServerException;
import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.dto.ServerConfigurationDTO;
import com.hamming.storim.common.dto.TileSetDto;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginResultDTO;
import com.hamming.storim.common.dto.protocol.serverpush.RoomAddedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.RoomDeletedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.TileSetAddedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.TileSetDeletedDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerConfigurationController implements ConnectionListener {
    private ServerConfigurationPanel panel;
    private DefaultListModel<TileSetEditorListItem> tileSetModel = new DefaultListModel<>();
    private DefaultListModel<RoomListItem> roomsModel = new DefaultListModel<>();
    private DefaultListModel<ShortUserListItem> adminsModel = new DefaultListModel<>();
    private DefaultListModel<ShortUserListItem> searchModel = new DefaultListModel<>();
    private MicroServerProxy microServerProxy;
    private ServerConfigurationDTO serverConfigurationDTO;

    public ServerConfigurationController(ServerConfigurationPanel panel, MicroServerProxy microServerProxy) {
        this.microServerProxy = microServerProxy;
        this.panel = panel;
        microServerProxy.getConnectionController().addConnectionListener(this);
        setup();
        registerReceivers();
    }

    private void registerReceivers() {
        microServerProxy.getConnectionController().registerReceiver(LoginResultDTO.class, (ProtocolReceiver<LoginResultDTO>) dto -> loginResult(dto));
        microServerProxy.getConnectionController().registerReceiver(TileSetAddedDTO.class, (ProtocolReceiver<TileSetAddedDTO>) dto -> addTileSet(dto));
        microServerProxy.getConnectionController().registerReceiver(TileSetDeletedDTO.class, (ProtocolReceiver<TileSetDeletedDTO>) dto -> removeTileSet(dto));
        microServerProxy.getConnectionController().registerReceiver(RoomAddedDTO.class, (ProtocolReceiver<RoomAddedDTO>) dto -> addRoom(dto));
        microServerProxy.getConnectionController().registerReceiver(RoomDeletedDTO.class, (ProtocolReceiver<RoomDeletedDTO>) dto -> removeRoom(dto));
    }

    private void removeRoom(RoomDeletedDTO dto) {
        SwingUtilities.invokeLater(() -> {
            int index = findRoomIndex(dto.getRoomId());
            if ( index != -1 ) panel.getCmbDefaultRoom().remove(index);
        });
    }

    private void addRoom(RoomAddedDTO dto) {
        int index = findRoomIndex(dto.getRoom().getId());
        if ( index == -1 ) {
            SwingUtilities.invokeLater(() -> {
               RoomListItem roomListItem = new RoomListItem(dto.getRoom().getId(),  dto.getRoom().getName());
               panel.getCmbDefaultRoom().addItem(roomListItem);
            });
        }
    }

    private void removeTileSet(TileSetDeletedDTO dto) {
        SwingUtilities.invokeLater(() -> {
            int index = findTileSetIndex(dto.getId());
            if ( index != -1 ) panel.getCmbDefaultTileSet().remove(index);
        });
    }

    private void addTileSet(TileSetAddedDTO dto) {
        int index = findTileSetIndex(dto.getTileSetDto().getId());
        if ( index == -1 ) {
            SwingUtilities.invokeLater(() -> {
                TileSet tileSet = new TileSet((dto.getTileSetDto()));
                TileSetListItem item = new TileSetListItem(tileSet);
                panel.getCmbDefaultTileSet().addItem(item);
            });
        }
    }

    private void loginResult(LoginResultDTO dto) {
        if (dto.isSuccess() && dto.isServerAdmin()) {
            fillServerConfigurationDetails();
            enable(true);
        }
    }

    private void fillServerConfigurationDetails() {
        try {
            ServerConfigurationDTO serverConfigurationDTO = microServerProxy.getServerConfiguration();
            fillAdminsList(serverConfigurationDTO.getServerAdmins());
            fillRoomComboboxes();
            fillTileSetComboboxes();
            setValues(serverConfigurationDTO);
        } catch (MicroServerException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillRoomComboboxes() {
        try {
            HashMap<Long, String> rooms = microServerProxy.getRooms();
            for (Long roomId : rooms.keySet()) {
                String roomName = rooms.get(roomId);
                RoomListItem roomListItem = new RoomListItem(roomId, roomName);
                panel.getCmbDefaultRoom().addItem(roomListItem);
            }
        } catch (MicroServerException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillTileSetComboboxes() {
        try {
            List<Long> tileSetIds = microServerProxy.getTileSets();
            for (Long tileSetId : tileSetIds) {
                TileSetDto tileSetDto = microServerProxy.getTileSet(tileSetId);
                TileSet tileSet = new TileSet(tileSetDto);
                TileSetListItem item = new TileSetListItem(tileSet);
                panel.getCmbDefaultTileSet().addItem(item);
            }
        } catch (MicroServerException e) {
            throw new RuntimeException(e);
        }
    }

    private void setValues(ServerConfigurationDTO config) {
        SwingUtilities.invokeLater(() -> {
            panel.getLblServerName().setText(config.getServerName());
            Long roomId = config.getDefaultRoomId();
            int roomIndex = findRoomIndex(roomId);
            if (roomIndex != -1) {
                panel.getCmbDefaultRoom().setSelectedIndex(roomIndex);
            }
            Long tileSetId = config.getDefaultTileSetId();
            int tileSetIndex = findTileSetIndex(tileSetId);
            if (tileSetIndex != -1) {
                panel.getCmbDefaultTileSet().setSelectedIndex(tileSetIndex);
            }
            int selectedTile = config.getDefaultTile();
            panel.getCmbTIle().setSelectedIndex(selectedTile);
        });

    }

    private int findRoomIndex(Long roomId) {
        int index = -1;
        for (int i = 0; i < panel.getCmbDefaultRoom().getItemCount(); i++) {
            if (panel.getCmbDefaultRoom().getItemAt(i).getId().equals(roomId)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private int findTileSetIndex(Long tileSetId) {
        int index = -1;
        for (int i = 0; i < panel.getCmbDefaultTileSet().getItemCount(); i++) {
            if (panel.getCmbDefaultTileSet().getItemAt(i).getTileSet().getId().equals(tileSetId)) {
                index = i;
                break;
            }
        }
        return index;
    }


    public void setup() {
        panel.getLstServerAdmins().setModel(adminsModel);
        panel.getCmbDefaultTileSet().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                TileSetListItem selectedItem = (TileSetListItem) panel.getCmbDefaultTileSet().getSelectedItem();
                if ( selectedItem != null) {
                    setSelectedTileSet(selectedItem.getTileSet());
                }
            }
        });

        setupAdminsPart();
    }

    private void setupAdminsPart() {
        panel.getTxtSearch().setText("");
        panel.getLstServerAdmins().setModel(adminsModel);
        panel.getListSearchResults().setModel(searchModel);
        panel.getBtnSearch().addActionListener(e -> searchUsers());
        panel.getBtnSaveToServer().addActionListener(e -> saveToServer());
        panel.getBtnAdd().addActionListener(e -> addToAdmins());
        panel.getBtnRemove().addActionListener(e -> removeFromAdmins());
        panel.getLstServerAdmins().addListSelectionListener(e -> {
            panel.getBtnRemove().setEnabled(true);
        });
        panel.getListSearchResults().addListSelectionListener(e -> {
            panel.getBtnAdd().setEnabled(true);
        });
        panel.getTxtSearch().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchUsers();
                }
            }

        });
        panel.getListSearchResults().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    addToAdmins();
                }
            }
        });
        panel.getLstServerAdmins().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    removeFromAdmins();
                }
            }
        });
        panel.getBtnRemove().setEnabled(false);
        panel.getBtnAdd().setEnabled(false);
    }

    private void fillAdminsList(List<Long> list) {
        for (Long id : list) {
            String name = getName(id);
            ShortUserListItem item = new ShortUserListItem(id, name);
            adminsModel.addElement(item);
        }
    }

    private String getName(Long userID) {
        String name = "";
        try {
            name = microServerProxy.getUser(userID).getName();
        } catch (MicroServerException e) {
            JOptionPane.showMessageDialog(panel, e.getMessage());
        }
        return name;
    }

    private void saveToServer() {
        TileSetListItem tileSetListItem = (TileSetListItem) panel.getCmbDefaultTileSet().getSelectedItem();
        Long defaultTileSetId = tileSetListItem.getTileSet().getId();
        int defaultTile = panel.getCmbTIle().getSelectedIndex();
        RoomListItem roomListItem = (RoomListItem) panel.getCmbDefaultRoom().getSelectedItem();
        Long defaultRoomId = roomListItem.getId();
        List<Long> serverAdmins = getAdminIds();

        if ( defaultTileSetId != null && defaultTile != -1 && defaultRoomId != null && serverAdmins != null){
            // Send update to Server
            microServerProxy.updateServerConfiguration(defaultTileSetId, defaultTile, defaultRoomId, serverAdmins);
        }
    }

    private List<Long> getAdminIds() {
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < adminsModel.getSize(); i++) {
            ShortUserListItem item = adminsModel.getElementAt(i);
            list.add(item.getUserId());
        }
        return list;
    }

    private void addToAdmins() {
        if (!panel.getListSearchResults().isSelectionEmpty()) {
            ShortUserListItem item = panel.getListSearchResults().getSelectedValue();
            if (!containsAdmin(item)) {
                adminsModel.addElement(item);
            }
        }
    }

    private void removeFromAdmins() {
        if (!panel.getLstServerAdmins().isSelectionEmpty()) {
            int index = panel.getLstServerAdmins().getSelectedIndex();
            adminsModel.removeElementAt(index);
        }
    }

    private boolean containsAdmin(ShortUserListItem item) {
        boolean found = false;
        for (int i = 0; i < adminsModel.getSize(); i++) {
            ShortUserListItem shortUserListItem = adminsModel.getElementAt(i);
            if (shortUserListItem.getUserId().equals(item.getUserId())) {
                found = true;
                break;
            }
        }
        return found;
    }

    private void setSelectedTileSet(TileSet tileSet) {
        SwingUtilities.invokeLater(() -> {
            panel.getCmbTIle().removeAllItems();
            for (Image tile : tileSet.getTiles()) {
                Image tileImageIcon = ImageUtils.resize(tile, 60, 60);
                panel.getCmbTIle().addItem(new ImageIcon(tileImageIcon));
            }
        });
    }

    private void searchUsers() {
        String searchText = panel.getTxtSearch().getText().trim();
        if (searchText != null && !searchText.equals("")) {
            HashMap<Long, String> users = searchUsers(searchText);
            searchModel.removeAllElements();
            for (Long id : users.keySet()) {
                String name = users.get(id);
                ShortUserListItem item = new ShortUserListItem(id, name);
                searchModel.addElement(item);
            }
        } else {
            JOptionPane.showMessageDialog(panel, "Please enter 'some' text");
        }
    }

    private HashMap<Long, String> searchUsers(String searchText) {
        HashMap<Long, String> found = new HashMap<>();
        try {
            found = microServerProxy.findUsersByDisplayname(searchText);
        } catch (MicroServerException e) {
            JOptionPane.showMessageDialog(panel, e.getMessage());
        }
        return found;
    }

    public void empty() {
        SwingUtilities.invokeLater(() -> {
            panel.getLblServerName().setText("");
            panel.getCmbDefaultRoom().removeAllItems();
            panel.getCmbDefaultTileSet().removeAllItems();
            panel.getCmbTIle().removeAllItems();
            panel.getTxtSearch().setText("");
            adminsModel.removeAllElements();
            searchModel.removeAllElements();
        });
        enable(false);
    }

    private void enable(boolean enabled) {
        SwingUtilities.invokeLater(() -> {
            panel.getBtnSaveToServer().setEnabled(enabled);
            panel.getBtnSearch().setEnabled(enabled);
        });
    }

    @Override
    public void connected() {
        empty();
    }

    @Override
    public void disconnected() {
        empty();
    }
}
