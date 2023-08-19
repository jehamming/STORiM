package com.hamming.storim.client.panels;

import com.hamming.storim.client.listitem.RoomDetailsListItem;
import com.hamming.storim.common.dto.TileDto;

import javax.swing.*;

/**
 *
 * @author jehamming
 */
public class RoomEditorPanel extends javax.swing.JPanel  {

    public RoomEditorPanel() {
        initComponents();
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        scrollPaneRooms = new javax.swing.JScrollPane();
        listRooms = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnTeleport = new javax.swing.JButton();
        btnCreate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        taRoomDescription = new javax.swing.JTextArea();
        tfRoomName = new javax.swing.JTextField();
        lblId = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtRows = new javax.swing.JTextField();
        txtCols = new javax.swing.JTextField();
        btnEditAuthorisation = new javax.swing.JButton();

        jLabel1.setText("Rooms");

        scrollPaneRooms.setViewportView(listRooms);

        jLabel2.setText("Room ID:");

        jLabel3.setText("Room Name");

        jLabel4.setText("Room Description");

        jLabel5.setText("Room Size :");

        btnTeleport.setText("Teleport");

        btnCreate.setText("Create");

        btnDelete.setText("Delete");

        btnSave.setText("Save");

        taRoomDescription.setColumns(20);
        taRoomDescription.setRows(5);
        jScrollPane1.setViewportView(taRoomDescription);

        tfRoomName.setText("Name");

        lblId.setText("-1");

        jLabel6.setText("Rows");

        jLabel7.setText("Cols");

        txtRows.setText("10");

        txtCols.setText("10");

        btnEditAuthorisation.setText("Change Authorisation");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(btnTeleport, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                        .addComponent(scrollPaneRooms, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnCreate)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnDelete)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnSave)
                                                .addContainerGap())
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel2)
                                                        .addComponent(jLabel3)
                                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(lblId)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(btnEditAuthorisation, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel7)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(txtCols))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel6)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(txtRows))
                                                        .addComponent(jScrollPane1)
                                                        .addComponent(tfRoomName)))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(scrollPaneRooms)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(26, 26, 26)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel2)
                                                        .addComponent(lblId)
                                                        .addComponent(btnEditAuthorisation))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel3)
                                                        .addComponent(tfRoomName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel5)
                                                        .addComponent(jLabel6)
                                                        .addComponent(txtRows, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel7)
                                                        .addComponent(txtCols, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(15, 43, Short.MAX_VALUE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnTeleport)
                                        .addComponent(btnCreate)
                                        .addComponent(btnDelete)
                                        .addComponent(btnSave)))
        );
    }// </editor-fold>


    // Variables declaration - do not modify
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEditAuthorisation;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnTeleport;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblId;
    private javax.swing.JList<RoomDetailsListItem> listRooms;
    private javax.swing.JScrollPane scrollPaneRooms;
    private javax.swing.JTextArea taRoomDescription;
    private javax.swing.JTextField tfRoomName;
    private javax.swing.JTextField txtCols;
    private javax.swing.JTextField txtRows;
    // End of variables declaration


    public JButton getBtnCreate() {
        return btnCreate;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JButton getBtnSave() {
        return btnSave;
    }

    public JButton getBtnTeleport() {
        return btnTeleport;
    }

    public JTextField getTxtCols() {
        return txtCols;
    }

    public JTextField getTxtRows() {
        return txtRows;
    }

    public JList<RoomDetailsListItem> getListRooms() {
        return listRooms;
    }

    public JTextArea getTaRoomDescription() {
        return taRoomDescription;
    }

    public JTextField getTfRoomName() {
        return tfRoomName;
    }

    public JLabel getLblId() {
        return lblId;
    }

    public JButton getBtnEditAuthorisation() {
        return btnEditAuthorisation;
    }
}