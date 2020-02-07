package com.escuelaing.arep.clientServer;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.io.DataOutputStream;

// Primeramente iniciamos la clase principal
public class Webserver {
  public static void main(String[] args) throws Exception {

    // En primer lugar declaramos las variables del puerto, e iniciamos la clase
    // ServerSocket en dicho puerto
    int port = 8080;
    ServerSocket serverSocket = new ServerSocket(port);
    System.err.println("Servidor corriendo en el puerto : " + port);

    // Arrancamos el loop principal donde conectaremos a los nuevos clientes
    while (true) {

      // Aceptamos las conexiones entrantes de los clientes
      Socket clientSocket = serverSocket.accept();
      System.err.println("\nNuevo cliente conectado...");

      // Declaramos las lecturas del buffer de entrada y salida del socket
      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

      // Leemos el request para enviarle posteriormente las imágenes
      String s = in.readLine();
      if (s.length() > 15)
        s = s.substring(0, 16);
      System.out.println(s);

      // A continuación mandamos la imágen icon
      if (s.equals("GET /icono_1.png")) {
        // Abrimos las imágen y leemos los bytes para mandarle la información al socket
        File file = new File("/src/main/resources/error.png");
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();

        // Cabeceras con la info de la imágen
        DataOutputStream binaryOut = new DataOutputStream(clientSocket.getOutputStream());
        binaryOut.writeBytes("HTTP/1.0 200 OK\r\n");
        binaryOut.writeBytes("Content-Type: image/png\r\n");
        binaryOut.writeBytes("Content-Length: " + data.length);
        binaryOut.writeBytes("\r\n\r\n");
        binaryOut.write(data);

        binaryOut.close();

        // Hacemos lo mismo para el favicon
      } else if (s.equals("GET /favicon.ico")) {
        File file = new File("/src/main/resources/favicon.ico");
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();

        DataOutputStream binaryOut = new DataOutputStream(clientSocket.getOutputStream());
        binaryOut.writeBytes("HTTP/1.0 200 OK\r\n");
        binaryOut.writeBytes("Content-Type: image/x-icon\r\n");
        binaryOut.writeBytes("Content-Length: " + data.length);
        binaryOut.writeBytes("\r\n\r\n");
        binaryOut.write(data);

        binaryOut.close();

        // Mandamos las cabeceras de la paǵina principal
      } else {
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Content-Type: text/html; charset=UTF-8\r\n");
        out.write("\r\n");

        out.write("\n<!DOCTYPE html>\n");
        out.write("<html><head><title>Servidor Web en JAVA</title>\n");
        out.write("<style>body { background-color: #A9D0F5 }</style></head>\n");
        out.write("<body><center><h1>Hola mundo!</h1><br>\n");
        out.write("<img src=\"icono_1.png\"/></center></body></html>\n");
      }

      // Cerramos que nos vamos
      System.err.println("Conexión del cliente terminada!\n");
      out.close();
      in.close();
      clientSocket.close();
    }
  }
}