/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.xoproject.client;

/**
 *
 * @author DELL
 */
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameView extends JFrame {

 
    private final GameViewModel viewModel;
    private final JButton[] buttons = new JButton[9];
    private JLabel statusLabel;
    private JPanel gamePanel;
    private JPanel controlPanel;

    public GameView(boolean isSinglePlayer) {
        this.viewModel = new GameViewModel(isSinglePlayer);
        initializeUI();
        if (!isSinglePlayer) {
            setupMultiplayer();
        }
    }

    private void initializeUI() {
        setTitle("XO Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());

        gamePanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("Arial", Font.BOLD, 40));
            final int position = i;
            buttons[i].addActionListener(e -> handleButtonClick(position));
            gamePanel.add(buttons[i]);
        }

        controlPanel = new JPanel();
        statusLabel = new JLabel("Player X's turn");
        controlPanel.add(statusLabel);
        
        JButton resetButton = new JButton("Reset Game");
        resetButton.addActionListener(e -> resetGame());
        controlPanel.add(resetButton);

        add(gamePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        updateView();
    }

    private void setupMultiplayer() {
        String serverAddress = JOptionPane.showInputDialog("Enter server address:");
        try {
            viewModel.connectToServer(serverAddress, 5555);
            new Thread(this::listenForServerMessages).start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to server");
            System.exit(1);
        }
    }

    private void listenForServerMessages() {
        while (true) {
            try {
                String message = viewModel.processServerMessage();
                if (message.startsWith("OPPONENT_MOVED")) {
                    int position = Integer.parseInt(message.split(" ")[1]);
                    SwingUtilities.invokeLater(() -> {
                        viewModel.processOpponentMove(position);
                        updateView();
                    });
                } else if (message.startsWith("VICTORY") || message.startsWith("DEFEAT") || message.equals("DRAW")) {
                    SwingUtilities.invokeLater(() -> {
                        updateView();
                        showGameResult(message);
                    });
                    break;
                }
            } catch (IOException ex) {
                Logger.getLogger(GameView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void handleButtonClick(int position) {
        if (viewModel.isMyTurn() && viewModel.makeMove(position)) {
            updateView();
            if (viewModel.isGameOver()) {
                showGameResult();
            }
        }
    }

    private void updateView() {
        char[] board = viewModel.getBoard();
        for (int i = 0; i < 9; i++) {
            buttons[i].setText(board[i] == ' ' ? "" : String.valueOf(board[i]));
            buttons[i].setEnabled(!viewModel.isGameOver() && board[i] == ' ' && viewModel.isMyTurn());
        }
        
        if (viewModel.isGameOver()) {
            statusLabel.setText("Game Over");
        } else {
            statusLabel.setText("Player " + viewModel.getCurrentPlayer() + "'s turn");
        }
    }

    private void showGameResult() {
        if (viewModel.checkWin()) {
            JOptionPane.showMessageDialog(this, "Player " + viewModel.getCurrentPlayer() + " wins!");
        } else {
            JOptionPane.showMessageDialog(this, "It's a draw!");
        }
    }

    private void showGameResult(String message) {
        if (message.startsWith("VICTORY")) {
            JOptionPane.showMessageDialog(this, "You win!");
        } else if (message.startsWith("DEFEAT")) {
            JOptionPane.showMessageDialog(this, "You lose!");
        } else {
            JOptionPane.showMessageDialog(this, "It's a draw!");
        }
    }

    private void resetGame() {
        viewModel.resetGame();
        updateView();
    }

   
}