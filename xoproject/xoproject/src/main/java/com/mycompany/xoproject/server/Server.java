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
import java.util.*;

public class Server {
    private static final int PORT = 5555;
    private List<GameSession> activeSessions = new ArrayList<>();

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("XO Game Server started on port " + PORT);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                
                // Wait for second player
                Socket secondClientSocket = serverSocket.accept();
                System.out.println("Second client connected: " + secondClientSocket);
                
                // Create new game session for these two players
                GameSession session = new GameSession(clientSocket, secondClientSocket);
                activeSessions.add(session);
                new Thread(session).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
