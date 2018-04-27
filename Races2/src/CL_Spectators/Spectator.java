package CL_Spectators;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Thread Spectator
 * @author fm
 */
public class Spectator extends Thread{

    private final int id;
    private int money;
    private final int NO_RACES;
    private Socket socketPaddock, socketBettingCentre;
    private OutputStream outPaddock, outBettingCentre;
    private ObjectOutputStream oPaddock, oBettingCentre;
    
    private final int STABLE;
    private final int BETTING_CENTRE;
    
    /**
     * @param id Id do espetador.
     * @param NO_RACES Número de corridas.
     */
    public Spectator( int id, int NO_RACES ) throws IOException{
        this.id = id;
        this.NO_RACES = NO_RACES;
        this.money = 500;
        
        this.STABLE = 12345;
        this.BETTING_CENTRE = 12340;
                
        this.socketPaddock = new Socket("localhost", STABLE);
        this.socketBettingCentre = new Socket("localhost", BETTING_CENTRE);
        
        this.outPaddock = socketPaddock.getOutputStream();
        this.outBettingCentre = socketBettingCentre.getOutputStream();        
        
        this.oPaddock = new ObjectOutputStream(outPaddock);
        this.oBettingCentre = new ObjectOutputStream(outBettingCentre);
    }

    /**
     * run()
     */
    @Override
    public void run(){
        try {
            for (int k = 0; k < 1; k++) {

                    waitForNextRace( id, socketPaddock, outPaddock, oPaddock );
                    goCheckHorses( id, socketPaddock, outPaddock, oPaddock );
                    money -= placeABet( id, money, socketBettingCentre, outBettingCentre, oBettingCentre );
                    //goWatchTheRace( id );
                    //if ( haveIWon( id ) ) {
                    //     money += goCollectTheGains( id, socketBettingCentre, outBettingCentre, oBettingCentre );
                    //}
            }
            //relaxABit( id );

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("Bye SPECTATOR " + id);
    }

    private void waitForNextRace(int id, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException {
        JSONObject json = new JSONObject();

        json.put("entidade", "spectator");
        json.put("metodo", "waitForNextRace");
        json.put("spectatorID", id);
        
        sendMessage(json, socket, out, o);
    }

    private void goCheckHorses(int id, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException {
        JSONObject json = new JSONObject();

        json.put("entidade", "spectator");
        json.put("metodo", "goCheckHorses");
        json.put("spectatorID", id);
        
        sendMessage(json, socket, out, o);
    }
    
    private int placeABet( int id, int money, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();

        json.put("entidade", "spectator");
        json.put("metodo", "placeABet");
        json.put("spectatorID", id);
        
        sendMessage(json, socket, out, o);
        
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
        
        return res.getInt("return");
    }
    
    private int goCollectTheGains( int id , Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();

        json.put("entidade", "spectator");
        json.put("metodo", "goCollectTheGains");
        json.put("spectatorID", id);
        
        sendMessage(json, socket, out, o);
        
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
        
        return res.getInt("return");
    }

    private void goWatchTheRace(int id, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();

        json.put("entidade", "spectator");
        json.put("metodo", "goWatchTheRace");
        json.put("spectatorID", id);
        
        sendMessage(json, socket, out, o);
        
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
    }

    private void relaxABit(int id, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();

        json.put("entidade", "spectator");
        json.put("metodo", "relaxABit");
        json.put("spectatorID", id);
        
        sendMessage(json, socket, out, o);
        
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
    }

    private boolean haveIWon(int id, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();

        json.put("entidade", "spectator");
        json.put("metodo", "relaxABit");
        json.put("spectatorID", id);
        
        sendMessage(json, socket, out, o);
        
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
        
       return res.getBoolean("return");
    }
    
     private void sendMessage( JSONObject json, Socket socket, OutputStream out, ObjectOutputStream o) throws IOException{
        o.writeObject( json.toString() );
        out.flush();
    }
    
    private JSONObject receiveMessage( Socket socket ) throws IOException, JSONException, ClassNotFoundException {
        InputStream in = socket.getInputStream();
        ObjectInputStream i = new ObjectInputStream(in);
        String s = (String) i.readObject();
        System.out.println(s);
        //i.close();
        JSONObject jsonObject = new JSONObject(s);
        return jsonObject;
    }
}