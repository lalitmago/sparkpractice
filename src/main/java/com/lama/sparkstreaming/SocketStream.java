package com.lama.sparkstreaming;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SocketStream {
    public static void main(String[] args) {
        Path path = FileSystems.getDefault().getPath("data/data-sensors.txt");

        List<String> lines = null;
        DataInputStream inputStream;
        PrintStream outputStream;

        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ServerSocket socServer = null;
        try {
            socServer = new ServerSocket(9876);
            System.out.println("Socket Opened on port : 9876 ...");

            System.out.println("Total records read : " + lines.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Socket clientSocket = socServer.accept();
            System.out.println("Accepted connection from client : " + clientSocket);

            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new PrintStream(clientSocket.getOutputStream());

            Thread.sleep(5000);
            for(int i = 1; i < lines.size(); i++) {
                System.out.println("Publishing : " + lines.get(i));
                outputStream.println(lines.get(i));
                outputStream.flush();
                Thread.sleep(10);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
