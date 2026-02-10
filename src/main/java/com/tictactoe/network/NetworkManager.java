package com.tictactoe.network;

import java.io.*;
import java.net.*;

public class NetworkManager {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ServerSocket serverSocket;

    // HOST MODE: Wait for someone to connect to you
    public void startServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        socket = serverSocket.accept(); // This blocks until a client connects
        setupStreams();
    }

    // CLIENT MODE: Connect to a host's IP
    public void connectToHost(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        setupStreams();
    }

    private void setupStreams() throws IOException {
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }
}
