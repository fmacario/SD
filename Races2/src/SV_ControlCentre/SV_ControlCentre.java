/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SV_ControlCentre;

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
public class SV_ControlCentre {
    private static int PORTA;
    private static String IP_GRI;
    private static int PORT_GRI;
    
    public static void main(String[] args) throws IOException  {
        Properties prop = new Properties();
	InputStream input = null;
        
        try {
            input = new FileInputStream("myProperties.properties");

            prop.load(input);

            PORTA = Integer.parseInt( prop.getProperty("PORT_CONTROL_CENTRE") );
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
        
        
        ControlCentre controlCentre = new ControlCentre(IP_GRI, PORT_GRI );
        
        ServerSocket servidor = new ServerSocket( PORTA );
        System.out.println("Servidor ControlCentre ouvindo a porta " + PORTA);

        while (true) {            
            Socket cliente = servidor.accept();
            System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
            
            Runnable r = new MyRunnable( controlCentre, cliente );
            new Thread(r).start();
        }
    }
}
