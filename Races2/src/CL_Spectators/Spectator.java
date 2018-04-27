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
    private Socket socketPaddock;
    private OutputStream outPaddock;
    private ObjectOutputStream oPaddock;
    
    /**
     * @param id Id do espetador.
     * @param NO_RACES Número de corridas.
     */
    public Spectator( int id, int NO_RACES ) throws IOException{
        this.id = id;
        this.NO_RACES = NO_RACES;
        this.money = 500;
        this.socketPaddock = new Socket("localhost", 12343);
        this.outPaddock = socketPaddock.getOutputStream();
        this.oPaddock = new ObjectOutputStream(outPaddock);
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
                    //money -= placeABet( id, money);
                    //goWatchTheRace( id );
                    //if ( haveIWon( id ) ) {
                    //     money += goCollectTheGains( id );
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

        json.put("entidade", "horse");
        json.put("metodo", "proceedToStable");
        json.put("spectatorID", id);
        
        sendMessage(json, socket, out, o);
    }

    private void goCheckHorses(int id, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException {
        JSONObject json = new JSONObject();

        json.put("entidade", "horse");
        json.put("metodo", "proceedToStable");
        json.put("spectatorID", id);
        
        sendMessage(json, socket, out, o);
    }
    
    private int placeABet( int id, int money, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private int goCollectTheGains( int id , Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void goWatchTheRace(int id, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void relaxABit(int id, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean haveIWon(int id, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
     private void sendMessage( JSONObject json, Socket socket, OutputStream out, ObjectOutputStream o) throws IOException{
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
}