/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SV_RacingTrack;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 *
 * @author fm
 */
public class SV_RacingTrack {
    private static int PORTA;

    /**
     * Número de espetadores (M).
     */
    private static int NO_SPECTATORS;

    /**
     * Distância da pista de corrida (D).
     */
    private static int TRACK_DISTANCE;
    
    public static void main(String[] args) throws IOException  {
        Properties prop = new Properties();
	InputStream input = null;
        
        try {
            input = new FileInputStream("myProperties.properties");

            prop.load(input);

            PORTA = Integer.parseInt( prop.getProperty("PORT_RACING_TRACK") );
            NO_SPECTATORS = Integer.parseInt( prop.getProperty("NO_SPECTATORS") );
            TRACK_DISTANCE = Integer.parseInt( prop.getProperty("TRACK_DISTANCE") );

	} catch (IOException ex) {
            ex.printStackTrace();
	} finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
	}
        
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
