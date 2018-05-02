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
            System.out.println(json.toString());
            JSONObject jsonRes;
            
            switch ( json.getString("entidade") ){
                case "broker":
                    Map<Integer, Integer> hashHorsesAgile;
            
                    switch ( json.getString("metodo") ){                
                        case "summonHorsesToPaddock":
                            hashHorsesAgile = stable.summonHorsesToPaddock(json.getInt( "nRace" ));

                            jsonRes = new JSONObject();
                            jsonRes.put("return", hashHorsesAgile.toString());
                            JSON.sendMessage(socket, jsonRes);
                            break;
                        case "end":
                            stable.end();
                            
                            jsonRes = new JSONObject();
                            jsonRes.put("return", "void");
                            JSON.sendMessage(socket, jsonRes);
                            break;
                    }
                    break;
                    
                case "horse":
                    switch ( json.getString("metodo") ){     
                        case "proceedToStable":
                            System.out.println("MyRunnable : proceedToStable");
                            int id = json.getInt( "horseID" );
                            int Pnk = json.getInt( "Pnk" );
                            stable.proceedToStable(id, Pnk);
                            
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
