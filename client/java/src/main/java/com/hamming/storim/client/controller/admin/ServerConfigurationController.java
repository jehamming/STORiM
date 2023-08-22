package com.hamming.storim.client.controller.admin;

import com.hamming.storim.client.listitem.RoomListItem;
import com.hamming.storim.client.listitem.ShortUserListItem;
import com.hamming.storim.client.listitem.TileSetEditorListItem;
import com.hamming.storim.client.panels.ServerConfigurationPanel;
import com.hamming.storim.common.MicroServerException;
import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.ServerConfigurationDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetServerConfigDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetServerConfigResultDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginResultDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

public class ServerConfigurationController implements ConnectionListener {
    private ServerConfigurationPanel panel;
    private DefaultListModel<TileSetEditorListItem> tileSetModel = new DefaultListModel<>();
    private DefaultListModel<RoomListItem> roomsModel = new DefaultListModel<>();
    private DefaultListModel<ShortUserListItem> adminsModel = new DefaultListModel<>();
    private MicroServerProxy microServerProxy;

    public ServerConfigurationController(ServerConfigurationPanel panel, MicroServerProxy microServerProxy) {
        this.microServerProxy = microServerProxy;
        this.panel = panel;
        microServerProxy.getConnectionController().addConnectionListener(this);
        setup();
        registerReceivers();
    }

    private void registerReceivers() {
        microServerProxy.getConnectionController().registerReceiver(LoginResultDTO.class, (ProtocolReceiver<LoginResultDTO>) dto -> loginResult(dto));
    }

    private void loginResult(LoginResultDTO dto) {
        if ( dto.isSuccess() && dto.isAdmin() ) {
            fillServerConfigurationDetails();
        }
    }

    private void fillServerConfigurationDetails() {
        try {
            ServerConfigurationDTO serverConfigurationDTO = microServerProxy.getServerConfiguration();
            fillComboboxes();
            setValues(serverConfigurationDTO);
        } catch (MicroServerException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillComboboxes() {
        try {
            HashMap<Long, String> rooms = microServerProxy.getRooms();
            for (Long roomId: rooms.keySet()) {
                String roomName = rooms.get(roomId);
                RoomListItem roomListItem = new RoomListItem(roomId, roomName);
                panel.getCmbDefaultRoom().addItem(roomListItem);
            }
        } catch (MicroServerException e) {
            throw new RuntimeException(e);
        }
    }

    private void setValues(ServerConfigurationDTO config) {
        SwingUtilities.invokeLater(() -> {
            panel.getLblServerName().setText(config.getServerName());
            Long roomId = config.getDefaultRoomId();
            int index = findRoomIndex(roomId);
            if ( index != -1 ) {
                panel.getCmbDefaultRoom().setSelectedIndex(index);
            }

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


    public void setup() {
        panel.getLstServerAdmins().setModel(adminsModel);
    }


    private void save() {

    }

    public void empty() {
        SwingUtilities.invokeLater(() -> {
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
