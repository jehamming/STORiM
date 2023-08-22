package com.hamming.storim.client.controller;

import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.client.STORIMWindowController;
import com.hamming.storim.client.listitem.ThingListItem;
import com.hamming.storim.client.panels.ThingPanel;
import com.hamming.storim.common.MicroServerException;
import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.AddThingDto;
import com.hamming.storim.common.dto.protocol.request.DeleteThingDTO;
import com.hamming.storim.common.dto.protocol.request.PlaceThingInRoomDTO;
import com.hamming.storim.common.dto.protocol.request.UpdateThingDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetThingDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetThingResultDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetThingsForUserDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetThingsForUserResponseDTO;
import com.hamming.storim.common.dto.protocol.serverpush.*;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ThingPanelController implements ConnectionListener {

    private MicroServerProxy microServerProxy;
    private ThingPanel panel;
    private STORIMWindowController windowController;

    private JFileChooser fileChooser;
    private BufferedImage tileImage;
    private DefaultListModel<ThingListItem> thingsModel = new DefaultListModel<>();

    private boolean newThing = false;
    private Image thingImage;
    private UserDto currentUser;
    private RoomDto currentRoom;


    public ThingPanelController(STORIMWindowController windowController, ThingPanel panel, MicroServerProxy microServerProxy) {
        this.panel = panel;
        this.windowController = windowController;
        this.microServerProxy = microServerProxy;
        this.fileChooser = new JFileChooser();
        microServerProxy.getConnectionController().addConnectionListener(this);
        registerReceivers();
        setup();
        empty(true);
    }


    private void registerReceivers() {
        microServerProxy.getConnectionController().registerReceiver(SetCurrentUserDTO.class, (ProtocolReceiver<SetCurrentUserDTO>) dto -> setCurrentUser(dto));
        microServerProxy.getConnectionController().registerReceiver(ThingAddedDTO.class, (ProtocolReceiver<ThingAddedDTO>) dto -> thingAdded(dto.getThing()));
        microServerProxy.getConnectionController().registerReceiver(ThingDeletedDTO.class, (ProtocolReceiver<ThingDeletedDTO>) dto -> thingDeleted(dto.getThingId()));
        microServerProxy.getConnectionController().registerReceiver(ThingUpdatedDTO.class, (ProtocolReceiver<ThingUpdatedDTO>) dto -> thingUpdated(dto.getThing()));
        microServerProxy.getConnectionController().registerReceiver(SetRoomDTO.class, (ProtocolReceiver<SetRoomDTO>) dto -> setRoom(dto.getRoom()));
    }

    private void setRoom(RoomDto room) {
        currentRoom = room;
    }

    private void setCurrentUser(SetCurrentUserDTO dto) {
        currentUser = dto.getUser();
        try {
            List<Long> thingIds = microServerProxy.getThingsForUser(currentUser.getId());
            for (Long thingId : thingIds) {
                ThingDto thing = microServerProxy.getThing(thingId);
                thingAdded(thing);
            }
        } catch (MicroServerException e) {
            throw new RuntimeException(e);
        }
    }

    private void setup() {
        panel.getListThings().setModel(thingsModel);
        panel.getBtnDelete().addActionListener(e -> deleteThing());
        panel.getBtnPlace().addActionListener(e -> placeThingInRoom());
        panel.getBtnSave().addActionListener(e -> saveThing());
        panel.getBtnCreate().addActionListener(e -> createThing());
        panel.getBtnChooseFile().addActionListener(e -> chooseFile());
        panel.getListThings().addListSelectionListener(e -> {
            ThingListItem item = panel.getListThings().getSelectedValue();
            if (item != null && item.getThing() != null) {
                thingSelected(item.getThing());
            }
        });
        setEditable(false);
        panel.getBtnSave().setEnabled(false);
        panel.getBtnDelete().setEnabled(false);
        panel.getBtnPlace().setEnabled(false);
        panel.getBtnCreate().setEnabled(false);
    }

    private void placeThingInRoom() {
        Long thingID = Long.valueOf(panel.getLblID().getText().trim());
        microServerProxy.placeThingInRoom(thingID, currentRoom.getId());
    }

    private void thingSelected(ThingDto thing) {
        SwingUtilities.invokeLater(() -> {
            panel.getLblID().setText(thing.getId().toString());
            panel.getTxtName().setText(thing.getName());
            panel.getTaDescription().setText(thing.getDescription());
            panel.getSlScale().setValue((int) (thing.getScale() * 100));
            panel.getSlRotation().setValue(thing.getRotation());
            panel.getBtnSave().setEnabled(true);
            panel.getBtnDelete().setEnabled(true);
            panel.getBtnPlace().setEnabled(true);
            setEditable(true);
            thingImage = ImageUtils.decode(thing.getImageData());
            Image iconImage = thingImage.getScaledInstance(panel.getLblImagePreview().getWidth(), panel.getLblImagePreview().getHeight(), Image.SCALE_SMOOTH);
            panel.getLblImagePreview().setIcon(new ImageIcon(iconImage));
        });
    }

    private void chooseFile() {
        int returnVal = fileChooser.showOpenDialog(panel);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                thingImage = ImageIO.read(file);
                SwingUtilities.invokeLater(() -> {
                    Image iconImage = thingImage.getScaledInstance(panel.getLblImagePreview().getWidth(), panel.getLblImagePreview().getHeight(), Image.SCALE_SMOOTH);
                    panel.getLblImagePreview().setIcon(new ImageIcon(iconImage));
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createThing() {
        panel.setEnabled(true);
        newThing = true;
        thingImage = null;
        SwingUtilities.invokeLater(() -> {
            panel.getLblID().setText("");
            panel.getTxtName().setText("New THING name");
            panel.getTaDescription().setText("New THING Description");
            panel.getSlScale().setValue(100);
            panel.getSlRotation().setValue(0);
            panel.getBtnSave().setEnabled(true);
            panel.getListThings().clearSelection();
            panel.getBtnDelete().setEnabled(false);
            panel.getBtnPlace().setEnabled(false);
            panel.getLblImagePreview().setIcon(null);
            setEditable(true);
        });
    }

    private void saveThing() {
        String thingName = panel.getTxtName().getText().trim();
        String thingDescription = panel.getTaDescription().getText();
        Float thingScale = (float) panel.getSlScale().getValue() / 100;
        Integer thingRotation = panel.getSlRotation().getValue();
        if (thingImage == null) {
            JOptionPane.showMessageDialog(panel, "Please choose image! ");
            return;
        }
        if (newThing) {
            byte[] imageData = ImageUtils.encode(thingImage);
            microServerProxy.addThing(thingName, thingDescription, thingScale, thingRotation, imageData);
            setEditable(false);
            empty(false);
            panel.getListThings().clearSelection();
            panel.getBtnDelete().setEnabled(false);
            panel.getBtnPlace().setEnabled(false);
        } else {
            // Update !
            Long thingId = Long.valueOf(panel.getLblID().getText());
            microServerProxy.updateThing(thingId, thingName, thingDescription, thingScale, thingRotation, ImageUtils.encode(thingImage));
        }

    }

    private void deleteThing() {
        Long thingID = Long.valueOf(panel.getLblID().getText());
        microServerProxy.deleteThing(thingID);
        empty(false);
    }

    private void setEditable(boolean editable) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel.getTxtName().setEnabled(editable);
                panel.getTaDescription().setEnabled(editable);
                panel.getBtnChooseFile().setEnabled(editable);
                panel.getSlRotation().setEnabled(editable);
                panel.getSlScale().setEnabled(editable);
            }
        });
    }

    public void empty(boolean fully) {
        SwingUtilities.invokeLater(() -> {
            panel.getLblID().setText("");
            panel.getTxtName().setText("");
            panel.getTaDescription().setText("");
            panel.getLblImagePreview().setText("");
            panel.getLblImagePreview().setIcon(null);
            panel.getBtnSave().setEnabled(false);
            panel.getBtnDelete().setEnabled(false);
            panel.getBtnPlace().setEnabled(false);
            panel.getBtnChooseFile().setEnabled(false);
            if (fully) {
                panel.getListThings().clearSelection();
                ;
                thingsModel.removeAllElements();
                panel.getListThings().removeAll();
            }
        });
        newThing = false;
    }

    public void thingAdded(ThingDto thing) {
        SwingUtilities.invokeLater(() -> {
            thingsModel.addElement(new ThingListItem(thing));
        });
    }

    public void thingDeleted(Long thingId) {
        SwingUtilities.invokeLater(() -> {
            ThingListItem found = null;
            for (int i = 0; i < thingsModel.getSize(); i++) {
                ThingListItem item = thingsModel.get(i);
                if (item.getThing().getId().equals(thingId)) {
                    found = item;
                    break;
                }
            }
            if (found != null) {
                thingsModel.removeElement(found);
            }
        });
    }
    public void thingDeleted(ThingDto thing) {
        SwingUtilities.invokeLater(() -> {
            ThingListItem found = null;
            for (int i = 0; i < thingsModel.getSize(); i++) {
                ThingListItem item = thingsModel.get(i);
                if (item.getThing().getId().equals(thing.getId())) {
                    found = item;
                    break;
                }
            }
            if (found != null) {
                thingsModel.removeElement(found);
            }
        });
    }

    public void thingUpdated(ThingDto thing) {
        SwingUtilities.invokeLater(() -> {
            ThingListItem found = null;
            for (int i = 0; i < thingsModel.getSize(); i++) {
                ThingListItem item = thingsModel.get(i);
                if (item.getThing().getId().equals(thing.getId())) {
                    item.setThing(thing);
                    break;
                }
            }
            panel.getListThings().invalidate();
            panel.getListThings().repaint();
        });
    }

    private ThingListItem getItem(ThingDto thing) {
        ThingListItem found = null;
        for (int i = 0; i < thingsModel.getSize(); i++) {
            ThingListItem item = thingsModel.get(i);
            if (item.getThing().equals(thing)) {
                found = item;
                break;
            }
        }
        return found;
    }

    public void thingSelectedInView(ThingDto thing) {
        SwingUtilities.invokeLater(() -> {
            ThingListItem item = getItem(thing);
            if (item != null) {
                panel.getListThings().setSelectedValue(item, true);
            }
        });
    }


    @Override
    public void connected() {
        empty(true);
        panel.getBtnCreate().setEnabled(true);
    }

    @Override
    public void disconnected() {
        currentRoom = null;
        currentUser = null;
    }
}
