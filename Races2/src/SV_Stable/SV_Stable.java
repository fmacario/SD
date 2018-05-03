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
import org.json.JSONException;

/**
 *
 * @author fm
 */
public class SV_Stable {
    private static int PORTA;
    private static int NO_COMPETITORS;
    private static String IP_GRI;
    private static int PORT_GRI;

    public static void main(String[] args) throws IOException, JSONException  {
        Properties prop = new Properties();
	InputStream input = null;
        
        try {
            input = new FileInputStream("myProperties.properties");

            prop.load(input);

            PORTA = Integer.parseInt( prop.getProperty("PORT_STABLE") );
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

        Stable stable = new Stable( NO_COMPETITORS, IP_GRI, PORT_GRI );
        
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
