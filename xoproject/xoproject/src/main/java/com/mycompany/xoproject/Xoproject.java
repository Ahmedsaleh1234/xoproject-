/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.xoproject;

import com.mycompany.xoproject.client.GameView;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author DELL
 */
public class Xoproject {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int choice = JOptionPane.showOptionDialog(null, 
                "Select game mode", 
                "XO Game", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                new String[]{"Single Player", "Multiplayer"}, 
                "Single Player");
            
            boolean isSinglePlayer = (choice == 0);
            new GameView(isSinglePlayer).setVisible(true);
        });
    }
}
