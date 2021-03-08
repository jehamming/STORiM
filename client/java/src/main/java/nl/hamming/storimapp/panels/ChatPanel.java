package nl.hamming.storimapp.panels;


import com.hamming.storim.Controllers;
import com.hamming.storim.interfaces.ConnectionListener;
import com.hamming.storim.interfaces.RoomListener;
import com.hamming.storim.interfaces.UserListener;
import com.hamming.storim.interfaces.VerbListener;
import com.hamming.storim.model.dto.LocationDto;
import com.hamming.storim.model.dto.RoomDto;
import com.hamming.storim.model.dto.UserDto;
import com.hamming.storim.model.dto.VerbDto;
import com.hamming.storim.model.dto.protocol.ExecVerbResultDTO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ChatPanel extends JPanel implements VerbListener, UserListener, RoomListener, ConnectionListener {

    private JTextArea chatOutput;
    private JComboBox cmbVerbs;
    private JTextField tfInput ;
    private DefaultComboBoxModel verbsModel;
    private Controllers controllers;

    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {
        empty();
    }



    private class VerbListItem {
        private String name;
        private VerbDto command;
        public VerbListItem(String name, VerbDto command) {
            this.name = name;
            this.command = command;
        }
        public VerbDto getCommand() {
            return command;
        }

        @Override
        public String toString() {
            return name;
        }
    }


    public ChatPanel(Controllers controllers) {
        this.controllers = controllers;
        controllers.getVerbController().addVerbListener(this);
        controllers.getUserController().addUserListener(this);
        controllers.getRoomController().addRoomListener(this);
        controllers.getConnectionController().addConnectionListener(this);
        createPanel();
    }

    private void createPanel() {
        setBorder(new TitledBorder("Chat"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        chatOutput = new JTextArea(10,40);
        JScrollPane scrollPane = new JScrollPane(chatOutput);

        JPanel pnlInput = new JPanel();
        cmbVerbs = new JComboBox();
        verbsModel = (DefaultComboBoxModel) cmbVerbs.getModel();
        tfInput = new JTextField(40);
        JButton btnSend = new JButton("Send");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send();
            }
        });
        add(scrollPane);

        tfInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    send();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        pnlInput.add(cmbVerbs);
        pnlInput.add(tfInput);
        pnlInput.add(btnSend);
        add(pnlInput);

    }

    private void send() {
        if ( controllers.getConnectionController().isConnected()) {
            String txt = tfInput.getText().trim();
            VerbListItem item = (VerbListItem) cmbVerbs.getSelectedItem();
            VerbDto dto = item.getCommand();
            controllers.getVerbController().executeVerb(dto, txt);
        }
    }

    public void empty() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chatOutput.setText("");
                verbsModel.removeAllElements();
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

    private void removeVerb(Long id) {
        VerbListItem found = null;
        for (int i = 0; i < verbsModel.getSize(); i++) {
            VerbListItem item = (VerbListItem) verbsModel.getElementAt(i);
            if (item != null && item.getCommand().getId().equals(id)) {
                found = item;
                break;
            }
        }
        if (found != null) {
            verbsModel.removeElement(found);
        }
    }

    @Override
    public void verbExecuted(ExecVerbResultDTO result) {
        addText(result.getOutput());
    }

    public void addText(String txt) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chatOutput.append(txt+"\n");
            }
        });
    }



    @Override
    public void userConnected(UserDto user) {
        addText(user.getName() + " connected !");
    }

    @Override
    public void userDisconnected(UserDto user) {
        addText(user.getName() + " disconnected !");
    }

    @Override
    public void userOnline(UserDto user) {
        addText(user.getName() + " is online");
    }

    @Override
    public void loginResult(boolean success, String message) {
        if (success) {
            Long roomId = controllers.getUserController().getCurrentUserLocation().getRoomId();
            RoomDto roomDto = controllers.getRoomController().findRoomByID(roomId);
            addText("You wake up in " + roomDto.getName());
        }
    }

    @Override
    public void userTeleported(Long userId, LocationDto location) {

    }


    @Override
    public void userInRoom(UserDto user, LocationDto location) {
        addText(user.getName() + " is in this room");
    }

    @Override
    public void userEnteredRoom(UserDto user, LocationDto location) {
        addText(user.getName() + " entered this room!");
    }

    @Override
    public void userLeftRoom(UserDto user) {
        addText(user.getName() + " left the room..");
    }

    @Override
    public void userLocationUpdate(UserDto user, LocationDto location) {

    }

    @Override
    public void currentUserLocationUpdate(Long sequenceNumber, LocationDto location) {

    }

    @Override
    public void setRoom(RoomDto room, LocationDto location) {
        addText("You are in " + room.getName());
    }



}
