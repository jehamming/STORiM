package com.hamming.storim.client.controller;

import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.client.STORIMWindow;
import com.hamming.storim.client.listitem.AvatarListItem;
import com.hamming.storim.client.panels.AvatarPanel;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.protocol.request.AddAvatarDto;
import com.hamming.storim.common.dto.protocol.request.DeleteAvatarDTO;
import com.hamming.storim.common.dto.protocol.request.SetAvatarDto;
import com.hamming.storim.common.dto.protocol.request.UpdateAvatarDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetAvatarDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetAvatarResponseDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetAvatarsDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetAvatarsResponseDTO;
import com.hamming.storim.common.dto.protocol.serverpush.SetCurrentUserDTO;
import com.hamming.storim.common.dto.protocol.serverpush.AvatarAddedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.AvatarDeletedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.AvatarUpdatedDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class AvatarPanelController implements ConnectionListener {

    private ConnectionController connectionController;
    private AvatarPanel panel;
    private STORIMWindow storimWindow;
    private DefaultListModel<AvatarListItem> avatarModel = new DefaultListModel<>();
    private boolean newAvatar = false;
    private Image avatarImage;
    private AvatarDto selectedAvatar;
    private JFileChooser fileChooser;


    public AvatarPanelController(STORIMWindow storimWindow, AvatarPanel panel, ConnectionController connectionController) {
        this.panel = panel;
        this.storimWindow = storimWindow;
        this.connectionController = connectionController;
        this.fileChooser = new JFileChooser();
        connectionController.addConnectionListener(this);
        registerReceivers();
        setup();
    }

    private void registerReceivers() {
        connectionController.registerReceiver(SetCurrentUserDTO.class, (ProtocolReceiver<SetCurrentUserDTO>) dto -> setCurrentUser(dto));
        connectionController.registerReceiver(AvatarAddedDTO.class, (ProtocolReceiver<AvatarAddedDTO>) dto -> avatarAdded(dto.getAvatar()));
        connectionController.registerReceiver(AvatarDeletedDTO.class, (ProtocolReceiver<AvatarDeletedDTO>) dto -> avatarDeleted(dto.getAvatarId()));
        connectionController.registerReceiver(AvatarUpdatedDTO.class, (ProtocolReceiver<AvatarUpdatedDTO>) dto -> avatarUpdated(dto.getAvatar()));
    }

    private void setCurrentUser(SetCurrentUserDTO dto) {
        // Get the Avatars!
        panel.empty(true);
        panel.getBtnCreate().setEnabled(true);
        List<Long> avatars = getAvatars(dto.getUser().getId());
        for (Long avatarId : avatars) {
            AvatarDto avatarDto = getAvatar(avatarId);
            avatarAdded(avatarDto);
        }
    }

    private List<Long> getAvatars(Long id) {
        List<Long> result = null;
        GetAvatarsDTO getAvatarsRequestDTO = new GetAvatarsDTO(id);
        GetAvatarsResponseDTO getAvatarsResponseDTO = connectionController.sendReceive(getAvatarsRequestDTO, GetAvatarsResponseDTO.class);
        if ( getAvatarsResponseDTO.getAvatars() != null ) {
            result = getAvatarsResponseDTO.getAvatars();
        }
        return result;
    }

    private AvatarDto getAvatar(Long id) {
        AvatarDto result = null;
        GetAvatarDTO getAvatarRequestDTO = new GetAvatarDTO(id);
        GetAvatarResponseDTO getAvatarResponseDTO = connectionController.sendReceive(getAvatarRequestDTO, GetAvatarResponseDTO.class);
        if ( getAvatarResponseDTO.getAvatar() != null ) {
            result = getAvatarResponseDTO.getAvatar();
        }
        return result;
    }


    private void setup() {
        panel.getListAvatars().setModel(avatarModel);
        panel.getBtnDelete().addActionListener(e -> deleteAvatar());
        panel.getBtnSave().addActionListener(e -> saveAvatar());
        panel.getBtnCreate().addActionListener(e -> createAvatar());
        panel.getBtnChooseFile().addActionListener(e -> chooseFile());
        panel.getListAvatars().addListSelectionListener(e -> {
            AvatarListItem item = panel.getListAvatars().getSelectedValue();
            if (item != null && item.getAvatar() != null) {
                avatarSelected(item.getAvatar());
            }
        });
        panel.setEditable(false);
        panel.getBtnSave().setEnabled(false);
        panel.getBtnDelete().setEnabled(false);
        panel.getBtnCreate().setEnabled(false);
        panel.getBtnSetCurrent().setEnabled(false);
        panel.getBtnSetCurrent().addActionListener(e -> setCurrentAvatar());
    }


    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {
        panel.empty(true);
        newAvatar = false;
    }

    private void setCurrentAvatar() {
        if (selectedAvatar != null) {
            SetAvatarDto setAvatarDto = new SetAvatarDto(selectedAvatar.getId());
            connectionController.send(setAvatarDto);
        }
    }

    private void avatarSelected(AvatarDto avatar) {
        selectedAvatar = avatar;
        SwingUtilities.invokeLater(() -> {
            panel.getLblAvatarID().setText(avatar.getId().toString());
            panel.getTxtAvatarName().setText(avatar.getName());
            panel.getBtnSave().setEnabled(true);
            panel.getBtnDelete().setEnabled(true);
            panel.getBtnSetCurrent().setEnabled(true);
            panel.setEditable(true);
            avatarImage = ImageUtils.decode(avatar.getImageData());
            Image iconImage = avatarImage.getScaledInstance(panel.getLblImagePreview().getWidth(), panel.getLblImagePreview().getHeight(), Image.SCALE_SMOOTH);
            panel.getLblImagePreview().setIcon(new ImageIcon(iconImage));
        });

    }

    private void chooseFile() {
        int returnVal = fileChooser.showOpenDialog(panel);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                avatarImage = ImageIO.read(file);
                SwingUtilities.invokeLater(() -> {
                    Image iconImage = avatarImage.getScaledInstance(panel.getLblImagePreview().getWidth(), panel.getLblImagePreview().getHeight(), Image.SCALE_SMOOTH);
                    panel.getLblImagePreview().setIcon(new ImageIcon(iconImage));
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createAvatar() {
        panel.setEnabled(true);
        newAvatar = true;
        avatarImage = null;
        SwingUtilities.invokeLater(() -> {
            panel.getLblAvatarID().setText("");
            panel.getTxtAvatarName().setText("New AVATAR Name");
            panel.getBtnSave().setEnabled(true);
            panel.getListAvatars().clearSelection();
            selectedAvatar = null;
            panel.getBtnDelete().setEnabled(false);
            panel.getLblImagePreview().setIcon(null);
            panel.setEditable(true);
        });
    }

    private void saveAvatar() {
        String avatarName = panel.getTxtAvatarName().getText().trim();
        if (avatarImage == null) {
            JOptionPane.showMessageDialog(panel, "Please choose image! ");
            return;
        }
        if (newAvatar) {
            AddAvatarDto addAvatarDto = new AddAvatarDto(avatarName, ImageUtils.encode(avatarImage) );
            connectionController.send(addAvatarDto);
        } else {
            // Update !
            Long avatarID = Long.valueOf(panel.getLblAvatarID().getText());
            UpdateAvatarDto updateAvatarDto = new UpdateAvatarDto(avatarID, avatarName, ImageUtils.encode(avatarImage));
            connectionController.send(updateAvatarDto);
        }

        panel.setEditable(false);
        panel.empty(false);
        panel.getListAvatars().clearSelection();
        selectedAvatar = null;
        panel.getBtnDelete().setEnabled(false);
    }

    private void deleteAvatar() {
        Long avatarID = Long.valueOf(panel.getLblAvatarID().getText());
        DeleteAvatarDTO deleteAvatarDTO = new DeleteAvatarDTO(avatarID);
        connectionController.send(deleteAvatarDTO);
        panel.empty(false);

    }

    public void avatarAdded(AvatarDto avatar) {
        SwingUtilities.invokeLater(() -> {
            avatarModel.addElement(new AvatarListItem(avatar));
        });
    }


    public void avatarDeleted(Long avatarId) {
        SwingUtilities.invokeLater(() -> {
            AvatarListItem found = null;
            for (int i = 0; i < avatarModel.getSize(); i++) {
                AvatarListItem item = avatarModel.get(i);
                if (item.getAvatar().getId().equals(avatarId)) {
                    found = item;
                    break;
                }
            }
            if (found != null) {
                avatarModel.removeElement(found);
            }
        });
    }

    public void avatarUpdated(AvatarDto avatar) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < avatarModel.getSize(); i++) {
                AvatarListItem item = avatarModel.get(i);
                if (item.getAvatar().getId().equals(avatar.getId())) {
                    item.setAvatar(avatar);
                    break;
                }
            }
            panel.getListAvatars().invalidate();
            panel.getListAvatars().repaint();
        });
    }


}
