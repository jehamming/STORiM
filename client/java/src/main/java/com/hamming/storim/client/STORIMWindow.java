package com.hamming.storim.client;


import com.hamming.storim.client.panels.ChatPanel;

import javax.swing.*;

/**
 *
 * @author jehamming
 */
public class STORIMWindow extends javax.swing.JFrame {

    /**
     * Creates new form STORIMWindow
     */
    public STORIMWindow() {
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    public void initComponents() {
        lblRoomName = new javax.swing.JTextField();
        lblRoomName.setEditable(false);
        lblRoomName.setBorder(null);
        jSplitPane2 = new javax.swing.JSplitPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuConnect = new javax.swing.JMenuItem();
        menuDisconnect = new javax.swing.JMenuItem();
        menuEdit = new javax.swing.JMenu();
        menuVerbs = new javax.swing.JMenuItem();
        menuAvatars = new javax.swing.JMenuItem();
        menuRooms = new javax.swing.JMenuItem();
        menuRoomTiles = new javax.swing.JMenuItem();
        menuItems = new javax.swing.JMenuItem();
        menuExits = new javax.swing.JMenuItem();
        menuAdmin = new javax.swing.JMenu();
        menuAdminPassword = new javax.swing.JMenuItem();
        menuAdminEditUsers = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(407, 600));

        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        pnlGameView.setMinimumSize(new java.awt.Dimension(300, 300));

        javax.swing.GroupLayout pnlGameViewLayout = new javax.swing.GroupLayout(pnlGameView);
        pnlGameView.setLayout(pnlGameViewLayout);
        pnlGameViewLayout.setHorizontalGroup(
                pnlGameViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 483, Short.MAX_VALUE)
        );
        pnlGameViewLayout.setVerticalGroup(
                pnlGameViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );

        jSplitPane2.setTopComponent(pnlGameView);

        chatPanel1.setMinimumSize(new java.awt.Dimension(242, 200));
        jSplitPane2.setRightComponent(chatPanel1);

        menuFile.setText("File");

        menuConnect.setText("Connect");
        menuConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuConnectActionPerformed(evt);
            }
        });
        menuFile.add(menuConnect);

        menuDisconnect.setText("Disconnect");
        menuFile.add(menuDisconnect);

        jMenuBar1.add(menuFile);

        menuEdit.setText("Edit");

        menuVerbs.setText("Verbs");
        menuEdit.add(menuVerbs);

        menuAvatars.setText("Avatars");
        menuEdit.add(menuAvatars);

        menuRooms.setText("Rooms");
        menuEdit.add(menuRooms);

        menuRoomTiles.setText("Room Tiles");
        menuEdit.add(menuRoomTiles);

        menuItems.setText("Items");
        menuEdit.add(menuItems);

        menuExits.setText("Exits");
        menuEdit.add(menuExits);

        jMenuBar1.add(menuEdit);

        menuAdmin.setText("Admin");

        menuAdminPassword.setText("Admin Password");
        menuAdmin.add(menuAdminPassword);

        menuAdminEditUsers.setText("Edit Users");
        menuAdmin.add(menuAdminEditUsers);

        jMenuBar1.add(menuAdmin);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblRoomName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(lblRoomName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private void menuConnectActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(STORIMWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(STORIMWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(STORIMWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(STORIMWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new STORIMWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private ChatPanel chatPanel1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTextField lblRoomName;
    private javax.swing.JMenu menuAdmin;
    private javax.swing.JMenuItem menuAdminEditUsers;
    private javax.swing.JMenuItem menuAdminPassword;
    private javax.swing.JMenuItem menuAvatars;
    private javax.swing.JMenuItem menuConnect;
    private javax.swing.JMenuItem menuDisconnect;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenuItem menuExits;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuItem menuItems;
    private javax.swing.JMenuItem menuRoomTiles;
    private javax.swing.JMenuItem menuRooms;
    private javax.swing.JMenuItem menuVerbs;
    private javax.swing.JPanel pnlGameView;
    // End of variables declaration

    public JMenuItem getMenuAdminEditUsers() {
        return menuAdminEditUsers;
    }

    public JMenuItem getMenuAdminPassword() {
        return menuAdminPassword;
    }

    public JMenuItem getMenuAvatars() {
        return menuAvatars;
    }

    public JMenuItem getMenuConnect() {
        return menuConnect;
    }

    public JMenuItem getMenuDisconnect() {
        return menuDisconnect;
    }

    public JMenuItem getMenuExits() {
        return menuExits;
    }

    public JMenuItem getMenuRoomTiles() {
        return menuRoomTiles;
    }

    public JMenuItem getMenuRooms() {
        return menuRooms;
    }

    public JMenuItem getMenuVerbs() {
        return menuVerbs;
    }

    public JTextField getLblRoomName() {
        return lblRoomName;
    }


    public void setPnlGameView(JPanel pnlGameView) {
        this.pnlGameView = pnlGameView;
    }

    public void setChatPanel(ChatPanel chatPanel1) {
        this.chatPanel1 = chatPanel1;
    }
}

