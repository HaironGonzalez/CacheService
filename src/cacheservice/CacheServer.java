/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cacheservice;

import java.net.*;
import java.io.*;
import org.json.simple.JSONObject;
/**
 *
 * @author Rudolfaraya
 */
public class CacheServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // COMUNICACIÓN CON EL CLIENTE
        ServerSocket serverSocket;
        Socket socketCliente;
        DataInputStream in; //Flujo de datos de entrada
        DataOutputStream out; //Flujo de datos de salida
        String mensaje;
        int laTengoenCache = 0;
        
        //COMUNICACIÓN CON EL INDEX
        ServerSocket serverSocketIndex;
        Socket socketIndex;
        DataOutputStream outIndex;
        ObjectInputStream inIndex;
        String mensajeIndex;
        
        
        try{
            serverSocket = new ServerSocket(4444);
            System.out.print("SERVIDOR CACHE ACTIVO a la espera de peticiones");
            
            //MIENTRAS PERMANEZCA ACTIVO EL SERVIDOR CACHE ESPERARÁ POR PETICIONES DE LOS CLIENTES
            while(true){
                socketCliente = serverSocket.accept();
                in = new DataInputStream(socketCliente.getInputStream()); //Entrada de los mensajes del cliente
                mensaje = in.readUTF(); //Leo el mensaje enviado por el cliente
                                 
                System.out.println("\nHe recibido del cliente: "+mensaje); //Muestro el mensaje recibido por el cliente
                //int particionBuscada = seleccionarParticion(mensaje, tamanoCache, numeroParticiones); //Busco la partición
                //double tamanoParticion = Math.ceil( (double)tamanoCache / (double)numeroParticiones);
                
                //Thread hilo = new Hilo(mensaje,particionBuscada,cache.GetTable(),(int) tamanoParticion);
                //hilo.start();
                
                //RESPUESTA DEL SERVIDOR CACHE AL CLIENTE
                    out = new DataOutputStream(socketCliente.getOutputStream());
                    String respuesta = "Respuesta para "+mensaje;
                    if(laTengoenCache == 1){
                        out.writeUTF(respuesta);
                        System.out.println("\nTengo la respuesta. He respondido al cliente: "+respuesta);   
                    }else{                        
                        out.writeUTF("miss");
                        out.close();
                        in.close();
                        socketCliente.close();
                                        
                        System.out.println("\nNo tengo la respuesta.");
                        
                        //LEER RESPUESTA DEL SERVIDOR INDEX
                        serverSocketIndex = new ServerSocket(6666);
                        socketIndex = serverSocketIndex.accept();
                        inIndex = new ObjectInputStream(socketIndex.getInputStream());
                        JSONObject mensajeRecibidoIndex = (JSONObject) inIndex.readObject();
                       
                        System.out.println("He recibido del SERVIDOR INDEX: "+mensajeRecibidoIndex);
                        
                        //outIndex.close();
                        inIndex.close();
                        socketIndex.close();
                        
                    }
                                    
            }      
        }catch(Exception e){
            System.out.print(e.getMessage());
        }
    }
    
}
