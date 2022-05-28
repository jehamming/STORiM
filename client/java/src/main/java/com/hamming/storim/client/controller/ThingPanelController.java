package com.hamming.storim.client.controller;

import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.client.STORIMWindow;
import com.hamming.storim.client.listitem.ThingListItem;
import com.hamming.storim.client.panels.ThingPanel;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.interfaces.ConnectionListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ThingPanelController implements ConnectionListener {

    private ConnectionController connectionController;
    private ThingPanel panel;
    private STORIMWindow storimWindow;

    private JFileChooser fileChooser;
    private BufferedImage tileImage;
    private DefaultListModel<ThingListItem> thingsModel = new DefaultListModel<>();
    private boolean newThing = false;
    private Image thingImage;


    public ThingPanelController(STORIMWindow storimWindow, ThingPanel panel, ConnectionController connectionController) {
        this.panel = panel;
        this.storimWindow = storimWindow;
        this.connectionController = connectionController;
        this.fileChooser = new JFileChooser();
        connectionController.addConnectionListener(this);
        registerReceivers();
        setup();
        empty(true);
    }


    private void registerReceivers() {

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
        //TODO PLace THing
        Long thingID = Long.valueOf(panel.getLblID().getText().trim());
//        Long roomId = controllers.getUserController().getCurrentUserLocation().getRoomId();
//        controllers.getThingController().placeThingInRoomRequest(thingID, roomId);
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
            //TODO New thing
            //controllers.getThingController().addThingRequest(thingName, thingDescription, thingScale, thingRotation, ImageUtils.encode(thingImage));
            setEditable(false);
            empty(false);
            panel.getListThings().clearSelection();
            panel.getBtnDelete().setEnabled(false);
            panel.getBtnPlace().setEnabled(false);
        } else {
            // Update !
            Long thingId = Long.valueOf(panel.getLblID().getText());
            //TODO Update thing
            //controllers.getThingController().updateThingRequest(thingId, thingName, thingDescription, thingScale, thingRotation, ImageUtils.encode(thingImage));
        }

    }

    private void deleteThing() {
        Long thingID = Long.valueOf(panel.getLblID().getText());
        //TODO Delete thing
        // controllers.getThingController().deleteThingRequest(thingID);
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
        //TODO Get things of current user
//        List<ThingDto> things = controllers.getThingController().getThings(controllers.getUserController().getCurrentUser().getId());
//        for (ThingDto thing : things) {
//            thingAdded(thing);
//        }

    }

    @Override
    public void disconnected() {

    }
}
