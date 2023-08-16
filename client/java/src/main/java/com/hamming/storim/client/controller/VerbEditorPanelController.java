package com.hamming.storim.client.controller;

import com.hamming.storim.client.STORIMWindowController;
import com.hamming.storim.client.listitem.VerbDetailListItem;
import com.hamming.storim.client.panels.VerbEditorPanel;
import com.hamming.storim.common.ProtocolHandler;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.VerbDetailsDTO;
import com.hamming.storim.common.dto.protocol.request.AddVerbDto;
import com.hamming.storim.common.dto.protocol.request.DeleteVerbDTO;
import com.hamming.storim.common.dto.protocol.request.UpdateVerbDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetVerbDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetVerbResponseDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserVerbsDTO;
import com.hamming.storim.common.dto.protocol.serverpush.VerbAddedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.VerbDeletedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.VerbUpdatedDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class VerbEditorPanelController implements ConnectionListener {

    private ConnectionController connectionController;
    private VerbEditorPanel panel;
    private STORIMWindowController windowController;
    private DefaultComboBoxModel<VerbDetailListItem> verbsModel = new DefaultComboBoxModel<>();
    private boolean newVerb = false;

    public VerbEditorPanelController(STORIMWindowController windowController, VerbEditorPanel panel, ConnectionController connectionController) {
        this.panel = panel;
        this.windowController = windowController;
        this.connectionController = connectionController;
        connectionController.addConnectionListener(this);
        registerReceivers();
        setup();
    }

    private void registerReceivers() {
       connectionController.registerReceiver(VerbAddedDTO.class, (ProtocolReceiver<VerbAddedDTO>) dto -> verbAdded(dto));
       connectionController.registerReceiver(UserVerbsDTO.class, (ProtocolReceiver<UserVerbsDTO>) dto -> setVerbs(dto));
       connectionController.registerReceiver(VerbDeletedDTO.class, (ProtocolReceiver<VerbDeletedDTO>) dto -> verbDeleted(dto));
       connectionController.registerReceiver(VerbUpdatedDTO.class, (ProtocolReceiver<VerbUpdatedDTO>) dto -> verbUpdated(dto));
    }

    private void verbUpdated(VerbUpdatedDTO dto) {
        VerbDetailsDTO verbDetailsDTO = getVerb(dto.getVerb().getId());
        VerbDetailListItem item = findVerbDetailListItem(dto.getVerb().getId());
        if ( verbDetailsDTO != null &&  item != null ) {
            SwingUtilities.invokeLater(() -> {
                item.setVerb(verbDetailsDTO);
            });
        }

    }

    private void verbDeleted(VerbDeletedDTO dto) {
        Long verbId = dto.getVerbID();
        VerbDetailListItem item = findVerbDetailListItem(verbId);
        if ( item != null ) {
            SwingUtilities.invokeLater(() -> {
                verbsModel.removeElement(item);
            });
        }
    }

    private VerbDetailListItem findVerbDetailListItem(Long verbId) {
        VerbDetailListItem found = null;
        for (int i = 0; i < verbsModel.getSize() ; i++) {
            VerbDetailListItem item = verbsModel.getElementAt(i);
            if (item.getVerb().getId().equals( verbId )) {
                found = item;
                break;
            }
        }
        return found;
    }


    private void verbAdded(VerbAddedDTO dto) {
        VerbDetailsDTO verbDetailsDTO = getVerb(dto.getVerb().getId());
        if ( verbDetailsDTO != null ) {
            SwingUtilities.invokeLater(() -> {
                VerbDetailListItem item = new VerbDetailListItem(verbDetailsDTO);
                verbsModel.addElement(item);
            });
        }
    }

    private VerbDetailsDTO getVerb(Long id) {
        VerbDetailsDTO result = null;
        GetVerbDTO getVerbDTO = new GetVerbDTO(id);
        GetVerbResponseDTO getVerbResponseDTO = connectionController.sendReceive(getVerbDTO, GetVerbResponseDTO.class);
        if ( getVerbResponseDTO != null && getVerbResponseDTO.getVerb() != null ) {
            result = getVerbResponseDTO.getVerb();
        }
        return result;
    }

    private void setVerbs(UserVerbsDTO dto) {
        SwingUtilities.invokeLater(() -> {
            for (Long verbId : dto.getVerbs().keySet()) {
                VerbDetailsDTO verbDetailsDTO = getVerb(verbId);
                VerbDetailListItem item = new VerbDetailListItem(verbDetailsDTO);
                verbsModel.addElement(item);
            }
        });
    }



    private void setup() {
        panel.getCmbVerbs().addActionListener(e -> commandSelected());
        panel.getBtnEdit().addActionListener(e -> editVerb());
        panel.getBtnNew().addActionListener(e -> newVerb());
        panel.getBtnDelete().addActionListener(e -> deleteVerb());
        panel.getBtnSave().addActionListener(e -> saveVerb());
        verbsModel = (DefaultComboBoxModel) panel.getCmbVerbs().getModel();
        panel.getTxtName().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                proposeText(panel.getTxtName().getText().trim());
            }
        });
    }


    private void proposeText(String txt) {
        String toCaller = "You " + txt + " '${message}'";
        String toLocation =  "${caller} " + txt + "s '${message}'";
        panel.getTxtToCaller().setText(toCaller);
        panel.getTxtToLocation().setText(toLocation);
    }

    private void commandSelected() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VerbDetailListItem item = (VerbDetailListItem) verbsModel.getSelectedItem();
                if (item != null) {
                    panel.getBtnEdit().setText("Edit " + item.getVerb().getName());
                    panel.getBtnDelete().setText("Delete " + item.getVerb().getName());
                    panel.setEditable(false);
                }
            }
        });
    }

    private void saveVerb() {
        if ( !allFieldsFilledIn() ) {
            JOptionPane.showMessageDialog(panel, "Not all fields filled in commpletely!");
        } else {
            String name = panel.getTxtName().getText().trim();
            String toCaller = panel.getTxtToCaller().getText().trim();
            String toLocation = panel.getTxtToLocation().getText().trim();
            if (newVerb) {
                AddVerbDto addVerbDto = ProtocolHandler.getInstance().getAddVerbDTO(name,toCaller, toLocation);
                connectionController.send(addVerbDto);
            } else {
                //Update verb
                VerbDetailListItem item = (VerbDetailListItem) panel.getCmbVerbs().getSelectedItem();
                UpdateVerbDto updateVerbDto = ProtocolHandler.getInstance().getUpdateVerbDTO(item.getVerb().getId(), name, toCaller, toLocation);
                connectionController.send(updateVerbDto);
            }
        }
    }

    private boolean allFieldsFilledIn() {
        boolean nameOK = !panel.getTxtName().getText().trim().equals("");
        boolean toCallerOK = !panel.getTxtToCaller().getText().trim().equals("");
        boolean toLocationOK = !panel.getTxtToLocation().getText().trim().equals("");
        return nameOK && toCallerOK && toLocationOK;
    }

    private void deleteVerb() {
        VerbDetailListItem item = (VerbDetailListItem) panel.getCmbVerbs().getSelectedItem();
        DeleteVerbDTO deleteVerbDTO =  ProtocolHandler.getInstance().getDeleteVerbDTO(item.getVerb().getId());
        connectionController.send(deleteVerbDTO);
    }

    private void newVerb() {
        panel.cleanTextFields();
        panel.setEditable(true);
        newVerb = true;
    }

    private void editVerb() {
        VerbDetailListItem item = (VerbDetailListItem) panel.getCmbVerbs().getSelectedItem();
        VerbDetailsDTO verb = item.getVerb();
        SwingUtilities.invokeLater(() -> {
            panel.getTxtName().setText(verb.getName());
            panel.getTxtToCaller().setText(verb.getToCaller());
            panel.getTxtToLocation().setText(verb.getToLocation());
        });
        panel.setEditable(true);
        newVerb = false;
    }


    @Override
    public void connected() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel.getBtnEdit().setEnabled(true);
                panel.getBtnDelete().setEnabled(true);
                panel.getBtnNew().setEnabled(true);
                panel.getBtnSave().setEnabled(true);
            }
        });

    }

    @Override
    public void disconnected() {
        panel.empty();
        SwingUtilities.invokeLater(() -> {
            panel.getBtnEdit().setEnabled(false);
            panel.getBtnDelete().setEnabled(false);
            panel.getBtnNew().setEnabled(false);
            panel.getBtnSave().setEnabled(false);
        });
    }
}
