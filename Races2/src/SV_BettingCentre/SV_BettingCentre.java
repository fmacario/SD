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
    
    public static void main(String[] args) throws IOException  {
        ServerSocket servidor = new ServerSocket( PORTA );
        System.out.println("Servidor BettingCentre ouvindo a porta " + PORTA);

        while (true) {            
            Socket cliente = servidor.accept();
            System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
        }
    }
}
