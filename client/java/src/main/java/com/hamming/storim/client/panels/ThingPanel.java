package com.hamming.storim.client.panels;

import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.common.Controllers;
import com.hamming.storim.common.interfaces.ThingListener;
import com.hamming.storim.common.interfaces.UserListener;
import com.hamming.storim.common.interfaces.ViewListener;
import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.UserDto;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ThingPanel extends javax.swing.JPanel implements ThingListener, UserListener, ViewListener {

    private Controllers controllers;
    private JFileChooser fileChooser;
    private DefaultListModel<ThingListItem> thingsModel = new DefaultListModel<>();
    private boolean newThing = false;
    private Image thingImage;



    private class ThingListItem {
        private ThingDto thing;

        public ThingListItem(ThingDto thing) {
            this.thing = thing;
        }

        public ThingDto getThing() {
            return thing;
        }

        public void setThing(ThingDto thing) {
            this.thing = thing;
        }

        @Override
        public String toString() {
            return thing.getName();
        }
    }


    public ThingPanel(Controllers controllers) {
        this.controllers = controllers;
        this.fileChooser = new JFileChooser();
        initComponents();
        setup();
        empty(true);
        controllers.getThingController().addThingListener(this);
        controllers.getUserController().addUserListener(this);
        controllers.getViewerController().addViewListener(this);
    }

    private void setup() {
        listThings.setModel(thingsModel);
        btnDelete.addActionListener(e -> deleteThing());
        btnPlace.addActionListener(e -> placeThingInRoom());
        btnSave.addActionListener(e -> saveThing());
        btnCreate.addActionListener(e -> createThing());
        btnChooseFile.addActionListener(e -> chooseFile());
        listThings.addListSelectionListener(e -> {
            ThingListItem item = listThings.getSelectedValue();
            if (item != null && item.getThing() != null) {
                thingSelected(item.getThing());
            }
        });
        setEditable(false);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
        btnPlace.setEnabled(false);
        btnCreate.setEnabled(false);
    }

    private void placeThingInRoom() {
        //TODO PLace THing
        Long thingID = Long.valueOf(lblID.getText().trim());
        Long roomId = controllers.getUserController().getCurrentUserLocation().getRoomId();
        controllers.getThingController().placeThingInRoomRequest(thingID, roomId);
    }

    private void thingSelected(ThingDto thing) {
        SwingUtilities.invokeLater(() -> {
            lblID.setText(thing.getId().toString());
            txtName.setText(thing.getName());
            taDescription.setText(thing.getDescription());
            slScale.setValue((int) (thing.getScale() * 100));
            slRotation.setValue(thing.getRotation());
            btnSave.setEnabled(true);
            btnDelete.setEnabled(true);
            btnPlace.setEnabled(true);
            setEditable(true);
            thingImage = ImageUtils.decode(thing.getImageData());
            Image iconImage = thingImage.getScaledInstance(lblImagePreview.getWidth(), lblImagePreview.getHeight(), Image.SCALE_SMOOTH);
            lblImagePreview.setIcon(new ImageIcon(iconImage));
        });
    }

    private void chooseFile() {
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                thingImage = ImageIO.read(file);
                SwingUtilities.invokeLater(() -> {
                    Image iconImage = thingImage.getScaledInstance(lblImagePreview.getWidth(), lblImagePreview.getHeight(), Image.SCALE_SMOOTH);
                    lblImagePreview.setIcon(new ImageIcon(iconImage));
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createThing() {
        setEnabled(true);
        newThing = true;
        thingImage = null;
        SwingUtilities.invokeLater(() -> {
            lblID.setText("");
            txtName.setText("New THING name");
            taDescription.setText("New THING Description");
            slScale.setValue(100);
            slRotation.setValue(0);
            btnSave.setEnabled(true);
            listThings.clearSelection();
            btnDelete.setEnabled(false);
            btnPlace.setEnabled(false);
            lblImagePreview.setIcon(null);
            setEditable(true);
        });
    }

    private void saveThing() {
        String thingName = txtName.getText().trim();
        String thingDescription = taDescription.getText();
        Float thingScale =  (float) slScale.getValue() / 100;
        Integer thingRotation = slRotation.getValue();
        if (thingImage == null) {
            JOptionPane.showMessageDialog(this, "Please choose image! ");
            return;
        }
        if (newThing) {
            controllers.getThingController().addThingRequest(thingName, thingDescription, thingScale, thingRotation, ImageUtils.encode(thingImage));
            setEditable(false);
            empty(false);
            listThings.clearSelection();
            btnDelete.setEnabled(false);
            btnPlace.setEnabled(false);
        } else {
            // Update !
            Long thingId = Long.valueOf(lblID.getText());
            controllers.getThingController().updateThingRequest(thingId, thingName, thingDescription, thingScale, thingRotation, ImageUtils.encode(thingImage));
        }

    }

    private void deleteThing() {
        Long thingID = Long.valueOf(lblID.getText());
        controllers.getThingController().deleteThingRequest(thingID);
        empty(false);
    }

    private void setEditable(boolean editable) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtName.setEnabled(editable);
                taDescription.setEnabled(editable);
                btnChooseFile.setEnabled(editable);
                slRotation.setEnabled(editable);
                slScale.setEnabled(editable);
            }
        });
    }

    public void empty(boolean fully) {
        SwingUtilities.invokeLater(() -> {
            lblID.setText("");
            txtName.setText("");
            taDescription.setText("");
            lblImagePreview.setText("");
            lblImagePreview.setIcon(null);
            btnSave.setEnabled(false);
            btnDelete.setEnabled(false);
            btnPlace.setEnabled(false);
            btnChooseFile.setEnabled(false);
            if (fully) {
                listThings.clearSelection();
                ;
                thingsModel.removeAllElements();
                listThings.removeAll();
            }
        });
        newThing = false;
    }

    @Override
    public void thingAdded(ThingDto thing) {
        SwingUtilities.invokeLater(() -> {
            thingsModel.addElement(new ThingListItem(thing));
        });
    }

    @Override
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

    @Override
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
            listThings.invalidate();
            listThings.repaint();
        });
    }


    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        listThings = new javax.swing.JList<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnCreate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        lblID = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnChooseFile = new javax.swing.JButton();
        lblImagePreview = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        taDescription = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnPlace = new javax.swing.JButton();
        btnTake = new javax.swing.JButton();
        slScale = new javax.swing.JSlider();
        slRotation = new javax.swing.JSlider();

        jScrollPane1.setViewportView(listThings);

        jLabel7.setText("ID");

        jLabel8.setText("Name");

        btnCreate.setText("Create");

        btnDelete.setText("Delete");

        lblID.setText("jLabel1");

        txtName.setText("jTextField1");

        btnSave.setText("Save");

        jLabel2.setText("Image");

        btnChooseFile.setText("Choose file");

        lblImagePreview.setText("Preview");

        jLabel1.setText("Description");

        taDescription.setColumns(20);
        taDescription.setRows(5);
        jScrollPane2.setViewportView(taDescription);

        jLabel3.setText("Scale");

        jLabel4.setText("Rotation");

        btnPlace.setText("Place");

        btnTake.setText("Take");

        slScale.setMaximum(150);
        slScale.setValue(100);

        slRotation.setMaximum(360);
        slRotation.setValue(0);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(jLabel4)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnCreate)
                                                .addGap(4, 4, 4)
                                                .addComponent(btnDelete)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnPlace)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnTake)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnSave))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(btnChooseFile, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(slScale, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                                                                        .addComponent(slRotation, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                                                .addGap(18, 18, 18)
                                                                .addComponent(lblImagePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblID, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(64, 64, 64)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblID)
                                                        .addComponent(jLabel7))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel8))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(lblImagePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                        .addComponent(btnChooseFile)
                                                                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        .addComponent(slScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(jLabel4)
                                                                                        .addComponent(slRotation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(btnSave)
                                                                        .addComponent(btnDelete)
                                                                        .addComponent(btnCreate)
                                                                        .addComponent(btnPlace)
                                                                        .addComponent(btnTake)))
                                                        .addComponent(jLabel1))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>


    // Variables declaration - do not modify
    private javax.swing.JButton btnChooseFile;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnPlace;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnTake;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblImagePreview;
    private javax.swing.JList<ThingListItem> listThings;
    private javax.swing.JSlider slRotation;
    private javax.swing.JSlider slScale;
    private javax.swing.JTextArea taDescription;
    private javax.swing.JTextField txtName;


    @Override
    public void userConnected(UserDto user) {

    }

    @Override
    public void userUpdated(UserDto user) {

    }

    @Override
    public void userDisconnected(UserDto user) {

    }

    @Override
    public void userOnline(UserDto user) {

    }

    @Override
    public void loginResult(boolean success, String message) {
        if (success) {
            empty(true);
            btnCreate.setEnabled(true);
            List<ThingDto> things = controllers.getThingController().getThings(controllers.getUserController().getCurrentUser().getId());
            for (ThingDto thing : things) {
                thingAdded(thing);
            }
        }
    }

    @Override
    public void userTeleported(Long userId, LocationDto location) {

    }

    @Override
    public void avatarAdded(AvatarDto avatar) {

    }

    @Override
    public void avatarDeleted(AvatarDto avatar) {

    }

    @Override
    public void avatarUpdated(AvatarDto avatar) {

    }

    @Override
    public void userSelectedInView(UserDto user) {

    }

    @Override
    public void thingSelectedInView(ThingDto thing) {
        SwingUtilities.invokeLater(() -> {
            ThingListItem item = getItem(thing);
            if ( item != null ) {
                listThings.setSelectedValue(item, true);
            }
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
}

