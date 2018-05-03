package CL_Broker;

import java.io.FileInputStream;
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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Thread Broker
 * @author fm
 */
public class Broker extends Thread{
    private int NO_RACES;
    
    private Map<Integer, Integer> hashHorsesAgile = new HashMap<Integer, Integer>();
    
    private Map<Integer, List<Integer>> mapSpec_Horse_Bet = new HashMap<Integer, List<Integer>>();
    private ArrayList<Integer> horsesWinnersList = new ArrayList<>();
    private ArrayList<Integer> specsWinnersList = new ArrayList<>();
    private Socket socketStable, socketBettingCentre, socketControlCentre, socketPaddock, socketRacingTrack;
    private OutputStream out;
    private ObjectOutputStream o;
    
    private int PORT_BETTING_CENTRE;
    private int PORT_CONTROL_CENTRE;
    private int PORT_PADDOCK;
    private int PORT_RACING_TRACK;
    private int PORT_STABLE;
    
    private String IP_BETTING_CENTRE;
    private String IP_CONTROL_CENTRE;
    private String IP_PADDOCK;
    private String IP_RACING_TRACK;
    private String IP_STABLE;
    
    /**
     *
     * @param NO_RACES Número de corridas.
     * @throws java.io.IOException
     */
    public Broker(int NO_RACES) throws IOException{
        Properties prop = new Properties();
	InputStream input = null;
        
        try {
            input = new FileInputStream("myProperties.properties");
            prop.load(input);
            this.NO_RACES = Integer.parseInt( prop.getProperty("NO_RACES") );
            
            this.PORT_BETTING_CENTRE = Integer.parseInt( prop.getProperty("PORT_BETTING_CENTRE") );
            this.PORT_CONTROL_CENTRE = Integer.parseInt( prop.getProperty("PORT_CONTROL_CENTRE") );
            this.PORT_PADDOCK = Integer.parseInt( prop.getProperty("PORT_PADDOCK") );
            this.PORT_RACING_TRACK = Integer.parseInt( prop.getProperty("PORT_RACING_TRACK") );
            this.PORT_STABLE = Integer.parseInt( prop.getProperty("PORT_STABLE") );
            
            this.IP_STABLE =  prop.getProperty("IP_STABLE");

	} catch (IOException ex) {
            ex.printStackTrace();
	} finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
	}

        
        //this.socketBettingCentre = new Socket("localhost", BETTING_CENTRE);
        //this.socketControlCentre = new Socket("localhost", CONTROL_CENTRE);
        //this.socketPaddock = new Socket("localhost", PADDOCK);
        //this.socketRacingTrack = new Socket("localhost", RACING_TRACK);
        System.out.println("1");
        //this.socketStable = new Socket(IP_STABLE, PORT_STABLE);
        System.out.println("2");
    }
    
    /**
     * run()
     */
    @Override
    public void run(){
        try {
            for (int k = 0; k < 4; k++) {
                    System.out.println("Vou comecar nova corrida!");
                    
                    //summonHorsesToPaddock
                    socketStable = new Socket(IP_STABLE, PORT_STABLE);
                    hashHorsesAgile = summonHorsesToPaddock(socketStable, k+1 );
                    System.out.println("sai da summonHorsesToPaddock!");
                    /*
                    //waitForSpectators
                    socketPaddock = new Socket("localhost", PADDOCK);
                    while( !waitForSpectators( socketPaddock )) {
                        System.out.println("broker está no while");
                        socketPaddock = new Socket("localhost", PADDOCK);
                    }
                    System.out.println("broker saiu do while e vai entrar na acceptTheBets");
                    
                    //acceptTheBets
                    socketBettingCentre = new Socket("localhost", BETTING_CENTRE);
                    mapSpec_Horse_Bet = acceptTheBets( hashHorsesAgile, socketBettingCentre );
                    
                    //startTheRace
                    socketRacingTrack = new Socket("localhost", RACING_TRACK);
                    horsesWinnersList = startTheRace( socketRacingTrack );
                    System.out.println("sai da startTheRace");
                    
                    //reportResults
                    socketControlCentre = new Socket("localhost", CONTROL_CENTRE);
                    specsWinnersList = reportResults( horsesWinnersList, mapSpec_Horse_Bet, socketControlCentre );
                    System.out.println("sai da reportResults");
                    
                    //areThereAnyWinners
                    socketControlCentre = new Socket("localhost", CONTROL_CENTRE);
                    if ( areThereAnyWinners( mapSpec_Horse_Bet , socketControlCentre ) ) {
                        System.out.println("Há Winners!");
                        //socketControlCentre = new Socket("localhost", CONTROL_CENTRE);
                        
                        //honourTheBets
                        socketBettingCentre = new Socket("localhost", BETTING_CENTRE);
                        honourTheBets( horsesWinnersList, specsWinnersList, socketBettingCentre);
                        System.out.println("SAI HonourBets");
                    }*/
            }
           /* System.out.println("broker saiu do for");
            
            //entertainTheGuests
            socketControlCentre = new Socket("localhost", CONTROL_CENTRE);
            entertainTheGuests( socketControlCentre );
            
            //end
            socketStable = new Socket("localhost", STABLE);
            end( socketStable );
            */
        } catch (JSONException | IOException | ClassNotFoundException ex) {
                Logger.getLogger(Broker.class.getName()).log(Level.SEVERE, null, ex);
            }
        System.out.println("Bye BROKER");
    }

    private Map<Integer, Integer> summonHorsesToPaddock(Socket socket, int k) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();
        
        json.put("entidade", "broker");
        json.put("metodo", "summonHorsesToPaddock");
        json.put("nRace", k);
        
        sendMessage(socket, json);
        
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
        
        return stringToMap( res.getString("return") );
    }

    private Map<Integer, List<Integer>> acceptTheBets(Map<Integer, Integer> hashHorsesAgile, Socket socket) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();
        
        json.put("entidade", "broker");
        json.put("metodo", "acceptTheBets");
        json.put("hashHorsesAgile", hashHorsesAgile.toString());
        
        sendMessage(socket, json);
        System.out.println("accpet the bets: broker enviu json");
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
        
        return stringToMapList( res.getString("return") );
    }

    private boolean waitForSpectators( Socket socket ) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();
        
        json.put("entidade", "broker");
        json.put("metodo", "waitForSpectators");
        
        sendMessage(socket, json);
        
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
        
        return res.getBoolean("return");
    }

    private ArrayList<Integer> startTheRace( Socket socket ) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();
        
        json.put("entidade", "broker");
        json.put("metodo", "startTheRace");
        
        sendMessage(socket, json);
        
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
        
        return stringToArrayList( res.getString("return") );
    }

    private ArrayList<Integer> reportResults(ArrayList<Integer> horsesWinnersList, Map<Integer, List<Integer>> mapSpec_Horse_Bet, Socket socket) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();
        
        json.put("entidade", "broker");
        json.put("metodo", "reportResults");
        json.put("horsesWinnersList", horsesWinnersList.toString());
        json.put("mapSpec_Horse_Bet", mapSpec_Horse_Bet.toString());
        
        sendMessage(socket, json);
        
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
        
        return stringToArrayList( res.getString("return") );
    }

    private boolean areThereAnyWinners(Map<Integer, List<Integer>> mapSpec_Horse_Bet, Socket socket) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();
        
        json.put("entidade", "broker");
        json.put("metodo", "areThereAnyWinners");
        json.put("mapSpec_Horse_Bet", mapSpec_Horse_Bet.toString());
        
        sendMessage(socket, json);
        
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
        
        return res.getBoolean("return");
    }

    private void honourTheBets(ArrayList<Integer> horsesWinnersList, ArrayList<Integer> specsWinnersList, Socket socket) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();
        
        json.put("entidade", "broker");
        json.put("metodo", "honourTheBets");
        json.put("horsesWinnersList", horsesWinnersList.toString());
        json.put("specsWinnersList", specsWinnersList.toString());
        
        sendMessage(socket, json);
        System.out.println("envio honourTheBets");
        JSONObject res = receiveMessage( socket );
        System.out.println("recebo honourTheBets");
        while( res == null ){
            res = receiveMessage( socket );
        }
    }

    private void entertainTheGuests(Socket socket) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();
        
        json.put("entidade", "broker");
        json.put("metodo", "entertainTheGuests");
        
        sendMessage(socket, json);
        
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
    }

    private void end( Socket socket ) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();
        
        json.put("entidade", "broker");
        json.put("metodo", "end");
        System.out.println("END ANTES DA MSG ENVIADA");
        sendMessage(socket, json);
        System.out.println("END MSG ENVIADA");
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
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
    
    public static Map<Integer, List<Integer>> stringToMapList( String s ){
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
    
     public static ArrayList<Integer> stringToArrayList ( String s ){
        ArrayList<Integer> arrayList = new ArrayList<>();
        
        if( s.length() == 2){
            return arrayList;
        }
        
        System.out.println("S:  - -- - - " + s);
        s = s.substring(1, s.length()-1);
        String parts[] = s.split(", ");
        for (String part : parts) {
            String[] split = part.split("=");
            arrayList.add( Integer.parseInt(split[0]));
        }
        return arrayList;
    }
    
}