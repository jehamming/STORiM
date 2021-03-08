package nl.hamming.storimapp.panels;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeleportPanel extends JPanel {

    private JButton btnTeleport;


    public TeleportPanel() {
        createPanel();
    }

    private void createPanel() {
        btnTeleport = new JButton("Teleport");
        btnTeleport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teleport();
            }
        });
        add(btnTeleport);
    }

    private void teleport() {
       // TODO
    }

}
