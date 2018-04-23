/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SV_Stable;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

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
            json = receiveJSON();
            
            switch ( json.getString("entidade") ){
                case "broker":
                    Map<Integer, Integer> hashHorsesAgile = null;
                    ArrayList<Integer> horsesWinnersList = null, specsWinnersList = null;
                    Map<Integer, List<Integer>> mapSpec_Horse_Bet = null;
            
                    switch ( json.getString("metodo") ){                
                    case "summonHorsesToPaddock":
                        int nRace = json.getInt( "nRace" );
                        hashHorsesAgile = stable.summonHorsesToPaddock(nRace);
                        System.out.println(hashHorsesAgile.toString());
                        JSONObject jsonRes = new JSONObject();
                        jsonRes.put("hashHorsesAgile", hashHorsesAgile);
                        sendMessage(jsonRes);
                        break;
                    case "waitForSpectators":

                        break;
                    case "acceptTheBets":
                        hashHorsesAgile = stringToMap( json.getString("hashHorsesAgile") );

                        break;
                    case "startTheRace":

                        break;
                    case "reportResults":
                        horsesWinnersList = stringToArrayList( json.getString("horsesWinnersList") );
                        mapSpec_Horse_Bet = stringToMapList( json.getString("mapSpec_Horse_Bet") );

                        break;
                    case "areThereAnyWinners":
                         mapSpec_Horse_Bet = stringToMapList( json.getString("mapSpec_Horse_Bet") );

                        break;
                    case "honourTheBets":
                        horsesWinnersList = stringToArrayList( json.getString("horsesWinnersList") );
                        specsWinnersList = stringToArrayList( json.getString("specsWinnersList") );                    

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
    
    
    public JSONObject receiveJSON() throws IOException, JSONException, ClassNotFoundException {
            InputStream in = socket.getInputStream();
            ObjectInputStream i = new ObjectInputStream(in);
            String s = (String) i.readObject();
            System.out.println(s);
            i.close();

            JSONObject jsonObject = new JSONObject(s);
            System.out.println( jsonObject.getString("metodo") );
            return jsonObject;
        }
    
    
    public Map<Integer, Integer> stringToMap( String s ){
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        
        s = s.substring(1, s.length()-1);
        String parts[] = s.split(", ");
        for (String part : parts) {
            String[] split = part.split("=");
            map.put( Integer.parseInt(split[0]), Integer.parseInt(split[1]) );
        }
        return map;
    }
    
    public Map<Integer, List<Integer>> stringToMapList( String s ){
        Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
        
        s = s.substring(1, s.length()-2);
        String parts[] = s.split("], ");
        
        for (String part : parts) {
            String[] split = part.split("=");
            
            split[1] = split[1].substring(1);
            
            String[] splitLista = split[1].split(", ");
            
            List<Integer> lista = new ArrayList<Integer>();
            lista.add(0, Integer.parseInt(splitLista[0]));
            lista.add(1, Integer.parseInt(splitLista[1]));
            
            map.put(Integer.parseInt(split[0]), lista);
        }
        
        return map;
    }
            
    
    
    public ArrayList<Integer> stringToArrayList ( String s ){
        ArrayList<Integer> arrayList = new ArrayList<>();
        
        s = s.substring(1, s.length()-1);
        String parts[] = s.split(", ");
        for (String part : parts) {
            String[] split = part.split("=");
            arrayList.add( Integer.parseInt(split[0]));
        }
        return arrayList;
    }
    
    
    private void sendMessage( JSONObject json ) throws IOException{
        OutputStream out = socket.getOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(out);

        o.writeObject( json.toString() );
        out.flush();
        
    }
    
}
