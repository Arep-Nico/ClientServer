package com.escuelaing.arep.clientServer;

import java.net.*;
import java.io.*;

public class HttpServer {

   public static final String USERPATH = System.getProperty("user.dir");
   public static final String SEPARATOR = System.getProperty("file.separator");
   public static void main(String[] args) throws IOException {
      ServerSocket serverSocket = null;
      try { 
         serverSocket = new ServerSocket(35000);
      } catch (IOException e) {
         System.err.println("Could not listen on port: 35000.");
         System.exit(1);
      }

      Socket clientSocket = null;
      try {
         System.out.println("Listo para recibir ...");
         clientSocket = serverSocket.accept();
      } catch (IOException e) {
         System.err.println("Accept failed.");
         System.exit(1);
      }
      PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      String inputLine, fileName = "/";
      while ((inputLine = in.readLine()) != null) {
         System.out.println("Recibí: " + inputLine);
         if (inputLine.startsWith("GET"))
            fileName = inputLine.substring(inputLine.indexOf("/")+1, inputLine.indexOf("HTTP"));
         if (!in.ready()) {
            break; 
         }
      }
      if (fileName.equals(" "))
         fileName = "index.html";

      HttpServer.returnFile(fileName, out);
      out.flush();
      out.close();
      in.close();
      clientSocket.close();
      serverSocket.close();
      System.out.println("Close Server");
   }

   private static void returnFile(String fileName, PrintWriter out) {
      String path = HttpServer.USERPATH + HttpServer.SEPARATOR + "src" + HttpServer.SEPARATOR + "main"
            + HttpServer.SEPARATOR + "java" + HttpServer.SEPARATOR + "resources" + HttpServer.SEPARATOR + fileName;

      out.println("HTTP/1.1 200 OK");
      out.println("Content-type: " + "text/html");
      out.println("\r\n");
      File file = new File(path);
      try {
         BufferedReader br = new BufferedReader(new FileReader(file));
         String st;
         while ((st = br.readLine()) != null)
            out.println(st);
         br.close();
      } catch (IOException e) {
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