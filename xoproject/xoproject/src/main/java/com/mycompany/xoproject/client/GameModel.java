/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.xoproject.client;

/**
 *
 * @author DELL
 */
public class GameModel {
    private char[] board = new char[9];
    private char currentPlayer = 'X';
    private boolean isSinglePlayer;
    private boolean gameOver = false;

    public GameModel(boolean isSinglePlayer) {
        this.isSinglePlayer = isSinglePlayer;
        resetBoard();
    }

    public void resetBoard() {
        for (int i = 0; i < 9; i++) {
            board[i] = ' ';
        }
        currentPlayer = 'X';
        gameOver = false;
    }

    public boolean makeMove(int position) {
        if (position < 0 || position >= 9 || board[position] != ' ' || gameOver) {
            return false;
        }

        board[position] = currentPlayer;
        
        if (checkWin()) {
            gameOver = true;
            return true;
        }
        
        if (isBoardFull()) {
            gameOver = true;
            return true;
        }
        
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        return true;
    }

    public boolean checkWin() {
        for (int i = 0; i < 3; i++) {
            //الاقفي لو زي بعض
            if (board[i*3] != ' ' && board[i*3] == board[i*3+1] && board[i*3] == board[i*3+2]) return true;
            //العمودي
            if (board[i] != ' ' && board[i] == board[i+3] && board[i] == board[i+6]) return true;
        }
        //    \
        if (board[0] != ' ' && board[0] == board[4] && board[0] == board[8]) return true;
        //     /
        if (board[2] != ' ' && board[2] == board[4] && board[2] == board[6]) return true;
        return false;
    }

    public boolean isBoardFull() {
        for (char c : board) {
            if (c == ' ') return false;
        }
        return true;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public char[] getBoard() {
        return board.clone();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isSinglePlayer() {
        return isSinglePlayer;
    }
}