package SV_RacingTrack;

import java.net.Socket;
import java.util.ArrayList;
import org.json.JSONObject;
import JSON.*;
import java.io.IOException;
import org.json.JSONException;

/**
 * class MyRunnable
 * @author fm
 */
public class MyRunnable implements Runnable {
    RacingTrack racingTrack;
    Socket socket;
    
    MyRunnable( RacingTrack racingTrack, Socket socket) {
        this.racingTrack = racingTrack;
        this.socket = socket;
    }

    /**
     * Recebe, descodifica a mensagem, chama o método pretendido e envia resposta.
     */
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

            
        } catch ( IOException | ClassNotFoundException | JSONException e ){
            e.printStackTrace();
        }
    }
}
