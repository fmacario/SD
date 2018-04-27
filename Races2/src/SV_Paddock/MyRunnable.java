/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SV_Paddock;

import java.net.Socket;
import org.json.JSONObject;
import JSON.*;

/**
 *
 * @author fm
 */
public class MyRunnable implements Runnable {
    Paddock paddock;
    Socket socket;
    
    MyRunnable( Paddock paddock, Socket socket) {
        this.paddock = paddock;
        this.socket = socket;
    }

    @Override
    public void run() {
        JSONObject json = null;
        
        try {
            json = JSON.receiveJSON( socket );
            
            switch ( json.getString("entidade") ){
                case "broker":
                    switch ( json.getString("metodo") ){     
                        case "waitForSpectators":
                            paddock.waitForSpectators();
                            break;
                    }
                    break;
                    
                case "horse":
                    switch ( json.getString("metodo") ){     
                        case "proceedToPaddock":
                            int horseID = json.getInt("horseID");
                            paddock.proceedToPaddock(horseID);
                            break;
                    }
                    break;
                    
                case "spectator":
                    int spectatorID;
                    switch ( json.getString("metodo") ){     
                        case "goCheckHorses":
                            spectatorID = json.getInt("spectatorID");
                            paddock.goCheckHorses(spectatorID);
                            break;
                        case "waitForNextRace":
                            spectatorID = json.getInt("spectatorID");
                            paddock.waitForNextRace(spectatorID);
                            break;
                    }
                    break;
            }

            
        } catch ( Exception e ){
            e.printStackTrace();
        }
    }
}
