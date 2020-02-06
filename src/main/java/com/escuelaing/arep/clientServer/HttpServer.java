package com.escuelaing.arep.clientServer;

import java.net.*;
import java.util.Date;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

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
            + HttpServer.SEPARATOR + "java" + HttpServer.SEPARATOR + "resources" + HttpServer.SEPARATOR + fileName.substring(0, fileName.length()-1);

      System.out.println("Request: " + fileName);
      try {
         File file = new File(path);
         BufferedReader br = new BufferedReader(new FileReader(file));
         String contentType = "";
         out.println("HTTP/1.1 200 OK");         
         
         if (fileName.endsWith(".html ") || fileName.endsWith(".htm "))
            contentType =  "text/html";
         else if (fileName.endsWith(".css "))
            contentType =  "text/css";
         else if (fileName.endsWith(".ico "))
            contentType =  "image/x-icon";
         else if (fileName.endsWith(".png "))
            contentType =  "image/png";
         else if (fileName.endsWith(".jpeg ") || fileName.endsWith(".jpg "))
            contentType =  "image/jpeg";
         else if (fileName.endsWith(".js "))
            contentType =  "application/javascript";
         else if (fileName.endsWith(".json "))
            contentType =  "application/json";
         else
            contentType =  "text/plain";
         
         out.println("Content-type: " + contentType);
         out.println("Server: Java HTTPServer");
         out.println("Date: " + new Date());
         out.println("\r\n");
         if (contentType.contains("image/")) {
            // https://docs.oracle.com/javase/tutorial/2d/images/saveimage.html
               // <BufferedImage bi = new BufferedIm(arg0);
               // File outputfile = new File("saved.png");
               // ImageIO.write(bi, "png", outputfile);>
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
}