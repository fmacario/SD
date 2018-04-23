package CL_Broker;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Thread Broker
 * @author fm
 */
public class Broker extends Thread{
    private final int NO_RACES;
    
    private Map<Integer, Integer> hashHorsesAgile = new HashMap<Integer, Integer>();
    
    private Map<Integer, List<Integer>> mapSpec_Horse_Bet = new HashMap<Integer, List<Integer>>();
    private ArrayList<Integer> horsesWinnersList = new ArrayList<>();
    private ArrayList<Integer> specsWinnersList = new ArrayList<>();
    private Socket socketStable;
    private OutputStream out;
    private ObjectOutputStream o;
    
    /**
     *
     * @param NO_RACES Número de corridas.
     * @throws java.io.IOException
     */
    public Broker(int NO_RACES) throws IOException{
        this.NO_RACES = NO_RACES;
        this.socketStable = new Socket("localhost", 12345);
        //this.out = socketStable.getOutputStream();
        //this.o = new ObjectOutputStream(out);
    }
    
    /**
     * run()
     */
    @Override
    public void run(){
        
        for (int k = 0; k < 1; k++) {
            
            try {
                
                hashHorsesAgile = summonHorsesToPaddock(socketStable, k+1 );
                System.out.println(hashHorsesAgile.toString());
                //while( !waitForSpectators()) {
                
                //}
                //mapSpec_Horse_Bet = acceptTheBets(hashHorsesAgile);
                //horsesWinnersList = startTheRace();
                //specsWinnersList = reportResults( horsesWinnersList, mapSpec_Horse_Bet );
                //if ( areThereAnyWinners( mapSpec_Horse_Bet ) ) {
                //  honourTheBets( horsesWinnersList, specsWinnersList );
                //}
            } catch (JSONException | IOException | ClassNotFoundException ex) {
                Logger.getLogger(Broker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //entertainTheGuests();
        //end();
        //System.out.println("Bye BROKER");
    }

    private Map<Integer, Integer> summonHorsesToPaddock(Socket socket, int k) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();
        
        json.put("entidade", "broker");
        json.put("metodo", "summonHorsesToPaddock");
        json.put("nRace", k);
        System.out.println(json);
        sendMessage(socket, json);
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
        
        return stringToMap( res.getString("hashHorsesAgile") );
    }

    private Map<Integer, List<Integer>> acceptTheBets(Map<Integer, Integer> hashHorsesAgile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean waitForSpectators() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ArrayList<Integer> startTheRace() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ArrayList<Integer> reportResults(ArrayList<Integer> horsesWinnersList, Map<Integer, List<Integer>> mapSpec_Horse_Bet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean areThereAnyWinners(Map<Integer, List<Integer>> mapSpec_Horse_Bet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void honourTheBets(ArrayList<Integer> horsesWinnersList, ArrayList<Integer> specsWinnersList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void entertainTheGuests() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void end() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void sendMessage(Socket socket, JSONObject json ) throws IOException{
        out = socket.getOutputStream();
        o = new ObjectOutputStream(out);
        
        o.writeObject( json.toString() );
        out.flush();
        
    }
    
    
    public JSONObject receiveMessage( Socket socket ) throws IOException, JSONException, ClassNotFoundException {
        InputStream in = socket.getInputStream();
        ObjectInputStream i = new ObjectInputStream(in);
        String s = (String) i.readObject();
        System.out.println(s);
        //i.close();
        JSONObject jsonObject = new JSONObject(s);
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
}