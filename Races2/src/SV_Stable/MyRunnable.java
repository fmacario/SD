/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SV_Stable;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import JSON.*;

/**
 *
 * @author fm
 */
public class MyRunnable implements Runnable {
    Stable stable;
    Socket socket;
    
    MyRunnable( Stable stable, Socket socket) {
        this.stable = stable;
        this.socket = socket;
    }

    @Override
    public void run() {
        JSONObject json = null;
        
        try {
            json = JSON.receiveJSON( socket );
            
            switch ( json.getString("entidade") ){
                case "broker":
                    Map<Integer, Integer> hashHorsesAgile = null;
                    ArrayList<Integer> horsesWinnersList = null, specsWinnersList = null;
                    Map<Integer, List<Integer>> mapSpec_Horse_Bet = null;
            
                    switch ( json.getString("metodo") ){                
                    case "summonHorsesToPaddock":
                        hashHorsesAgile = stable.summonHorsesToPaddock(json.getInt( "nRace" ));
                        
                        JSONObject jsonRes = new JSONObject();
                        jsonRes.put("hashHorsesAgile", hashHorsesAgile.toString());
                        JSON.sendMessage(socket, jsonRes);
                        break;
                    case "waitForSpectators":

                        break;
                    case "acceptTheBets":
                        hashHorsesAgile = JSON.stringToMap( json.getString("hashHorsesAgile") );

                        break;
                    case "startTheRace":

                        break;
                    case "reportResults":
                        horsesWinnersList = JSON.stringToArrayList( json.getString("horsesWinnersList") );
                        mapSpec_Horse_Bet = JSON.stringToMapList( json.getString("mapSpec_Horse_Bet") );

                        break;
                    case "areThereAnyWinners":
                         mapSpec_Horse_Bet = JSON.stringToMapList( json.getString("mapSpec_Horse_Bet") );

                        break;
                    case "honourTheBets":
                        horsesWinnersList = JSON.stringToArrayList( json.getString("horsesWinnersList") );
                        specsWinnersList = JSON.stringToArrayList( json.getString("specsWinnersList") );                    

                        break;
                    case "entertainTheGuests":

                        break;
                    case "end":

                        break;
                    }
                    break;
                    
                case "horse":
                    switch ( json.getString("metodo") ){     
                        case "proceedToStable":
                            int id = json.getInt( "id" );
                            int Pnk = json.getInt( "Pnk" );
                            stable.proceedToStable(id, Pnk);
                        break;
                    }
                    break;
                    
                case "spectator":
                    
                    break;
            }

            
        } catch ( Exception e ){
            e.printStackTrace();
        }
    }
}
