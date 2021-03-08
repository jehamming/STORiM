package nl.hamming.storimapp.panels;

import com.hamming.storim.controllers.MoveController;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class MovementPanel extends JPanel{

    private MoveController moveController;

    public MovementPanel(MoveController moveController) {
        this.moveController = moveController;
        createPanel();
    }

    private void createPanel() {
        setBorder(new TitledBorder("Movement"));
        add(new JLabel("<html>Use WSAD to Move,<br>Press 'B' for auto Baseplate mode</html>"));
    }


}
