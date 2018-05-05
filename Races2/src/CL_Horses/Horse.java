/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CL_Horses;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;
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
    private Socket socketStable, socketPaddock, socketRacingTrack;
    private OutputStream outStable, outPaddock, outRacingTrack;
    private ObjectOutputStream oStable, oPaddock, oRacingTrack;
    
    
    private int PORT_PADDOCK;
    private int PORT_RACING_TRACK;
    private int PORT_STABLE;
    
    private String IP_PADDOCK;
    private String IP_RACING_TRACK;
    private String IP_STABLE;

    /**
     *
     * @param NO_RACES
     * @param TRACK_DISTANCE
     * @param id
     * @throws IOException
     */
    public Horse(int NO_RACES, int TRACK_DISTANCE, int id) throws IOException{
        Properties prop = new Properties();
	InputStream input = null;
        
        this.NO_RACES = NO_RACES;
        this.TRACK_DISTANCE = TRACK_DISTANCE;
        this.id = id;
        this.Pnk = (int )(Math.random() * (TRACK_DISTANCE/4) + 3); // Pnk
        
        try {
            input = new FileInputStream("myProperties.properties");
            prop.load(input);
            
            this.PORT_PADDOCK = Integer.parseInt( prop.getProperty("PORT_PADDOCK") );
            this.PORT_RACING_TRACK = Integer.parseInt( prop.getProperty("PORT_RACING_TRACK") );
            this.PORT_STABLE = Integer.parseInt( prop.getProperty("PORT_STABLE") );
            
            this.IP_PADDOCK =  prop.getProperty("IP_PADDOCK");
            this.IP_RACING_TRACK =  prop.getProperty("IP_RACING_TRACK");
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
    }
    
    /**
     * run()
     */
    @Override
    public void run(){
        try {
            for (int k = 0; k < NO_RACES ; k++) {
                //System.out.println("comecei nova corrida!");
                
                //proceedToStable
                socketStable = new Socket(IP_STABLE, PORT_STABLE);
                outStable = socketStable.getOutputStream();
                oStable = new ObjectOutputStream(outStable);
                proceedToStable( id, Pnk, socketStable, outStable, oStable );
                //System.out.println("sai da proceedToStable!");
                
                //proceedToPaddock
                socketPaddock = new Socket(IP_PADDOCK, PORT_PADDOCK);
                outPaddock = socketPaddock.getOutputStream();
                oPaddock = new ObjectOutputStream(outPaddock);
                proceedToPaddock( id, socketPaddock, outPaddock, oPaddock );
                //System.out.println("estou no paddock - chamar proceedToStartLine");
                
                //proceedToStartLine
                socketRacingTrack = new Socket(IP_RACING_TRACK, PORT_RACING_TRACK);
                outRacingTrack = socketRacingTrack.getOutputStream();
                oRacingTrack = new ObjectOutputStream(outRacingTrack);
                proceedToStartLine(id, socketRacingTrack, outRacingTrack, oRacingTrack );
                
                //hasFinishLineBeenCrossed
                socketRacingTrack = new Socket(IP_RACING_TRACK, PORT_RACING_TRACK);
                outRacingTrack = socketRacingTrack.getOutputStream();
                oRacingTrack = new ObjectOutputStream(outRacingTrack);
                
                while( !hasFinishLineBeenCrossed( id, socketRacingTrack, outRacingTrack, oRacingTrack )){
                    
                    //makeAMove
                    socketRacingTrack = new Socket(IP_RACING_TRACK, PORT_RACING_TRACK);
                    outRacingTrack = socketRacingTrack.getOutputStream();
                    oRacingTrack = new ObjectOutputStream(outRacingTrack);
                    makeAMove( id, Pnk, socketRacingTrack, outRacingTrack, oRacingTrack );
                    //System.out.println("make a move " + id);
                    
                    socketRacingTrack = new Socket(IP_RACING_TRACK, PORT_RACING_TRACK);
                    outRacingTrack = socketRacingTrack.getOutputStream();
                    oRacingTrack = new ObjectOutputStream(outRacingTrack);
                }
                    //System.out.println("acabei a corrida!");
            }
            
            //proceedToStable
            socketStable = new Socket(IP_STABLE, PORT_STABLE);
            outStable = socketStable.getOutputStream();
            oStable = new ObjectOutputStream(outStable);         
            proceedToStable( id, Pnk, socketStable, outStable, oStable );
            
            System.out.println("Bye HORSE " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void proceedToStable(int id, int Pnk, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();

        json.put("entidade", "horse");
        json.put("metodo", "proceedToStable");
        json.put("horseID", id);
        json.put("Pnk", Pnk);
        
        sendMessage(json, socket, out, o);
        
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
    }

    private void proceedToPaddock(int id, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();

        json.put("entidade", "horse");
        json.put("metodo", "proceedToPaddock");
        json.put("horseID", id);
        
        sendMessage(json, socket, out, o);
        
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
    }

    private void proceedToStartLine(int id, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();

        json.put("entidade", "horse");
        json.put("metodo", "proceedToStartLine");
        json.put("horseID", id);
        
        sendMessage(json, socket, out, o);
        
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
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
        
        return res.getBoolean("return");
    }

    private void makeAMove(int id, int Pnk, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException {
        JSONObject json = new JSONObject();

        json.put("entidade", "horse");
        json.put("metodo", "makeAMove");
        json.put("horseID", id);
        json.put("Pnk", Pnk);
        
        sendMessage(json, socket, out, o);
    }
    
    private void sendMessage( JSONObject json, Socket socket, OutputStream out, ObjectOutputStream o) throws IOException{
        o.writeObject( json.toString() );
        out.flush();
        
    }
    
    private JSONObject receiveMessage( Socket socket ) throws IOException, JSONException, ClassNotFoundException {
        InputStream in = socket.getInputStream();
        ObjectInputStream i = new ObjectInputStream(in);
        String s = (String) i.readObject();
        //System.out.println(s);
        //i.close();
        JSONObject jsonObject = new JSONObject(s);
        return jsonObject;
    }
    
}
