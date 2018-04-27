/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SV_Paddock;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author fm
 */
public class SV_Paddock {
    static final int NO_COMPETITORS = 4;
    static final int NO_SPECTATORS = 4;
    static final int PORTA = 12343;
    
    public static void main(String[] args) throws IOException  {
        
        Paddock paddock = new Paddock(NO_COMPETITORS, NO_SPECTATORS);
        
        ServerSocket servidor = new ServerSocket( PORTA );
        System.out.println("Servidor Paddock ouvindo a porta " + PORTA);

        while (true) {            
            Socket cliente = servidor.accept();
            System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
            
            Runnable r = new MyRunnable( paddock, cliente );
            new Thread(r).start();
        }
    }
}
