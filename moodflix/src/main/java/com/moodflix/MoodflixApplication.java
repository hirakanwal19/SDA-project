package com.moodflix;

import com.moodflix.gui.MoodflixGUI;
import javax.swing.SwingUtilities;

public class MoodflixApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MoodflixGUI().setVisible(true);
        });
    }
}
