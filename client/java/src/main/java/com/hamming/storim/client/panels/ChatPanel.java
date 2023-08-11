package com.hamming.storim.client.panels;


import com.hamming.storim.client.listitem.VerbListItem;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ChatPanel extends JPanel  {

    /**
     * Creates new form ChatPanel
     */
    public ChatPanel() {
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

        jScrollPane1 = new javax.swing.JScrollPane();
        textPaneChatOutput = new javax.swing.JTextPane();
        cmbVerbs = new javax.swing.JComboBox<>();
        tfInput = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();

        jScrollPane1.setViewportView(textPaneChatOutput);

        btnSend.setText("Send");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbVerbs, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfInput, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSend)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tfInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmbVerbs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSend)))
        );
    }// </editor-fold>


    // Variables declaration - do not modify
    private javax.swing.JButton btnSend;
    private javax.swing.JComboBox<VerbListItem> cmbVerbs;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane textPaneChatOutput;
    private javax.swing.JTextField tfInput;
    // End of variables declaration


    public JButton getBtnSend() {
        return btnSend;
    }

    public JComboBox<VerbListItem> getCmbVerbs() {
        return cmbVerbs;
    }

    public JTextPane getTextPaneChatOutput() {
        return textPaneChatOutput;
    }

    public JTextField getTfInput() {
        return tfInput;
    }
}
