/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CL_Broker;

import java.io.IOException;
import org.json.JSONException;



/**
 *
 * @author fm
 */
public class MainBroker {
    
    /**
     * Número de corridas (K).
     */
    public static final int NO_RACES = 4;
    
    public static void main(String[] args) throws IOException, ClassNotFoundException, JSONException {
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