/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.xoproject.server;

/**
 *
 * @author DELL
 */
import java.io.*;
import java.net.*;
import java.util.Arrays;

public class GameSession implements Runnable {
    private Socket player1;
    private Socket player2;
    private PrintWriter out1;
    private PrintWriter out2;
    private BufferedReader in1;
    private BufferedReader in2;
    private char[] board = new char[9];
    private char currentPlayer = 'X';

    public GameSession(Socket player1, Socket player2) {
        this.player1 = player1;
        this.player2 = player2;
        Arrays.fill(board, ' ');
    }

    @Override
    public void run() {
        try {
            out1 = new PrintWriter(player1.getOutputStream(), true);
            out2 = new PrintWriter(player2.getOutputStream(), true);
            in1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
            in2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));

            out1.println("WELCOME X");
            out2.println("WELCOME O");
            
            while (true) {
                String move;
                if (currentPlayer == 'X') {
                    move = in1.readLine();
                    out2.println("OPPONENT_MOVED " + move);
                } else {
                    move = in2.readLine();
                    out1.println("OPPONENT_MOVED " + move);
                }
                
                int position = Integer.parseInt(move);
                board[position] = currentPlayer;
                
                if (checkWin()) {
                    out1.println("VICTORY " + currentPlayer);
                    out2.println("DEFEAT " + currentPlayer);
                    break;
                } else if (isBoardFull()) {
                    out1.println("DRAW");
                    out2.println("DRAW");
                    break;
                }
                
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            }
            
            player1.close();
            player2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkWin() {
        // Check rows, columns and diagonals for win
        for (int i = 0; i < 3; i++) {
            if (board[i*3] != ' ' && board[i*3] == board[i*3+1] && board[i*3] == board[i*3+2]) return true;
            if (board[i] != ' ' && board[i] == board[i+3] && board[i] == board[i+6]) return true;
        }
        if (board[0] != ' ' && board[0] == board[4] && board[0] == board[8]) return true;
        if (board[2] != ' ' && board[2] == board[4] && board[2] == board[6]) return true;
        return false;
    }

    private boolean isBoardFull() {
        for (char c : board) {
            if (c == ' ') return false;
        }
        return true;
    }
}
