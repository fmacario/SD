package SV_Paddock;

import java.net.Socket;
import org.json.JSONObject;
import JSON.*;
import java.io.IOException;
import org.json.JSONException;

/**
 * class MYRunnable
 * @author fm
 */
public class MyRunnable implements Runnable {
    Paddock paddock;
    Socket socket;
    
    MyRunnable( Paddock paddock, Socket socket) {
        this.paddock = paddock;
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
                    switch ( json.getString("metodo") ){     
                        case "waitForSpectators":
                            boolean b = paddock.waitForSpectators();
                            
                            jsonRes = new JSONObject();
                            jsonRes.put("return", b);
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
                    switch ( json.getString("metodo") ){     
                        case "proceedToPaddock":
                            int horseID = json.getInt("horseID");
                            paddock.proceedToPaddock(horseID);
                            
                            jsonRes = new JSONObject();
                            jsonRes.put("return", "void");
                            JSON.sendMessage(socket, jsonRes);                            
                            break;
                    }
                    break;
                    
                case "spectator":
                    int spectatorID;
                    switch ( json.getString("metodo") ){     
                        case "goCheckHorses":
                            spectatorID = json.getInt("spectatorID");
                            paddock.goCheckHorses(spectatorID);
                            //System.out.println("GO CHECK ------------------");
                            jsonRes = new JSONObject();
                            jsonRes.put("return", "void");
                            JSON.sendMessage(socket, jsonRes);
                            break;
                        case "waitForNextRace":
                            spectatorID = json.getInt("spectatorID");
                            paddock.waitForNextRace(spectatorID);
                            
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
