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

    /**
     * Número de espetadores (M).
     */
    public static final int NO_SPECTATORS = 4;

    /**
     * Distância da pista de corrida (D).
     */
    public static final int TRACK_DISTANCE = 50;
    
    public static void main(String[] args) throws IOException  {
        
        RacingTrack racingTrack = new RacingTrack(NO_SPECTATORS, TRACK_DISTANCE);
        
        ServerSocket servidor = new ServerSocket( PORTA );
        System.out.println("Servidor RacingTrack ouvindo a porta " + PORTA);

        while (true) {            
            Socket cliente = servidor.accept();
            System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
            
            Runnable r = new MyRunnable( racingTrack, cliente );
            new Thread(r).start();
        }
    }
}
