package com.escuelaing.arep.clientServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SqrtServer {
    public static void main(final String[] args) throws IOException {
        ServerSocket serverSocket = null;
        System.out.println("Server up");
        try {
            serverSocket = new ServerSocket(35000);
        } catch (final IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (final IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        final PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine, outputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Mensaje: " + inputLine);
            try {
                outputLine = "Respuesta: " + Math.pow(Double.parseDouble(inputLine), 2);
            } catch (NumberFormatException e) {
                if (inputLine.equals("Bye.")) 
                    outputLine = "Respuesta: " + inputLine;
                else
                    outputLine = "Respuesta: " + inputLine + " is not a number";
            }
            out.println(outputLine);
            if (outputLine.equals("Respuesta: Bye.")) 
                break;
        }
        
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}