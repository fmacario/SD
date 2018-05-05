package SV_BettingCentre;

import java.net.Socket;
import org.json.JSONObject;
import JSON.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
                case "broker":
                    Map<Integer, Integer> hashHorsesAgile;
                    ArrayList<Integer> horsesWinnersList, specsWinnersList;
                    switch ( json.getString("metodo") ){  
                        case "acceptTheBets":
                            //System.out.println("case acceptTheBets");
                            hashHorsesAgile = JSON.stringToMap( json.getString("hashHorsesAgile") );
                            
                            //System.out.println("antes da acceptTheBets");
                            Map<Integer, List<Integer>> acceptTheBets = bettingCentre.acceptTheBets(hashHorsesAgile);
                            
                            //System.out.println("antes da acceptTheBets, " + acceptTheBets.toString());
                            jsonRes = new JSONObject();
                            jsonRes.put("return", acceptTheBets.toString());
                            JSON.sendMessage(socket, jsonRes);
                            break;
                        case "honourTheBets":
                            //System.out.println("case honourTheBets");
                            horsesWinnersList = JSON.stringToArrayList( json.getString("horsesWinnersList") );
                            specsWinnersList = JSON.stringToArrayList( json.getString("specsWinnersList") );
                            
                            bettingCentre.honourTheBets(horsesWinnersList, specsWinnersList);
                            
                            jsonRes = new JSONObject();
                            jsonRes.put("return", "void");
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
