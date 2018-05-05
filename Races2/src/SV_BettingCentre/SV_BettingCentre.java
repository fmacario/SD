package SV_BettingCentre;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 * class SV_BettingCentre.
 * @author fm
 */
public class SV_BettingCentre {
    
    /**
     * Porta usada pelo BettingCentre.
     */
    private static int PORTA;
    
    /**
     * Número de competidores (N).
     */
    private static int NO_COMPETITORS;

    /**
     * Número de espetadores (M).
     */
    private static int NO_SPECTATORS;

    /**
     * Aposta máxima.
     */
    private static int MAX_BET;
    
    /**
     * IP do GRI.
     */
    private static String IP_GRI;
    
    /**
     * Porta do GRI.
     */
    private static int PORT_GRI;
    
    /**
     * main SV_BettingCentre.
     * @param args Não usado.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException  {
        Properties prop = new Properties();
	InputStream input = null;
        
        try {
            input = new FileInputStream("myProperties.properties");

            prop.load(input);

            PORTA = Integer.parseInt( prop.getProperty("PORT_BETTING_CENTRE") );
            NO_SPECTATORS = Integer.parseInt( prop.getProperty("NO_SPECTATORS") );
            NO_COMPETITORS = Integer.parseInt( prop.getProperty("NO_COMPETITORS") );
            MAX_BET = Integer.parseInt( prop.getProperty("MAX_BET") );
            IP_GRI = prop.getProperty("IP_GRI");
            PORT_GRI = Integer.parseInt( prop.getProperty("PORT_GRI") );

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
        
        BettingCentre bettingCentre = new BettingCentre(NO_SPECTATORS, NO_COMPETITORS, MAX_BET, IP_GRI, PORT_GRI );
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
