package com.hamming.storim.client.controller;

import com.hamming.storim.client.STORIMWindow;
import com.hamming.storim.client.listitem.VerbListItem;
import com.hamming.storim.client.panels.ChatPanel;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.protocol.request.ExecVerbDTO;
import com.hamming.storim.common.dto.protocol.serverpush.*;
import com.hamming.storim.common.dto.protocol.serverpush.VerbDeletedDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ChatPanelController implements ConnectionListener {

    private ConnectionController connectionController;
    private ChatPanel panel;
    private STORIMWindow storimWindow;
    private DefaultComboBoxModel<VerbListItem> verbsModel = new DefaultComboBoxModel<>();

    public ChatPanelController(STORIMWindow storimWindow, ChatPanel panel, ConnectionController connectionController) {
        this.panel = panel;
        this.storimWindow = storimWindow;
        this.connectionController = connectionController;
        connectionController.addConnectionListener(this);
        registerReceivers();
        setup();
    }

    private void registerReceivers() {
        connectionController.registerReceiver(SetRoomDTO.class, (ProtocolReceiver<SetRoomDTO>) dto -> setRoom(dto));
        connectionController.registerReceiver(UserInRoomDTO.class, (ProtocolReceiver<UserInRoomDTO>) dto -> userInRoom(dto));
        connectionController.registerReceiver(UserDisconnectedDTO.class, (ProtocolReceiver<UserDisconnectedDTO>) dto -> userDisconnected(dto));
        connectionController.registerReceiver(UserConnectedDTO.class, (ProtocolReceiver<UserConnectedDTO>) dto -> userConnected(dto));
        connectionController.registerReceiver(UserLeftRoomDTO.class, (ProtocolReceiver<UserLeftRoomDTO>) dto -> userLeftRoom(dto));
        connectionController.registerReceiver(UserEnteredRoomDTO.class, (ProtocolReceiver<UserEnteredRoomDTO>) dto -> userEnteredRoom(dto));
        connectionController.registerReceiver(UserVerbsDTO.class, (ProtocolReceiver<UserVerbsDTO>) dto -> setVerbs(dto));
        connectionController.registerReceiver(MessageInRoomDTO.class, (ProtocolReceiver<MessageInRoomDTO>) dto -> messageInRoom(dto));
        connectionController.registerReceiver(VerbAddedDTO.class, (ProtocolReceiver<VerbAddedDTO>) dto -> verbAdded(dto));
        connectionController.registerReceiver(VerbDeletedDTO.class, (ProtocolReceiver<VerbDeletedDTO>) dto -> verbDeleted(dto));
    }



    private void verbAdded(VerbAddedDTO dto) {
        SwingUtilities.invokeLater(() -> {
            VerbListItem item = new VerbListItem(dto.getVerb().getId(), dto.getVerb().getName());
            verbsModel.addElement(item);
        });
    }

    private void messageInRoom(MessageInRoomDTO dto) {
        addText(dto.getMessage());
    }

    private void setVerbs(UserVerbsDTO dto) {
        SwingUtilities.invokeLater(() -> {
            for (Long verbId : dto.getVerbs().keySet()) {
                String verbName = dto.getVerbs().get(verbId);
                VerbListItem item = new VerbListItem(verbId, verbName);
                verbsModel.addElement(item);
            }
        });
    }


    private void setup() {

        verbsModel = (DefaultComboBoxModel) panel.getCmbVerbs().getModel();

        panel.getBtnSend().addActionListener(e -> send());

        panel.getTfInput().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    send();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    private void send() {
        if (connectionController.isConnected()) {
            String txt = panel.getTfInput().getText().trim();
            VerbListItem item = (VerbListItem) panel.getCmbVerbs().getSelectedItem();
            connectionController.send(new ExecVerbDTO(item.getId(), txt));
            SwingUtilities.invokeLater(() -> {
                panel.getTfInput().setSelectionStart(0);
                int end = panel.getTfInput().getText().length();
                panel.getTfInput().setSelectionEnd(end);
            });
        }
    }

    public void verbDeleted(VerbDeletedDTO verb) {
        SwingUtilities.invokeLater(() -> removeVerb(verb.getVerbID()));
    }

    private void removeVerb(Long id) {
        VerbListItem found = null;
        for (int i = 0; i < verbsModel.getSize(); i++) {
            VerbListItem item = verbsModel.getElementAt(i);
            if (item != null && item.getId().equals(id)) {
                found = item;
                break;
            }
        }
        if (found != null) {
            verbsModel.removeElement(found);
        }
    }


    public void addText(String txt) {
        SwingUtilities.invokeLater(() -> panel.getChatOutput().append(txt + "\n"));
    }


    private void userConnected(UserConnectedDTO dto) {
        addText(dto.getName() + " connected.");
    }

    private void userDisconnected(UserDisconnectedDTO dto) {
        addText(dto.getUserName() + " disconnected.");
    }


    public void userInRoom(UserInRoomDTO userInRoomDTO) {
        addText(userInRoomDTO.getUser().getName() + " is in this room");
    }


    public void userEnteredRoom(UserEnteredRoomDTO userEnteredRoomDTO) {
        addText(userEnteredRoomDTO.getUser().getName() + " entered this room! (from " + userEnteredRoomDTO.getOldRoomName() + ")");
    }


    public void userLeftRoom(UserLeftRoomDTO userLeftRoomDTO) {
        addText(userLeftRoomDTO.getUserName() + " went to " + userLeftRoomDTO.getNewRoomName());
    }

    public void setRoom(SetRoomDTO setRoomDTO) {
        addText("You are in " + setRoomDTO.getRoom().getName());
    }


    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {
        panel.empty();
    }
}
