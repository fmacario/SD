package SV_GRI;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 * class SV_GRI
 * @author fm
 */
public class SV_GRI {
    private static int PORTA;
    private static int NO_SPECTATORS;
    private static int NO_COMPETITORS;
    private static int TRACK_DISTANCE;
    
    /**
     * main SV_GRI
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException  {
        Properties prop = new Properties();
	InputStream input = null;
        
        try {
            input = new FileInputStream("myProperties.properties");

            prop.load(input);
            
            PORTA = Integer.parseInt( prop.getProperty("PORT_GRI") );
            NO_SPECTATORS = Integer.parseInt( prop.getProperty("NO_SPECTATORS") );
            NO_COMPETITORS = Integer.parseInt( prop.getProperty("NO_COMPETITORS") );
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
        
        GRI gri = new GRI(NO_COMPETITORS, NO_SPECTATORS, TRACK_DISTANCE);
        
        ServerSocket servidor = new ServerSocket( PORTA );
        System.out.println("Servidor GRI ouvindo a porta " + PORTA);
        boolean b = false;
        while (true) {            
            Socket cliente = servidor.accept();
            System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
            
            Runnable r = new MyRunnable( gri, cliente);
            new Thread(r).start();
        }
    }
}
