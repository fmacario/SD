/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SV_RacingTrack;

import java.net.Socket;
import java.util.ArrayList;
import org.json.JSONObject;
import JSON.*;

/**
 *
 * @author fm
 */
public class MyRunnable implements Runnable {
    RacingTrack racingTrack;
    Socket socket;
    
    MyRunnable( RacingTrack racingTrack, Socket socket) {
        this.racingTrack = racingTrack;
        this.socket = socket;
    }

    @Override
    public void run() {
        JSONObject json = null;
        JSONObject jsonRes;
        
        try {
            json = JSON.receiveJSON( socket );
            
            switch ( json.getString("entidade") ){
                case "broker":
                    ArrayList<Integer> winnersList = new ArrayList<>();
                    
                    switch ( json.getString("metodo") ){      
                        case "startTheRace":
                            ArrayList<Integer> startTheRace = racingTrack.startTheRace();
                            
                            jsonRes = new JSONObject();
                            jsonRes.put("return", startTheRace.toString());
                            JSON.sendMessage(socket, jsonRes);                            
                            break;
                        case "end":
                            jsonRes = new JSONObject();
                            jsonRes.put("return", "void");
                            JSON.sendMessage(socket, jsonRes);
                            System.exit(1);
                            break;
                    }
                    break;
                    
                case "horse":
                    int id;
                    switch ( json.getString("metodo") ){     
                        case "proceedToStartLine":
                            id = json.getInt( "horseID" );
                            racingTrack.proceedToStartLine(id);
                            
                            jsonRes = new JSONObject();
                            jsonRes.put("return", "void");
                            JSON.sendMessage(socket, jsonRes);
                            break;
                        case "hasFinishLineBeenCrossed":
                            id = json.getInt( "horseID" );
                            boolean b = racingTrack.hasFinishLineBeenCrossed(id);
                            
                            jsonRes = new JSONObject();
                            jsonRes.put("return", b);
                            JSON.sendMessage(socket, jsonRes);
                            break;
                        case "makeAMove":
                            id = json.getInt( "horseID" );
                            int Pnk = json.getInt( "Pnk" );
                            racingTrack.makeAMove(id, Pnk);
                            
                            jsonRes = new JSONObject();
                            jsonRes.put("return", "void");
                            JSON.sendMessage(socket, jsonRes);
                            break;
                    }
                    break;
            }

            
        } catch ( Exception e ){
            e.printStackTrace();
        }
    }
}
