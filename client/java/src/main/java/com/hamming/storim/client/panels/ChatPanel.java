package com.hamming.storim.client.panels;


import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ChatPanel extends JPanel  {

    private JTextArea chatOutput;
    private JComboBox cmbVerbs;
    private JTextField tfInput ;
    private JButton btnSend;


    public ChatPanel() {
      initComponents();
    }

    private void initComponents() {
        setBorder(new TitledBorder("Chat"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        chatOutput = new JTextArea(10,40);
        JScrollPane scrollPane = new JScrollPane(chatOutput);

        JPanel pnlInput = new JPanel();
        cmbVerbs = new JComboBox();
        tfInput = new JTextField(40);
        btnSend = new JButton("Send");;
        add(scrollPane);
        pnlInput.add(cmbVerbs);
        pnlInput.add(tfInput);
        pnlInput.add(btnSend);
        add(pnlInput);
    }



    public void empty() {
        SwingUtilities.invokeLater(() -> {
            chatOutput.setText("");
            ((DefaultComboBoxModel) cmbVerbs.getModel()).removeAllElements();
        });
    }


    public JTextArea getChatOutput() {
        return chatOutput;
    }

    public JComboBox getCmbVerbs() {
        return cmbVerbs;
    }

    public JTextField getTfInput() {
        return tfInput;
    }

    public JButton getBtnSend() {
        return btnSend;
    }
}
