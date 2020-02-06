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
        String[] radians;
        double num = 0;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Mensaje: " + inputLine);
            
            try {
                if (inputLine.contains("pi/")) {
                    radians = inputLine.split("/");
                    double numerator = 1;
                    if (radians[0].indexOf("pi") != 0)
                        numerator = Double.parseDouble(radians[0].substring(0, radians[0].indexOf("pi")));
                    num = Math.PI * numerator / Double.parseDouble(radians[1]);
                } else if (inputLine.contains("fun:")) {

                } else if (inputLine.equals("pi")) {
                    num = Math.PI;
                } else
                    num = Double.parseDouble(inputLine);

                System.out.println("Math.sin(" + num + ")=" + Math.sin(num));

                if (inputLine.startsWith("fun:")) {
                    if (inputLine.endsWith("sin"))
                        fun = "sin";
                    else if (inputLine.endsWith("cos"))
                        fun = "con";
                    else if (inputLine.endsWith("tan"))
                        fun = "tan";
                    outputLine = "Respuesta: funcion cambiada";
                } else {
                    if (fun.equals("sin"))
                        outputLine = "Respuesta: Sin(" + inputLine + ")= " + String.valueOf(Math.sin(num));
                    else if (fun.equals("con"))
                        outputLine = "Respuesta: Cos(" + inputLine + ")= " + String.valueOf(Math.cos(num));
                    else if (fun.equals("tan"))
                        outputLine = "Respuesta: Tan(" + inputLine + ")= " + String.valueOf(Math.tan(num));
                    else
                        outputLine = "Respuesta: " + inputLine;
                }
            } catch (NumberFormatException e) {
                if (inputLine.equals("Bye."))
                    outputLine = "Respuesta: " + inputLine;
                else
                    outputLine = "Respuesta: " + inputLine + " is not a valid number";
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