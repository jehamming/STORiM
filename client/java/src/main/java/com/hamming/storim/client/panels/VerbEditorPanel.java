package com.hamming.storim.client.panels;

import com.hamming.storim.common.Controllers;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.interfaces.VerbListener;
import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.verb.ExecVerbResultDTO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

public class VerbEditorPanel extends JPanel implements VerbListener, ConnectionListener {
    private JComboBox cmbVerbs;
    private DefaultComboBoxModel verbsModel;
    private JButton btnEdit;
    private JButton btnNew;
    private JButton btnDelete;
    private JPanel pnlCommands;
    private JTextField txtName;
    private JTextField txtToCaller;
    private JTextField txtToLocation;
    private JButton btnSave;
    private Controllers controllers;
    boolean newVerb = false;


    private class VerbListItem {
        private String name;
        private VerbDto verb;
        public VerbListItem(String name, VerbDto command) {
            this.name = name;
            this.verb = command;
        }
        public VerbDto getVerb() {
            return verb;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public VerbEditorPanel(Controllers controllers) {
        this.controllers = controllers;
        createPanel();
        controllers.getVerbController().addVerbListener(this);
        controllers.getConnectionController().addConnectionListener(this);
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
        cmbVerbs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                commandSelected();
            }
        });
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlCommands.add(cmbVerbs, gbc);

        btnEdit = new JButton();
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editVerb();
            }
        });
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        pnlCommands.add(btnEdit, gbc);

        btnNew = new JButton();
        btnNew.setText("New verb");
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newVerb();
            }
        });
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        pnlCommands.add(btnNew, gbc);

        btnDelete = new JButton();
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteVerb();
            }
        });
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
        txtName.setColumns(40);
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
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveVerb();
            }
        });
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        add(btnSave, gbc);

        verbsModel = (DefaultComboBoxModel) cmbVerbs.getModel();

        txtName.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                proposeText(txtName.getText().trim());
            }
        });
    }

    private void proposeText(String txt) {
        String toCaller = "You " + txt + " '${message}'";
        String toLocation =  "${caller} " + txt + "s '${message}'";
        txtToCaller.setText(toCaller);
        txtToLocation.setText(toLocation);
    }

    private void commandSelected() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VerbListItem item = (VerbListItem) verbsModel.getSelectedItem();
                if (item != null) {
                    btnEdit.setText("Edit " + item.getVerb().getName());
                    btnDelete.setText("Delete " + item.getVerb().getName());
                    setEditable(false);
                }
            }
        });
    }

    private void saveVerb() {
        if ( !allFieldsFilledIn() ) {
            JOptionPane.showMessageDialog(this, "Not all fields filled in commpletely!");
        } else {
            String name = txtName.getText().trim();
            String toCaller = txtToCaller.getText().trim();
            String toLocation = txtToLocation.getText().trim();
            if ( newVerb) {
                controllers.getVerbController().addVerb(name, toCaller, toLocation);
            } else {
                //Update verb
                VerbListItem item = (VerbListItem) cmbVerbs.getSelectedItem();
                VerbDto verb = item.getVerb();
                controllers.getVerbController().updateVerb(verb.getId(), name, toCaller, toLocation);
            }
        }
    }

    private boolean allFieldsFilledIn() {
        boolean nameOK = !txtName.getText().trim().equals("");
        boolean toCallerOK = !txtToCaller.getText().trim().equals("");
        boolean toLocationOK = !txtToLocation.getText().trim().equals("");
        return nameOK && toCallerOK && toLocationOK;
    }

    private void deleteVerb() {
        VerbListItem item = (VerbListItem) cmbVerbs.getSelectedItem();
        VerbDto verb = item.getVerb();
        controllers.getVerbController().deleteVerb(verb);
    }

    private void newVerb() {
        cleanTextFields();
        setEditable(true);
        newVerb = true;
    }

    private void editVerb() {
        VerbListItem item = (VerbListItem) cmbVerbs.getSelectedItem();
        VerbDto verb = item.getVerb();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtName.setText(verb.getName());
                txtToCaller.setText(verb.getToCaller());
                txtToLocation.setText(verb.getToLocation());
            }
        });
        setEditable(true);
        newVerb = false;
    }

    private void cleanTextFields() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtName.setText("");
                txtToCaller.setText("");
                txtToLocation.setText("");
            }
        });
    }

    private void setEditable(boolean editable) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtName.setEnabled(editable);
                txtToCaller.setEnabled(editable);
                txtToLocation.setEnabled(editable);
            }
        });
    }

    public void empty() {
        cleanTextFields();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                verbsModel.removeAllElements();
                btnEdit.setText("Edit");
                btnDelete.setText("Delete");
            }
        });
    }

    @Override
    public void verbReceived(VerbDto verb) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                removeVerb(verb.getId());
                verbsModel.addElement( new VerbListItem(verb.getName(), verb));
            }
        });
    }

    @Override
    public void verbDeleted(Long verbID) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                removeVerb(verbID);
            }
        });
    }

    @Override
    public void verbExecuted(ExecVerbResultDTO result) {

    }

    @Override
    public void connected() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                btnEdit.setEnabled(true);
                btnDelete.setEnabled(true);
                btnNew.setEnabled(true);
                btnSave.setEnabled(true);
            }
        });
    }

    @Override
    public void disconnected() {
        empty();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                btnEdit.setEnabled(false);
                btnDelete.setEnabled(false);
                btnNew.setEnabled(false);
                btnSave.setEnabled(false);
            }
        });
    }

    private void removeVerb(Long id) {
        VerbListItem found = null;
        for (int i = 0; i < verbsModel.getSize(); i++) {
            VerbListItem item = (VerbListItem) verbsModel.getElementAt(i);
            if (item != null && item.getVerb().getId().equals(id)) {
                found = item;
                break;
            }
        }
        if (found != null) {
            verbsModel.removeElement(found);
        }
    }

}
