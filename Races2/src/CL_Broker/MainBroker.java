/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CL_Broker;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.json.JSONException;


/**
 *
 * @author fm
 */
public class MainBroker {
    
    /**
     * Número de corridas (K).
     */
    private static int NO_RACES;
   
    public static void main(String[] args) throws IOException, ClassNotFoundException, JSONException {
        
        Properties prop = new Properties();
	InputStream input = null;
        
        try {
            input = new FileInputStream("myProperties.properties");
            prop.load(input);
            NO_RACES = Integer.parseInt( prop.getProperty("NO_RACES") );

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
        
        System.out.println("BROKER");
        
        Broker broker = new Broker(NO_RACES);
        broker.start();
       
        //gri.setBrokerState( BrokerState.OPENING_THE_EVENT );
        //gri.updateStatus();
        
        while (true) {            
            try {

                broker.join();

                System.exit(0);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }  
        }
    }
}
