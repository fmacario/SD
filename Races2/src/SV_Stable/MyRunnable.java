package SV_Stable;

import java.net.Socket;
import java.util.Map;
import org.json.JSONObject;
import JSON.*;
import java.io.IOException;
import org.json.JSONException;

/**
 * class MyRunnable.
 * @author fm
 */
public class MyRunnable implements Runnable {
    Stable stable;
    Socket socket;
    
    MyRunnable( Stable stable, Socket socket) {
        this.stable = stable;
        this.socket = socket;
    }

    /**
     * Recebe, descodifica a mensagem, chama o método pretendido e envia resposta.
     */
    @Override
    public void run() {
        JSONObject json = null;
        
        try {
            json = JSON.receiveJSON( socket );
            //System.out.println(json.toString());
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
                            System.exit(1);
                            break;
                    }
                    break;
                    
                case "horse":
                    switch ( json.getString("metodo") ){     
                        case "proceedToStable":
                            //System.out.println("MyRunnable : proceedToStable");
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

            
        } catch ( IOException | ClassNotFoundException | JSONException e ){
            e.printStackTrace();
        }
    }
}
