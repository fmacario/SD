/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SV_Stable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 *
 * @author fm
 */
public class SV_Stable {
    private static int PORTA;
    private static int NO_COMPETITORS;

    public static void main(String[] args) throws IOException  {
        Properties prop = new Properties();
	InputStream input = null;
        
        try {
            input = new FileInputStream("myProperties.properties");

            prop.load(input);

            PORTA = Integer.parseInt( prop.getProperty("PORT_STABLE") );
            NO_COMPETITORS = Integer.parseInt( prop.getProperty("NO_COMPETITORS") );

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

        Stable stable = new Stable( NO_COMPETITORS );
        
        ServerSocket servidor = new ServerSocket( PORTA );
        System.out.println("Servidor Stable ouvindo a porta " + PORTA);
        
        InetAddress iAddress = InetAddress.getLocalHost();
        String server_IP = iAddress.getHostAddress();
        System.out.println("Server IP address : " +server_IP);
        
        while (true) {            
            Socket cliente = servidor.accept();
            System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
            
            Runnable r = new MyRunnable( stable, cliente );
            new Thread(r).start();
        }
    }
}
