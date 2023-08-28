package com.hamming.storim.client.controller;

import com.hamming.storim.client.STORIMWindowController;
import com.hamming.storim.client.listitem.VerbListItem;
import com.hamming.storim.client.panels.ChatPanel;
import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.protocol.request.ExecVerbDTO;
import com.hamming.storim.common.dto.protocol.serverpush.*;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ChatPanelController implements ConnectionListener {

    private ChatPanel panel;
    private STORIMWindowController windowController;
    private DefaultComboBoxModel<VerbListItem> verbsModel = new DefaultComboBoxModel<>();
    private SimpleAttributeSet attributeSet;
    private MicroServerProxy microServerProxy;

    public ChatPanelController(STORIMWindowController windowController, ChatPanel panel, MicroServerProxy microServerProxy) {
        this.panel = panel;
        this.windowController = windowController;
        this.microServerProxy = microServerProxy;
        microServerProxy.getConnectionController().addConnectionListener(this);
        registerReceivers();
        setup();
    }

    private void registerReceivers() {
        microServerProxy.getConnectionController().registerReceiver(SetRoomDTO.class, (ProtocolReceiver<SetRoomDTO>) dto -> setRoom(dto));
        microServerProxy.getConnectionController().registerReceiver(UserInRoomDTO.class, (ProtocolReceiver<UserInRoomDTO>) dto -> userInRoom(dto));
        microServerProxy.getConnectionController().registerReceiver(UserDisconnectedDTO.class, (ProtocolReceiver<UserDisconnectedDTO>) dto -> userDisconnected(dto));
        microServerProxy.getConnectionController().registerReceiver(UserConnectedDTO.class, (ProtocolReceiver<UserConnectedDTO>) dto -> userConnected(dto));
        microServerProxy.getConnectionController().registerReceiver(UserLeftRoomDTO.class, (ProtocolReceiver<UserLeftRoomDTO>) dto -> userLeftRoom(dto));
        microServerProxy.getConnectionController().registerReceiver(UserEnteredRoomDTO.class, (ProtocolReceiver<UserEnteredRoomDTO>) dto -> userEnteredRoom(dto));
        //microServerProxy.getConnectionController().registerReceiver(UserVerbsDTO.class, (ProtocolReceiver<UserVerbsDTO>) dto -> setVerbs(dto));
        microServerProxy.getConnectionController().registerReceiver(MessageInRoomDTO.class, (ProtocolReceiver<MessageInRoomDTO>) dto -> messageInRoom(dto));
        microServerProxy.getConnectionController().registerReceiver(VerbAddedDTO.class, (ProtocolReceiver<VerbAddedDTO>) dto -> verbAdded(dto));
        microServerProxy.getConnectionController().registerReceiver(VerbDeletedDTO.class, (ProtocolReceiver<VerbDeletedDTO>) dto -> verbDeleted(dto));
    }



    private void verbAdded(VerbAddedDTO dto) {
        SwingUtilities.invokeLater(() -> {
            VerbListItem item = new VerbListItem(dto.getVerb().getId(), dto.getVerb().getName());
            verbsModel.addElement(item);
        });
    }

    private void messageInRoom(MessageInRoomDTO dto) {
        addText(dto.getMessage());
        if ( !dto.getSourceID().equals(windowController.getCurrentUser().getId()) ) {
            windowController.getWindow().setTitle(dto.getMessage());
        } else {
            windowController.getWindow().setTitle(windowController.getCurrentUser().getName());
        }
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

        attributeSet = new SimpleAttributeSet();
        StyleConstants.setBold(attributeSet, false);

        // Set the attributes before adding text
        panel.getTextPaneChatOutput().setCharacterAttributes(attributeSet, true);

        DefaultCaret caret = (DefaultCaret) panel.getTextPaneChatOutput().getCaret();
        caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
    }

    private void send() {
        if (microServerProxy.getConnectionController().isConnected()) {
            String txt = panel.getTfInput().getText().trim();
            VerbListItem item = (VerbListItem) panel.getCmbVerbs().getSelectedItem();

            microServerProxy.executeVerb(item.getId(), txt);

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
        SwingUtilities.invokeLater(() -> {
            Document doc = panel.getTextPaneChatOutput().getDocument();
            try {
                doc.insertString(doc.getLength(), txt + "\n", attributeSet);
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        });
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
        SwingUtilities.invokeLater(() -> {
            panel.getCmbVerbs().removeAllItems();
            panel.getTextPaneChatOutput().setText("");
        });
    }
}
