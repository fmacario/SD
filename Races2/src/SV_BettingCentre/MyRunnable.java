package SV_BettingCentre;

import java.net.Socket;
import org.json.JSONObject;
import JSON.*;
import java.io.IOException;
import org.json.JSONException;

/**
 * class MyRunnable.
 * @author fm
 */
public class MyRunnable implements Runnable {
    BettingCentre bettingCentre;
    Socket socket;
    
    MyRunnable( BettingCentre bettingCentre, Socket socket) {
        this.bettingCentre = bettingCentre;
        this.socket = socket;
    }

    /**
     * Recebe, descodifica a mensagem, chama o método pretendido e envia resposta.
     */
    @Override
    public void run() {
        JSONObject json;
        JSONObject jsonRes;
        
        try {
            json = JSON.receiveJSON( socket );
            
            switch ( json.getString("entidade") ){
                
                    
                case "spectator":
                    int spectatorID;
                    switch ( json.getString("metodo") ){     
                        case "placeABet":
                            spectatorID = json.getInt("spectatorID");
                            int money = json.getInt("money");
                            //System.out.println("antes de chamar a funcao placeABet");
                            int bet = bettingCentre.placeABet(spectatorID, money);
                            //System.out.println("depois de chamar a funcao placeABet,bet = " + spectatorID +  " , valor: " +bet);
                            jsonRes = new JSONObject();
                            jsonRes.put("return", bet);
                            JSON.sendMessage(socket, jsonRes);
                            break;
                        case "goCollectTheGains":
                            //System.out.println("servidor recebi msg goCollectTheGains");
                            spectatorID = json.getInt("spectatorID");
                            int gains = bettingCentre.goCollectTheGains(spectatorID);
                            //System.out.println("servidor enviei msg goCollectTheGains");
                            jsonRes = new JSONObject();
                            jsonRes.put("return", gains);
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
