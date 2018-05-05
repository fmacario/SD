package SV_Paddock;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 * class SV_Paddock.
 * @author fm
 */
public class SV_Paddock {
    private static int NO_COMPETITORS;
    private static int NO_SPECTATORS;
    private static int PORTA;
    private static String IP_GRI;
    private static int PORT_GRI;
    
    /**
     * main SV_Paddock.
     * @param args Não usado.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException  {
        Properties prop = new Properties();
	InputStream input = null;
        
        try {
            input = new FileInputStream("myProperties.properties");

            prop.load(input);

            PORTA = Integer.parseInt( prop.getProperty("PORT_PADDOCK") );
            NO_SPECTATORS = Integer.parseInt( prop.getProperty("NO_SPECTATORS") );
            NO_COMPETITORS = Integer.parseInt( prop.getProperty("NO_COMPETITORS") );
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
        
        Paddock paddock = new Paddock(NO_COMPETITORS, NO_SPECTATORS, IP_GRI, PORT_GRI );
        
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
