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
    private Socket socketStable, socketPaddock;
    private OutputStream outStable, outPaddock;
    private ObjectOutputStream oStable, oPaddock;
    

    public Horse(int NO_RACES, int TRACK_DISTANCE, int id) throws IOException{
        this.NO_RACES = NO_RACES;
        this.TRACK_DISTANCE = TRACK_DISTANCE;
        this.id = id;
        this.Pnk = (int )(Math.random() * (TRACK_DISTANCE/4) + 3); // Pnk
        
        this.socketStable = new Socket("localhost", 12345);
        this.socketPaddock = new Socket("localhost", 12343);
        
        this.outStable = socketStable.getOutputStream();
        this.outPaddock = socketPaddock.getOutputStream();
        
        this.oStable = new ObjectOutputStream(outStable);
        this.oPaddock = new ObjectOutputStream(outPaddock);
    }
    
    /**
     * run()
     */
    @Override
    public void run(){
        try {
            for (int k = 0; k < 1 ; k++) {

                proceedToStable( id, Pnk, socketStable, outStable, oStable );
                proceedToPaddock( id, socketPaddock, outPaddock, oPaddock );
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

    private void proceedToStable(int id, int Pnk, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException {
        JSONObject json = new JSONObject();

        json.put("entidade", "horse");
        json.put("metodo", "proceedToStable");
        json.put("horseID", id);
        json.put("Pnk", Pnk);
        
        sendMessage(json, socket, out, o);
    }

    private void proceedToPaddock(int id, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException {
        JSONObject json = new JSONObject();

        json.put("entidade", "horse");
        json.put("metodo", "proceedToPaddock");
        json.put("horseID", id);
        
        sendMessage(json, socket, out, o);
    }

    private void proceedToStartLine(int id, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException {
        JSONObject json = new JSONObject();

        json.put("entidade", "horse");
        json.put("metodo", "proceedToStartLine");
        json.put("horseID", id);
        
        sendMessage(json, socket, out, o);
    }

    private boolean hasFinishLineBeenCrossed(int id, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();

        json.put("entidade", "horse");
        json.put("metodo", "hasFinishLineBeenCrossed");
        json.put("horseID", id);
        
        sendMessage(json, socket, out, o);
        
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
        
        String s = res.getString("boolean");
        if ( s.equals("true"))
            return true;
        return false;
    }

    private void makeAMove(int id, int Pnk, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException {
        JSONObject json = new JSONObject();

        json.put("entidade", "horse");
        json.put("metodo", "hasFinishLineBeenCrossed");
        json.put("horseID", id);
        json.put("Pnk", Pnk);
        
        sendMessage(json, socket, out, o);
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
