package com.escuelaing.arep.clientServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        try {
            URL site = new URL("http://google.com/");
            System.out.println("<--------------------- Ejercicio 1 --------------------->");
            Ejercicio1(site);
            System.out.println("<--------------------- Ejercicio 2 --------------------->");
            Ejercicio2(site);
            System.out.println("<--------------------- Ejercicio 3 --------------------->");
            Ejercicio3(args);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static void Ejercicio1(URL site) {
        System.out.println("Protocol: " + site.getProtocol());
        System.out.println("Authority: " + site.getAuthority());
        System.out.println("Host: " + site.getHost());
        System.out.println("Port: " + site.getPort());
        System.out.println("Path: " + site.getPath());
        System.out.println("Query: " + site.getQuery());
        System.out.println("File: " + site.getFile());
        System.out.println("Ref: " + site.getRef());
    }

    private static void Ejercicio2(URL site) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(site.openStream()));
            BufferedWriter writer = new BufferedWriter(new FileWriter("resultado.html"));
            String inputLine = null;
            while ((inputLine = br.readLine()) != null) {
                writer.write(inputLine);
                System.out.println(inputLine);
            }
            writer.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void Ejercicio3(String[] args) {
        try {
            SqrtServer.main(args);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
