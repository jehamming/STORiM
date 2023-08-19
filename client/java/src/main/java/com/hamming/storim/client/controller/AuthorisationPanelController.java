package com.hamming.storim.client.controller;

import com.hamming.storim.client.listitem.ShortUserListItem;
import com.hamming.storim.client.panels.AuthorisationPanel;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.BasicObjectDTO;
import com.hamming.storim.common.dto.protocol.request.UpdateAuthorisationDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUserDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUserResultDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.SearchUsersRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.SearchUsersResultDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuthorisationPanelController implements ConnectionListener {

    private ConnectionController connectionController;
    private AuthorisationPanel panel;
    private DefaultComboBoxModel<ShortUserListItem> editorsModel = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<ShortUserListItem> searchResultsModel = new DefaultComboBoxModel<>();
    private String objectTitle;
    private JFrame frame;
    private Component parent;
    private BasicObjectDTO dto;

    private AuthorisationPanelController(Component parent, BasicObjectDTO dto, AuthorisationPanel panel, ConnectionController connectionController) {
        this.panel = panel;
        this.dto = dto;
        this.connectionController = connectionController;
        this.objectTitle =  "(" + dto.getId() + ")"+ dto.getName();
        this.parent = parent;
        connectionController.addConnectionListener(this);
        setup();
    }

    private void show() {
        //Prepare Frame
        frame = new JFrame("Edit Authorisation:" + objectTitle);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(parent);
    }

    public static void showAuthorisationPanel(Component parent, BasicObjectDTO dto, ConnectionController connectionController) {
        AuthorisationPanel panel = new AuthorisationPanel();
        AuthorisationPanelController authorisationPanelController = new AuthorisationPanelController(parent, dto, panel, connectionController);
        authorisationPanelController.show();
    }


    private void setup() {
        panel.getLblName().setText(objectTitle);
        String owner =  "(" + dto.getOwnerID() + ")" + getName(dto.getOwnerID());
        panel.getLblOwner().setText(owner);
        panel.getTxtSearchUserName().setText("");
        panel.getListEditors().setModel(editorsModel);
        panel.getListSearchResults().setModel(searchResultsModel);
        panel.getBtnSearch().addActionListener(e -> searchUsers());
        panel.getBtnCancel().addActionListener(e -> cancel());
        panel.getBtnSave().addActionListener(e -> save());
        panel.getBtnAddToEditors().addActionListener(e -> addToEditors());
        panel.getBtnRemoveFromEditors().addActionListener(e -> removeFromEditors());
        panel.getListEditors().addListSelectionListener(e -> {
            panel.getBtnRemoveFromEditors().setEnabled(true);
        });
        panel.getListSearchResults().addListSelectionListener(e -> {
            panel.getBtnAddToEditors().setEnabled(true);
        });
        panel.getTxtSearchUserName().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchUsers();
                }
            }

        });
        panel.getListSearchResults().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    addToEditors();
                }
            }
        });
        panel.getListEditors().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    removeFromEditors();
                }
            }
        });
        fillEditorList(dto.getEditors());
    }

    private void fillEditorList(List<Long> list) {
        for (Long id : list) {
            String name = getName(id);
            ShortUserListItem item = new ShortUserListItem(id, name);
            editorsModel.addElement(item);
        }
    }

    private void save() {
        List<Long> newEditorIds = getEditorIds();
        // Send update to Server
        UpdateAuthorisationDto uaDto = new UpdateAuthorisationDto(dto.getId(), dto.getClass().getSimpleName(), newEditorIds);
        connectionController.send(uaDto);
        frame.dispose();

    }

    private List<Long> getEditorIds() {
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < editorsModel.getSize() ; i++) {
            ShortUserListItem item = editorsModel.getElementAt(i);
            list.add(item.getUserId());
        }
        return list;
    }

    private void removeFromEditors() {
        if ( !panel.getListEditors().isSelectionEmpty() ) {
            int index = panel.getListEditors().getSelectedIndex();
            editorsModel.removeElementAt(index);
        }
    }

    private void addToEditors() {
        if ( !panel.getListSearchResults().isSelectionEmpty() ) {
            ShortUserListItem item  = panel.getListSearchResults().getSelectedValue();
            if ( ! containsEditor( item ) ) {
                editorsModel.addElement(item);
            }
        }
    }

    private boolean containsEditor(ShortUserListItem item) {
        boolean found = false;
        for (int i = 0; i < editorsModel.getSize() ; i++) {
            ShortUserListItem shortUserListItem = editorsModel.getElementAt(i);
            if ( shortUserListItem.getUserId().equals( item.getUserId())) {
                found = true;
                break;
            }
        }
        return found;
    }

    private void cancel() {
        frame.dispose();
    }


    private void searchUsers() {
        String searchText = panel.getTxtSearchUserName().getText().trim();
        if (searchText != null && !searchText.equals("")) {
            HashMap<Long, String> users = searchUsers(searchText);
            searchResultsModel.removeAllElements();
            for (Long id : users.keySet()) {
                String name = users.get(id);
                ShortUserListItem item = new ShortUserListItem(id, name);
                searchResultsModel.addElement(item);
            }
        } else {
            JOptionPane.showMessageDialog(panel, "Please enter 'some' text");
        }
    }

    private HashMap<Long, String> searchUsers(String searchText) {
        HashMap<Long, String> foundUsers = new HashMap<>();
        SearchUsersRequestDTO request = new SearchUsersRequestDTO(searchText);
        SearchUsersResultDTO result = connectionController.sendReceive(request, SearchUsersResultDTO.class);
        if (result.isSuccess()) {
            foundUsers = result.getUsers();
        }
        return foundUsers;
    }


    private String getName(Long userID) {
        String name = "";
        GetUserDTO getUserDTO = new GetUserDTO(userID);
        GetUserResultDTO getUserResultDTO = connectionController.sendReceive(getUserDTO, GetUserResultDTO.class);
        if (getUserResultDTO.isSuccess()) {
            name = getUserResultDTO.getUser().getName();
        }
        return name;
    }


    @Override
    public void connected() {
        SwingUtilities.invokeLater(() ->
                setEnabled(false));
    }

    @Override
    public void disconnected() {
        empty();
        setEnabled(false);
    }

    private void empty() {
        SwingUtilities.invokeLater(() -> {
            panel.getLblName().setText("");
            panel.getLblOwner().setText("");
            panel.getTxtSearchUserName().setText("");
            editorsModel.removeAllElements();
            searchResultsModel.removeAllElements();
        });
    }

    public void setEnabled(boolean enabled) {
        SwingUtilities.invokeLater(() -> {
            panel.getTxtSearchUserName().setEnabled(enabled);
            panel.getBtnSearch().setEnabled(enabled);
            panel.getBtnCancel().setEnabled(enabled);
            panel.getBtnSave().setEnabled(enabled);
        });
    }
}
