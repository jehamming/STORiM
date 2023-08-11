package com.hamming.storim.client.panels;

import com.hamming.storim.client.listitem.TileSetListItem;

import javax.swing.*;

/**
 *
 * @author jehamming
 */
public class RoomTileEditorPanel extends javax.swing.JPanel {

    /**
     * Creates new form RoomTileEditorViewPanel
     */
    public RoomTileEditorPanel(JPanel viewPanel) {
        this.ViewPanel = viewPanel;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        ControlsPanel = new javax.swing.JPanel();
        cmbTileset = new javax.swing.JComboBox<>();
        cmbTiles = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        txtRow = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtCol = new javax.swing.JTextField();
        btnApply = new javax.swing.JButton();
        btnApplyAll = new javax.swing.JButton();
        cbGrid = new javax.swing.JCheckBox();
        btnRevert = new javax.swing.JButton();
        btnSaveToServer = new javax.swing.JButton();

        javax.swing.GroupLayout ViewPanelLayout = new javax.swing.GroupLayout(ViewPanel);
        ViewPanel.setLayout(ViewPanelLayout);
        ViewPanelLayout.setHorizontalGroup(
                ViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 548, Short.MAX_VALUE)
        );
        ViewPanelLayout.setVerticalGroup(
                ViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel1.setText("Row:");

        txtRow.setText("01");

        jLabel2.setText("Col:");

        txtCol.setText("01");

        btnApply.setText("Apply to Row/Col");

        btnApplyAll.setText("Apply to All");

        cbGrid.setText("Grid");

        btnRevert.setText("Revert all Changes");

        btnSaveToServer.setText("Save to Server");

        javax.swing.GroupLayout ControlsPanelLayout = new javax.swing.GroupLayout(ControlsPanel);
        ControlsPanel.setLayout(ControlsPanelLayout);
        ControlsPanelLayout.setHorizontalGroup(
                ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ControlsPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnApplyAll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cmbTiles, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cmbTileset, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnApply, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(ControlsPanelLayout.createSequentialGroup()
                                                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtCol)
                                                        .addComponent(txtRow)))
                                        .addComponent(cbGrid)
                                        .addComponent(btnRevert, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnSaveToServer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ControlsPanelLayout.setVerticalGroup(
                ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ControlsPanelLayout.createSequentialGroup()
                                .addComponent(cmbTileset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbTiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnApplyAll)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cbGrid)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtRow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(txtCol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnApply)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRevert)
                                .addGap(20, 20, 20)
                                .addComponent(btnSaveToServer))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(ControlsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ViewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(ControlsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(14, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(ViewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>





    // Variables declaration - do not modify
    private javax.swing.JPanel ControlsPanel;
    private javax.swing.JPanel ViewPanel;
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnApplyAll;
    private javax.swing.JComboBox<ImageIcon> cmbTiles;
    private javax.swing.JComboBox<TileSetListItem> cmbTileset;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField txtCol;
    private javax.swing.JTextField txtRow;
    private javax.swing.JCheckBox cbGrid;
    private javax.swing.JButton btnRevert;
    private javax.swing.JButton btnSaveToServer;
    // End of variables declaration


    public JButton getBtnApply() {
        return btnApply;
    }

    public JButton getBtnApplyAll() {
        return btnApplyAll;
    }


    public JComboBox<ImageIcon> getCmbTiles() {
        return cmbTiles;
    }

    public JComboBox<TileSetListItem> getCmbTileset() {
        return cmbTileset;
    }

    public JTextField getTxtCol() {
        return txtCol;
    }

    public JTextField getTxtRow() {
        return txtRow;
    }

    public JCheckBox getCbGrid() {
        return cbGrid;
    }

    public JButton getBtnRevert() {
        return btnRevert;
    }

    public JButton getBtnSaveToServer() {
        return btnSaveToServer;
    }
}
