/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.xoproject.client;

/**
 *
 * @author DELL
 */

// client/viewmodel/GameViewModel.java

import java.io.*;
import java.net.*;

public class GameViewModel {
    private GameModel model;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private char mySymbol;
    private boolean myTurn;

    public GameViewModel(boolean isSinglePlayer) {
        this.model = new GameModel(isSinglePlayer);
    }

    public void connectToServer(String serverAddress, int port) throws IOException {
        socket = new Socket(serverAddress, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        String welcomeMessage = in.readLine();
        if (welcomeMessage.startsWith("WELCOME")) {
            mySymbol = welcomeMessage.split(" ")[1].charAt(0);
            myTurn = (mySymbol == 'X');
        }
    }

    // New method: Process messages from server
    public String processServerMessage() throws IOException {
        return in.readLine();
    }

    // New method: Check if current player has won
    public boolean checkWin() {
        return model.checkWin();
    }

    public boolean makeMove(int position) {
        if (model.isSinglePlayer()) {
            return model.makeMove(position);
        } else {
            if (myTurn) {
                out.println(position);
                boolean validMove = model.makeMove(position);
                if (validMove) {
                    myTurn = false;
                }
                return validMove;
            }
            return false;
        }
    }

    public void processOpponentMove(int position) {
        if (!model.isSinglePlayer() && !myTurn) {
            model.makeMove(position);
            myTurn = true;
        }
    }

    public void resetGame() {
        model.resetBoard();
        if (!model.isSinglePlayer() && mySymbol == 'O') {
            myTurn = false;
        } else {
            myTurn = true;
        }
    }

    public char getCurrentPlayer() {
        return model.getCurrentPlayer();
    }

    public char[] getBoard() {
        return model.getBoard();
    }

    public boolean isGameOver() {
        return model.isGameOver();
    }

    public boolean isMyTurn() {
        return model.isSinglePlayer() || myTurn;
    }

    public void closeConnection() throws IOException {
        if (socket != null) {
            socket.close();
        }
    }
}
