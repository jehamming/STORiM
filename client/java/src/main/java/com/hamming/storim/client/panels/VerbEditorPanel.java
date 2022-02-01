package com.hamming.storim.client.panels;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class VerbEditorPanel extends JPanel {
    private JComboBox cmbVerbs;
    private JButton btnEdit;
    private JButton btnNew;
    private JButton btnDelete;
    private JPanel pnlCommands;
    private JTextField txtName;
    private JTextField txtToCaller;
    private JTextField txtToLocation;
    private JButton btnSave;



    public VerbEditorPanel() {
        createPanel();
        setEditable(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnNew.setEnabled(false);
        btnSave.setEnabled(false);
    }

    private void createPanel() {
        setBorder(new TitledBorder("Verb Editor"));
        setLayout(new GridBagLayout());
        pnlCommands = new JPanel();
        pnlCommands.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(pnlCommands, gbc);
        cmbVerbs = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlCommands.add(cmbVerbs, gbc);
        btnEdit = new JButton();
        btnEdit.setText("Edit");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        pnlCommands.add(btnEdit, gbc);
        btnNew = new JButton();
        btnNew.setText("New verb");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        pnlCommands.add(btnNew, gbc);
        btnDelete = new JButton();
        btnDelete.setText("Delete");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlCommands.add(btnDelete, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(panel1, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Verb name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel1.add(label1, gbc);
        final JLabel label2 = new JLabel();
        txtName = new JTextField();
        txtName.setColumns(30);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(txtName, gbc);
        txtToCaller = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtToCaller, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Text shown to the Caller: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label3, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Text show to Location:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label4, gbc);
        txtToLocation = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtToLocation, gbc);
        btnSave = new JButton();
        btnSave.setText("Save");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        add(btnSave, gbc);
    }


    public void cleanTextFields() {
        SwingUtilities.invokeLater(() -> {
            txtName.setText("");
            txtToCaller.setText("");
            txtToLocation.setText("");
        });
    }

    public void setEditable(boolean editable) {
        SwingUtilities.invokeLater(() -> {
            txtName.setEnabled(editable);
            txtToCaller.setEnabled(editable);
            txtToLocation.setEnabled(editable);
        });
    }

    public void empty() {
        cleanTextFields();
        SwingUtilities.invokeLater(() -> {
            ((DefaultComboBoxModel) cmbVerbs.getModel()).removeAllElements();
            btnEdit.setText("Edit");
            btnDelete.setText("Delete");
        });
    }


    public JComboBox getCmbVerbs() {
        return cmbVerbs;
    }

    public JButton getBtnEdit() {
        return btnEdit;
    }

    public JButton getBtnNew() {
        return btnNew;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JPanel getPnlCommands() {
        return pnlCommands;
    }

    public JTextField getTxtName() {
        return txtName;
    }

    public JTextField getTxtToCaller() {
        return txtToCaller;
    }

    public JTextField getTxtToLocation() {
        return txtToLocation;
    }

    public JButton getBtnSave() {
        return btnSave;
    }
}
