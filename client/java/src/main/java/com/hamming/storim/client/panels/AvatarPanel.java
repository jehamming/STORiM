package com.hamming.storim.client.panels;

import com.hamming.storim.common.interfaces.UserListener;

public class AvatarPanel extends javax.swing.JPanel  {
//    private JFileChooser fileChooser;
//    private DefaultListModel<AvatarListItem> avatarModel = new DefaultListModel<>();
//    private boolean newAvatar = false;
//    private Image avatarImage;
//    private AvatarDto selectedAvatar;
//
//    private class AvatarListItem {
//        private AvatarDto avatar;
//
//        public AvatarListItem(AvatarDto avatar) {
//            this.avatar = avatar;
//        }
//
//        public AvatarDto getAvatar() {
//            return avatar;
//        }
//
//        public void setAvatar(AvatarDto avatar) {
//            this.avatar = avatar;
//        }
//
//        @Override
//        public String toString() {
//            return avatar.getName();
//        }
//    }
//
//    public AvatarPanel(Controllers controllers) {
//        this.controllers = controllers;
//        this.fileChooser = new JFileChooser();
//        initComponents();
//        setup();
//        controllers.getUserController().addUserListener(this);
//    }
//
//    private void setup() {
//        listAvatars.setModel(avatarModel);
//        btnDelete.addActionListener(e -> deleteAvatar());
//        btnSave.addActionListener(e -> saveAvatar());
//        btnCreate.addActionListener(e -> createAvatar());
//        btnChooseFile.addActionListener(e -> chooseFile());
//        listAvatars.addListSelectionListener(e -> {
//            AvatarListItem item = listAvatars.getSelectedValue();
//            if (item != null && item.getAvatar()!= null) {
//                avatarSelected(item.getAvatar());
//            }
//        });
//        setEditable(false);
//        btnSave.setEnabled(false);
//        btnDelete.setEnabled(false);
//        btnCreate.setEnabled(false);
//        btnSetCurrent.setEnabled(false);
//        btnSetCurrent.addActionListener(e -> {
//            setCurrentAvatar();
//        });
//    }
//
//    private void setCurrentAvatar() {
//        if (selectedAvatar != null ) {
//            controllers.getUserController().setAvatarRequest(selectedAvatar);
//        }
//    }
//
//    private void avatarSelected(AvatarDto avatar) {
//        selectedAvatar = avatar;
//        SwingUtilities.invokeLater(() -> {
//            lblAvatarID.setText(avatar.getId().toString());
//            txtAvatarName.setText(avatar.getName());
//            btnSave.setEnabled(true);
//            btnDelete.setEnabled(true);
//            btnSetCurrent.setEnabled(true);
//            setEditable(true);
//            avatarImage = ImageUtils.decode(avatar.getImageData());
//            Image iconImage = avatarImage.getScaledInstance(lblImagePreview.getWidth(), lblImagePreview.getHeight(), Image.SCALE_SMOOTH);
//            lblImagePreview.setIcon(new ImageIcon(iconImage));
//        });
//
//    }
//
//    private void chooseFile() {
//        int returnVal = fileChooser.showOpenDialog(this);
//
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            try {
//                File file = fileChooser.getSelectedFile();
//                avatarImage = ImageIO.read(file);
//                SwingUtilities.invokeLater(() -> {
//                    Image iconImage = avatarImage.getScaledInstance(lblImagePreview.getWidth(), lblImagePreview.getHeight(), Image.SCALE_SMOOTH);
//                    lblImagePreview.setIcon(new ImageIcon(iconImage));
//                });
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void createAvatar() {
//        setEnabled(true);
//        newAvatar = true;
//        avatarImage = null;
//        SwingUtilities.invokeLater(() -> {
//            lblAvatarID.setText("");
//            txtAvatarName.setText("New AVATAR Name");
//            btnSave.setEnabled(true);
//            listAvatars.clearSelection();
//            selectedAvatar = null;
//            btnDelete.setEnabled(false);
//            lblImagePreview.setIcon(null);
//            setEditable(true);
//        });
//    }
//
//    private void saveAvatar() {
//        String avatarName = txtAvatarName.getText().trim();
//        if (avatarImage == null ) {
//            JOptionPane.showMessageDialog(this, "Please choose image! ");
//            return;
//        }
//        if (newAvatar) {
//            controllers.getUserController().addAvatarRequest(avatarName, ImageUtils.encode(avatarImage));
//        } else {
//            // Update !
//            Long avatarID = Long.valueOf(lblAvatarID.getText());
//            controllers.getUserController().updateAvatarRequest(avatarID, avatarName, ImageUtils.encode(avatarImage));
//        }
//
//        setEditable(false);
//        empty(false);
//        listAvatars.clearSelection();
//        selectedAvatar = null;
//        btnDelete.setEnabled(false);
//    }
//
//    private void deleteAvatar() {
//        Long avatarIDd = Long.valueOf(lblAvatarID.getText());
//        controllers.getUserController().deleteAvatar(avatarIDd);
//        empty(false);
//
//    }
//
//    private void setEditable(boolean editable) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                txtAvatarName.setEnabled(editable);
//                btnChooseFile.setEnabled(editable);
//            }
//        });
//    }
//
//    public void empty(boolean fully) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                lblAvatarID.setText("");
//                txtAvatarName.setText("");
//                lblImagePreview.setText("");
//                lblImagePreview.setIcon(null);
//                btnSave.setEnabled(false);
//                btnDelete.setEnabled(false);
//                btnChooseFile.setEnabled(false);
//                btnSetCurrent.setEnabled(false);
//                if (fully) {
//                    listAvatars.clearSelection();;
//                    avatarModel.removeAllElements();
//                    listAvatars.removeAll();
//                }
//            }
//        });
//        newAvatar = false;
//    }
//
//
//    @Override
//    public void userConnected(UserDto user) {
//
//    }
//
//    @Override
//    public void userUpdated(UserDto user) {
//
//    }
//
//    @Override
//    public void userDisconnected(UserDto user) {
//
//    }
//
//    @Override
//    public void userOnline(UserDto user) {
//
//    }
//
//    @Override
//    public void loginResult(boolean success, String message) {
//        if (success) {
//            empty(true);
//            btnCreate.setEnabled(true);
//            List<AvatarDto> avatars = controllers.getUserController().getCurrentUserAvatars();
//            for (AvatarDto avatar:avatars ) {
//                avatarAdded(avatar);
//            }
//        }
//    }
//
//    @Override
//    public void userTeleported(Long userId, LocationDto location) {
//
//    }
//
//    @Override
//    public void avatarAdded(AvatarDto avatar) {
//        SwingUtilities.invokeLater(() -> {
//            avatarModel.addElement(new AvatarListItem( avatar));
//        });
//    }
//
//    @Override
//    public void avatarDeleted(AvatarDto avatar) {
//        SwingUtilities.invokeLater(() -> {
//            AvatarListItem found = null;
//            for (int i = 0; i < avatarModel.getSize(); i++) {
//                AvatarListItem item = avatarModel.get(i);
//                if (item.getAvatar().getId().equals(avatar.getId())) {
//                    found = item;
//                    break;
//                }
//            }
//            if (found != null) {
//                avatarModel.removeElement(found);
//            }
//        });
//    }
//
//    @Override
//    public void avatarUpdated(AvatarDto avatar) {
//        SwingUtilities.invokeLater(() -> {
//            AvatarListItem found = null;
//            for (int i = 0; i < avatarModel.getSize(); i++) {
//                AvatarListItem item = avatarModel.get(i);
//                if (item.getAvatar().getId().equals(avatar.getId())) {
//                    item.setAvatar(avatar);
//                    break;
//                }
//            }
//            listAvatars.invalidate();
//            listAvatars.repaint();
//        });
//    }
//
//
//    private void initComponents() {
//
//        jScrollPane1 = new javax.swing.JScrollPane();
//        listAvatars = new javax.swing.JList<>();
//        jLabel7 = new javax.swing.JLabel();
//        jLabel8 = new javax.swing.JLabel();
//        btnCreate = new javax.swing.JButton();
//        btnDelete = new javax.swing.JButton();
//        lblAvatarID = new javax.swing.JLabel();
//        txtAvatarName = new javax.swing.JTextField();
//        btnSave = new javax.swing.JButton();
//        jLabel2 = new javax.swing.JLabel();
//        btnChooseFile = new javax.swing.JButton();
//        lblImagePreview = new javax.swing.JLabel();
//        btnSetCurrent = new javax.swing.JButton();
//
//        jScrollPane1.setViewportView(listAvatars);
//
//        jLabel7.setText("Avatar ID");
//
//        jLabel8.setText("Avatar Name");
//
//        btnCreate.setText("Create");
//
//        btnDelete.setText("Delete");
//
//        lblAvatarID.setText("jLabel1");
//
//        txtAvatarName.setText("jTextField1");
//
//        btnSave.setText("Save");
//
//        jLabel2.setText("Image");
//
//        btnChooseFile.setText("Choose file");
//
//        lblImagePreview.setText("Preview");
//
//        btnSetCurrent.setText("Set as Avatar");
//
//        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
//        this.setLayout(layout);
//        layout.setHorizontalGroup(
//                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addGroup(layout.createSequentialGroup()
//                                .addContainerGap()
//                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
//                                        .addGroup(layout.createSequentialGroup()
//                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
//                                                .addGap(47, 47, 47))
//                                        .addGroup(layout.createSequentialGroup()
//                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
//                                                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                                        .addGroup(layout.createSequentialGroup()
//                                                                .addComponent(btnCreate)
//                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                                                .addComponent(btnDelete)))
//                                                .addGap(18, 18, 18)))
//                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                        .addComponent(lblAvatarID, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(btnChooseFile, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(lblImagePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                        .addGroup(layout.createSequentialGroup()
//                                                .addComponent(btnSetCurrent)
//                                                .addGap(62, 62, 62)
//                                                .addComponent(btnSave))
//                                        .addComponent(txtAvatarName, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
//                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//        );
//        layout.setVerticalGroup(
//                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addGroup(layout.createSequentialGroup()
//                                .addContainerGap()
//                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
//                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                        .addGroup(layout.createSequentialGroup()
//                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                                                        .addComponent(jLabel7)
//                                                        .addComponent(lblAvatarID))
//                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                                                        .addComponent(jLabel8)
//                                                        .addComponent(txtAvatarName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                                        .addGroup(layout.createSequentialGroup()
//                                                                .addComponent(btnChooseFile)
//                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                                                .addComponent(lblImagePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
//                                                .addGap(38, 38, 38))
//                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                                                .addComponent(btnDelete)
//                                                .addComponent(btnCreate)
//                                                .addComponent(btnSave)
//                                                .addComponent(btnSetCurrent)))
//                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//        );
//    }
//
//
//    // Variables declaration - do not modify
//    private javax.swing.JButton btnChooseFile;
//    private javax.swing.JButton btnCreate;
//    private javax.swing.JButton btnDelete;
//    private javax.swing.JButton btnSetCurrent;
//    private javax.swing.JButton btnSave;
//    private javax.swing.JLabel jLabel2;
//    private javax.swing.JLabel jLabel3;
//    private javax.swing.JLabel jLabel7;
//    private javax.swing.JLabel jLabel8;
//    private javax.swing.JScrollPane jScrollPane1;
//    private javax.swing.JLabel lblAvatarID;
//    private javax.swing.JLabel lblImagePreview;
//    private JList<AvatarListItem> listAvatars;
//    private javax.swing.JTextField txtAvatarName;
//

    // End of variables declaration
}

