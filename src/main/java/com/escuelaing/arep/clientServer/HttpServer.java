package com.escuelaing.arep.clientServer;

import java.net.*;
import java.util.Date;
import java.io.*;

public class HttpServer {

   public static final String USERPATH = System.getProperty("user.dir");
   public static final String SEPARATOR = System.getProperty("file.separator");

   public static void main(String[] args) throws IOException {

      ServerSocket serverSocket = null;
      try {
         serverSocket = new ServerSocket(getPort());
         while (true) {
            Socket clientSocket = null;
            try {
               System.out.println("Listo para recibir, puerto: " + serverSocket.getLocalPort());
               clientSocket = serverSocket.accept();
            } catch (IOException e) {
               System.err.println("Accept failed.");
               System.exit(1);
            }
            HttpServer.resolve(clientSocket);
         }
      } catch (Exception e) {
         System.err.println("Could not listen on port: 35000.");
         System.exit(1);
      }

   }

   private static void resolve(Socket clientSocket) throws IOException {
      OutputStream ops = clientSocket.getOutputStream();
      PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      String inputLine, fileName = "/";
      while ((inputLine = in.readLine()) != null) {
         // System.out.println("Recibí: " + inputLine);
         if (inputLine.startsWith("GET"))
            fileName = inputLine.substring(inputLine.indexOf("/") + 1, inputLine.indexOf("HTTP"));
         if (!in.ready()) {
            break;
         }
      }
      if (fileName.equals(" "))
         fileName = "index.html ";
      if (!fileName.equals("/"))
         HttpServer.returnFile(fileName, out, ops);
      out.flush();
      ops.close();
      out.close();
      in.close();
      clientSocket.close();
   }

   private static void returnFile(String fileName, PrintWriter out, OutputStream os) {

      String path = HttpServer.USERPATH + HttpServer.SEPARATOR + "src" + HttpServer.SEPARATOR + "main"
            + HttpServer.SEPARATOR + "java" + HttpServer.SEPARATOR + "resources" + HttpServer.SEPARATOR
            + fileName.substring(0, fileName.length() - 1);

      System.out.println("Request: " + fileName);
      try {
         File file = new File(path);
         BufferedReader br = new BufferedReader(new FileReader(file));
         String contentType = "";
         out.println("HTTP/1.1 200 OK");
         out.println("Access-Control-Allow-Origin: *");

         if (fileName.endsWith(".html ") || fileName.endsWith(".htm "))
            contentType = "text/html";
         else if (fileName.endsWith(".css "))
            contentType = "text/css";
         else if (fileName.endsWith(".ico "))
            contentType = "image/x-icon";
         else if (fileName.endsWith(".png "))
            contentType = "image/png";
         else if (fileName.endsWith(".jpeg ") || fileName.endsWith(".jpg "))
            contentType = "image/jpeg";
         else if (fileName.endsWith(".js "))
            contentType = "application/javascript";
         else if (fileName.endsWith(".json "))
            contentType = "application/json";
         else
            contentType = "text/plain";

         // out.println("Content-Length: " + file.length());
         out.println("Content-type: " + contentType);
         out.println("Server: Java HTTPServer");
         out.println("Date: " + new Date());
         out.println("\r\n");
         if (contentType.contains("image/")) {
            String st;
            while ((st = br.readLine()) != null)
               out.println(st.getBytes("UTF-8"));
            br.close();
         } else {
            String st;
            while ((st = br.readLine()) != null)
               out.println(st);
            br.close();
         }
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

   private static int getPort() {
      if (System.getenv("PORT") != null) {
         return Integer.parseInt(System.getenv("PORT"));
      }
      return 8080; // returns default port if heroku-port isn't set (i.e. on localhost)
   }
}