package com.escuelaing.arep.clientServer;

import java.net.*;
import java.util.Date;
import java.io.*;

public class HttpServer {

   public static final String USERPATH = System.getProperty("user.dir");
   public static final String SEPARATOR = System.getProperty("file.separator");
   public static void main(String[] args) throws IOException {
      while (true) {
         ServerSocket serverSocket = null;
         try { 
            serverSocket = new ServerSocket(35000);
         } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
         }

         Socket clientSocket = null;
         try {
            System.out.println("Listo para recibir, puerto: " + serverSocket.getLocalPort());
            clientSocket = serverSocket.accept();
         } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
         }
      
         PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
         BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         String inputLine, fileName = "/";
         while ((inputLine = in.readLine()) != null) {
            // System.out.println("Recibí: " + inputLine);
            if (inputLine.startsWith("GET"))
               fileName = inputLine.substring(inputLine.indexOf("/")+1, inputLine.indexOf("HTTP"));
            if (!in.ready()) {
               break; 
            }
         }
         if (fileName.equals(" "))
            fileName = "index.html ";
         if (!fileName.equals("/"))
            HttpServer.returnFile(fileName, out);
         out.flush();
         out.close();
         in.close();
         clientSocket.close();
         serverSocket.close();
      }
   }

   private static void returnFile(String fileName, PrintWriter out) {
      String path = HttpServer.USERPATH + HttpServer.SEPARATOR + "src" + HttpServer.SEPARATOR + "main"
            + HttpServer.SEPARATOR + "java" + HttpServer.SEPARATOR + "resources" + HttpServer.SEPARATOR + fileName;

      System.out.println("Request: " + fileName + " " + fileName.endsWith(" "));
      try {
         File file = new File(path);
         BufferedReader br = new BufferedReader(new FileReader(file));

         out.println("HTTP/1.1 200 OK");         
         
         if (fileName.endsWith(".html ") || fileName.endsWith(".htm "))
            out.println("Content-type: " + "text/html");
         else if (fileName.endsWith(".css "))
            out.println("Content-type: " + "text/css");
         else if (fileName.endsWith(".ico "))
            out.println("Content-type: " + "image/x-icon");
         else if (fileName.endsWith(".png "))
            out.println("Content-type: " + "image/png");
         else if (fileName.endsWith(".jpeg ") || fileName.endsWith(".jpg "))
            out.println("Content-type: " + "image/jpeg");
         else if (fileName.endsWith(".js "))
            out.println("Content-type: " + "application/javascript");
         else if (fileName.endsWith(".json "))
            out.println("Content-type: " + "application/json");
         else
            out.println("Content-type: " + "text/plain");
         
         out.println("Server: Java HTTPServer");
         out.println("Date: " + new Date());
         out.println("\r\n");
         
         String st;
         while ((st = br.readLine()) != null)
            out.println(st);
         br.close();
      } catch (IOException e) {
         out.println("HTTP/1.1 404 Not Found");
         out.println("Content-type: " + "text/html");
         out.println("\r\n");
         String outputLine = "<!DOCTYPE html>" + 
          "<html>" + 
          "<head>" + 
          "<meta charset=\"UTF-8\">" + 
          "<title>File Not Found</title>\n" + 
          "</head>" + 
          "<body>" + 
          "<center><h1>File Not Found</h1></center>" + 
          "</body>" + 
          "</html>";
          out.println(outputLine);
      }
   }
}