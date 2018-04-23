/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CL_Horses;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Thread Horse
 * @author fm
 */
public class Horse extends Thread{

    private final int id;
    private final int Pnk;
    private final int NO_RACES;
    private final int TRACK_DISTANCE;
    private Socket socket;
    private OutputStream out;
    private ObjectOutputStream o;
    

    public Horse(int NO_RACES, int TRACK_DISTANCE, int id) throws IOException{
        this.NO_RACES = NO_RACES;
        this.TRACK_DISTANCE = TRACK_DISTANCE;
        this.id = id;
        this.Pnk = (int )(Math.random() * (TRACK_DISTANCE/4) + 3); // Pnk
        this.socket = new Socket("localhost", 12345);
        this.out = socket.getOutputStream();
        this.o = new ObjectOutputStream(out);
    }
    
    /**
     * run()
     */
    @Override
    public void run(){
        try {
            
            for (int k = 0; k < 1 ; k++) {

                proceedToStable( id, Pnk );
                //proceedToPaddock(id);
                //proceedToStartLine(id);
                //while( !hasFinishLineBeenCrossed( id ) ){
                  //  makeAMove( id, Pnk );
                //}
            }
            //proceedToStable(id, Pnk);
            //System.out.println("Bye HORSE " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void proceedToStable(int id, int Pnk) throws JSONException, IOException {
        JSONObject json = new JSONObject();

        json.put("entidade", "horse");
        json.put("metodo", "proceedToStable");
        json.put("id", id);
        json.put("Pnk", Pnk);
        
        sendMessage(json);
    }

    private void proceedToPaddock(int id) throws JSONException, IOException {
        JSONObject json = new JSONObject();

        json.put("entidade", "horse");
        json.put("metodo", "proceedToPaddock");
        json.put("id", id);
        
        sendMessage(json);
    }

    private void proceedToStartLine(int id) throws JSONException, IOException {
        JSONObject json = new JSONObject();

        json.put("entidade", "horse");
        json.put("metodo", "proceedToStartLine");
        json.put("id", id);
        
        sendMessage(json);
    }

    private boolean hasFinishLineBeenCrossed(int id) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();

        json.put("entidade", "horse");
        json.put("metodo", "hasFinishLineBeenCrossed");
        json.put("id", id);
        
        sendMessage(json);
        
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
        
        String s = res.getString("boolean");
        if ( s.equals("true"))
            return true;
        return false;
    }

    private void makeAMove(int id, int Pnk) throws JSONException, IOException {
        JSONObject json = new JSONObject();

        json.put("entidade", "horse");
        json.put("metodo", "hasFinishLineBeenCrossed");
        json.put("id", id);
        json.put("Pnk", Pnk);
        
        sendMessage(json);
    }
    
    private void sendMessage( JSONObject json ) throws IOException{
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
