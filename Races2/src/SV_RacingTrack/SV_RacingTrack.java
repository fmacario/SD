/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SV_RacingTrack;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author fm
 */
public class SV_RacingTrack {
    static final int PORTA = 12344;
    
    public static void main(String[] args) throws IOException  {
        ServerSocket servidor = new ServerSocket( PORTA );
        System.out.println("Servidor RacingTrack ouvindo a porta " + PORTA);

        while (true) {            
            Socket cliente = servidor.accept();
            System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
        }
    }
}
