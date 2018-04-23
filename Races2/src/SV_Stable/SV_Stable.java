/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SV_Stable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author fm
 */
public class SV_Stable {
    static final int PORTA = 12345;
    static final int NO_COMPETITORS = 4;
    
    public static void main(String[] args) throws IOException  {
        
        Stable stable = new Stable( NO_COMPETITORS );
        
        ServerSocket servidor = new ServerSocket( PORTA );
        System.out.println("Servidor Stable ouvindo a porta " + PORTA);

        while (true) {            
            Socket cliente = servidor.accept();
            System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
            
            Runnable r = new MyRunnable( stable, cliente );
            new Thread(r).start();
        }
    }
}
