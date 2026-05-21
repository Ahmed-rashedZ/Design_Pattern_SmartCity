package com.smartgrid;

import com.smartgrid.ui.SimpleUI;
import javax.swing.SwingUtilities;

public class GUIMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleUI ui = new SimpleUI();
            ui.setVisible(true);
        });
    }
}
