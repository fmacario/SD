/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SV_Stable;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 *
 * @author fm
 */
public class SV_Stable {
    static final int PORTA = 12345;
    static final int NO_COMPETITORS = 4;
    
    private static Properties properties = new Properties();
    private static String propertiesFileName = "myProperties.properties";
    private static InputStream inputStream;
    
    public static void main(String[] args) throws IOException  {
        
        try { 
            inputStream =  SV_Stable.class.getClassLoader().getResourceAsStream(propertiesFileName);
            properties.load(inputStream);              
        }catch(Exception e){
            e.printStackTrace();
        }
        
        int PORTA = Integer.parseInt(properties.getProperty("PORT_BETTING_CENTRE"));
        
        
        Stable stable = new Stable( NO_COMPETITORS );
        
        ServerSocket servidor = new ServerSocket( PORTA );
        System.out.println("Servidor Stable ouvindo a porta " + PORTA);

        while (true) {            
            Socket cliente = servidor.accept();
            System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
            
            Runnable r = new MyRunnable( stable, cliente );
            new Thread(r).start();
        }
    }
}
