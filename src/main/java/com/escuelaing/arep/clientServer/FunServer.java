package com.escuelaing.arep.clientServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class FunServer {
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
        final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
        String inputLine, outputLine, fun = "sin";
        double num = 0;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Mensaje: " + inputLine);
            System.out.println(inputLine+" "+ "π \u03C0 "+inputLine.equals("\u03C0"));
            if (inputLine.startsWith("fun:")){
                if(inputLine.endsWith("sin"))
                    fun = "sin";
                else if (inputLine.endsWith("con"))
                    fun = "con";
                else if (inputLine.endsWith("tan"))
                    fun = "tan";
                System.out.println("funcion");
            }

            try {
                Double.parseDouble(inputLine);
            } catch (NumberFormatException e) {
                if (inputLine.equals("Bye."))
                    outputLine = "Respuesta: " + inputLine;
                else
                    outputLine = "Respuesta: " + inputLine + " is not a valid number";
            }

            if (fun.equals("sin"))
                System.out.println("sin");
            else if (fun.equals("con"))
                System.out.println("con");
            else if (fun.equals("tan"))
                System.out.println("tan");
                
            outputLine = "Respuesta: " + inputLine;

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