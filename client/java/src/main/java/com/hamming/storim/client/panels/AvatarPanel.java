package com.hamming.storim.client.panels;

import com.hamming.storim.client.listitem.AvatarListItem;

import javax.swing.*;

public class AvatarPanel extends javax.swing.JPanel  {



    public AvatarPanel() {
         initComponents();
    }


    public void setEditable(boolean editable) {
        SwingUtilities.invokeLater(() -> {
            txtAvatarName.setEnabled(editable);
            btnChooseFile.setEnabled(editable);
        });
    }

    public void empty(boolean fully) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                lblAvatarID.setText("");
                txtAvatarName.setText("");
                lblImagePreview.setText("");
                lblImagePreview.setIcon(null);
                btnSave.setEnabled(false);
                btnDelete.setEnabled(false);
                btnChooseFile.setEnabled(false);
                btnSetCurrent.setEnabled(false);
                if (fully) {
                    listAvatars.clearSelection();;
                    ((DefaultListModel) listAvatars.getModel()).removeAllElements();
                    listAvatars.removeAll();
                }
            }
        });

    }


    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        listAvatars = new javax.swing.JList<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnCreate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        lblAvatarID = new javax.swing.JLabel();
        txtAvatarName = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnChooseFile = new javax.swing.JButton();
        lblImagePreview = new javax.swing.JLabel();
        btnSetCurrent = new javax.swing.JButton();

        jScrollPane1.setViewportView(listAvatars);

        jLabel7.setText("Avatar ID");

        jLabel8.setText("Avatar Name");

        btnCreate.setText("Create");

        btnDelete.setText("Delete");

        lblAvatarID.setText("jLabel1");

        txtAvatarName.setText("jTextField1");

        btnSave.setText("Save");

        jLabel2.setText("Image");

        btnChooseFile.setText("Choose file");

        lblImagePreview.setText("Preview");

        btnSetCurrent.setText("Set as Avatar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(47, 47, 47))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(btnCreate)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(btnDelete)))
                                                .addGap(18, 18, 18)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblAvatarID, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnChooseFile, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblImagePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnSetCurrent)
                                                .addGap(62, 62, 62)
                                                .addComponent(btnSave))
                                        .addComponent(txtAvatarName, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel7)
                                                        .addComponent(lblAvatarID))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel8)
                                                        .addComponent(txtAvatarName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(btnChooseFile)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(lblImagePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(38, 38, 38))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(btnDelete)
                                                .addComponent(btnCreate)
                                                .addComponent(btnSave)
                                                .addComponent(btnSetCurrent)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }


    // Variables declaration - do not modify
    private javax.swing.JButton btnChooseFile;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnSetCurrent;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAvatarID;
    private javax.swing.JLabel lblImagePreview;
    private JList<AvatarListItem> listAvatars;
    private javax.swing.JTextField txtAvatarName;
    // End of variables declaration


    public JButton getBtnChooseFile() {
        return btnChooseFile;
    }

    public JButton getBtnCreate() {
        return btnCreate;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JButton getBtnSetCurrent() {
        return btnSetCurrent;
    }

    public JButton getBtnSave() {
        return btnSave;
    }

    public JLabel getLblAvatarID() {
        return lblAvatarID;
    }

    public JLabel getLblImagePreview() {
        return lblImagePreview;
    }

    public JList<AvatarListItem> getListAvatars() {
        return listAvatars;
    }

    public JTextField getTxtAvatarName() {
        return txtAvatarName;
    }
}

