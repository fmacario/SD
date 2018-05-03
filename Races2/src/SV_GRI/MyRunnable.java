/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SV_GRI;

import java.net.Socket;
import org.json.JSONObject;
import JSON.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fm
 */
public class MyRunnable implements Runnable {
    GRI gri;
    Socket socket;
    
    MyRunnable( GRI gri, Socket socket) {
        this.gri = gri;
        this.socket = socket;
    }

    @Override
    public void run() {
        JSONObject json;
        JSONObject jsonRes;
        
        try {
            json = JSON.receiveJSON( socket );
            
            switch ( json.getString("metodo") ){
                case "updateStatus":
                    System.out.println("case updateStatus");
                    gri.updateStatus();
                    
                    
                    break;
                    
                case "setBrokerState":
                    System.out.println("case setBrokerState");
                    
                    json.getString("");
                    
                    break;
                    
                case "setSpectatorState":
                    System.out.println("case setSpectatorState");
                    break;
                    
                case "setMoney":
                    System.out.println("case setMoney");
                    break;
                    
                case "addMoney":
                    System.out.println("case addMoney");
                    break;
                    
                case "setRn":
                    System.out.println("case setRn");
                    break;
                    
                case "setHorseState":
                    System.out.println("case setHorseState");
                    break;
                    
                case "setHorseMaxDistance":
                    System.out.println("case setHorseMaxDistance");
                    break;
                    
                case "setRaceTrackDistance":
                    System.out.println("case setRaceTrackDistance");
                    break;
                    
                case "setBetSelection":
                    System.out.println("case setBetSelection");
                    break;
                    
                case "setBetAmount":
                    System.out.println("case setBetAmount");
                    break;
                    
                case "setHorseWinningProb":
                    System.out.println("case setHorseWinningProb");
                    break;
                    
                case "setIterationNumber":
                    System.out.println("case setIterationNumber");
                    break;
                    
                case "setTrackPosition":
                    System.out.println("case setTrackPosition");
                    break;
                    
                case "setStandingAtTheEnd":
                    System.out.println("case setStandingAtTheEnd");
                    break;
            }
                

            
        } catch ( Exception e ){
            e.printStackTrace();
        }
    }
}
