/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SV_Stable;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fm
 */
public class MyRunnable implements Runnable {
    Socket cliente;
    
    MyRunnable( Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        Scanner sc_msg = null;
        try {
            sc_msg = new Scanner( cliente.getInputStream() );
        } catch (IOException ex) {
            Logger.getLogger(MyRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JSONObject json = null;
        try {
            json = new JSONObject( sc_msg.nextLine() );
            System.out.println(json.toString());
        } catch (JSONException ex) {
            Logger.getLogger(MyRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            String metodo = json.get("metodo").toString();
            System.out.println(metodo);
            switch (metodo){
                case "[\"queroIrPara\"]":
                    System.out.println( json.get("teste"));
                    break;
            }
                            
            //String msg = sc_msg.nextLine();
            
            //System.out.println(msg);
        } catch (JSONException ex) {
            Logger.getLogger(MyRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
