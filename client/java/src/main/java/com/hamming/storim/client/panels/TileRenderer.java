package com.hamming.storim.client.panels;

import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.common.dto.TileDto;

import javax.swing.*;
import java.awt.*;

public class TileRenderer extends JLabel implements ListCellRenderer {

    public TileRenderer() {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    /*
     * This method finds the image and text corresponding
     * to the selected value and returns the label, set up
     * to display the text and image.
     */
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        if (value != null) {
            TileDto tile = (TileDto) value;
            Image image = ImageUtils.decode(tile.getImageData());
            ImageIcon icon = new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            setIcon(icon);
        }

        return this;
    }

}
