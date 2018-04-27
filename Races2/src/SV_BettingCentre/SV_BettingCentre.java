package SV_BettingCentre;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author fm
 */
public class SV_BettingCentre {
    static final int PORTA = 12340;
    
    /**
     * Número de competidores (N).
     */
    public static final int NO_COMPETITORS = 4;

    /**
     * Número de espetadores (M).
     */
    public static final int NO_SPECTATORS = 4;

    /**
     * Aposta máxima.
     */
    public static final double MAX_BET = 1000;
    
    public static void main(String[] args) throws IOException  {
        
        BettingCentre bettingCentre = new BettingCentre(NO_SPECTATORS, NO_COMPETITORS, MAX_BET);
        ServerSocket servidor = new ServerSocket( PORTA );
        System.out.println("Servidor BettingCentre ouvindo a porta " + PORTA);

        while (true) {            
            Socket cliente = servidor.accept();
            System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
            
            Runnable r = new MyRunnable( bettingCentre, cliente );
            new Thread(r).start();
        }
    }
}
