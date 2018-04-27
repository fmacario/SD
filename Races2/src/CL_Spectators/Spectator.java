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
    private Socket socketPaddock, socketBettingCentre, socketControlCentre;
    private OutputStream outPaddock, outBettingCentre, outControlCentre;
    private ObjectOutputStream oPaddock, oBettingCentre, oControlCentre;
    
    private final int PADDOCK;
    private final int BETTING_CENTRE;
    private final int CONTROL_CENTRE;
    
    /**
     * @param id Id do espetador.
     * @param NO_RACES Número de corridas.
     */
    public Spectator( int id, int NO_RACES ) throws IOException{
        this.id = id;
        this.NO_RACES = NO_RACES;
        this.money = 500;
        
        this.CONTROL_CENTRE = 12341;
        this.PADDOCK = 12343;
        this.BETTING_CENTRE = 12340;
                
        this.socketControlCentre = new Socket("localhost", CONTROL_CENTRE);
        this.socketPaddock = new Socket("localhost", PADDOCK);
        this.socketBettingCentre = new Socket("localhost", BETTING_CENTRE);
        
        this.outPaddock = socketPaddock.getOutputStream();
        this.outControlCentre = socketControlCentre.getOutputStream();        
        this.outBettingCentre = socketBettingCentre.getOutputStream();
        
        this.oPaddock = new ObjectOutputStream(outPaddock);
        this.oControlCentre = new ObjectOutputStream(outControlCentre);
        this.oBettingCentre = new ObjectOutputStream(outBettingCentre);
    }

    /**
     * run()
     */
    @Override
    public void run(){
        try {
            for (int k = 0; k < 4; k++) {

                    waitForNextRace( id, socketPaddock, outPaddock, oPaddock );
                    System.out.println("ANTES DO GO CHECK");
                    socketPaddock = new Socket("localhost", PADDOCK);
                    outPaddock = socketPaddock.getOutputStream();
                    oPaddock = new ObjectOutputStream(outPaddock);
                    goCheckHorses( id, socketPaddock, outPaddock, oPaddock );
                    System.out.println("DEPOIS DO GO CHECK");
                    money -= placeABet( id, money, socketBettingCentre, outBettingCentre, oBettingCentre );
                    goWatchTheRace( id, socketControlCentre, outControlCentre, oControlCentre );
                    socketControlCentre = new Socket("localhost", CONTROL_CENTRE);
                    outControlCentre = socketControlCentre.getOutputStream();
                    oControlCentre = new ObjectOutputStream(outControlCentre);
                    System.out.println("vou ver se ganhei ");
                    if ( haveIWon( id, socketControlCentre, outControlCentre, oControlCentre ) ) {
                        System.out.println("GANHEI! vou chamar a collectTheGains " + id);
                        socketBettingCentre = new Socket("localhost", BETTING_CENTRE);
                        outBettingCentre = socketBettingCentre.getOutputStream();
                        oBettingCentre = new ObjectOutputStream(outBettingCentre);
                        
                        money += goCollectTheGains( id, socketBettingCentre, outBettingCentre, oBettingCentre );
                        System.out.println("sai da collectTheGains " + id);
                        socketControlCentre = new Socket("localhost", CONTROL_CENTRE);
                        outControlCentre = socketControlCentre.getOutputStream();
                        oControlCentre = new ObjectOutputStream(outControlCentre);
                    }
            }
            System.out.println("spectator sai do for");
            socketControlCentre = new Socket("localhost", CONTROL_CENTRE);
            outControlCentre = socketControlCentre.getOutputStream();
            oControlCentre = new ObjectOutputStream(outControlCentre);
            relaxABit( id, socketControlCentre, outControlCentre, oControlCentre);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Bye SPECTATOR " + id);
    }

    private void waitForNextRace(int id, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();

        json.put("entidade", "spectator");
        json.put("metodo", "waitForNextRace");
        json.put("spectatorID", id);
        
        sendMessage(json, socket, out, o);
        
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
    }

    private void goCheckHorses(int id, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();

        json.put("entidade", "spectator");
        json.put("metodo", "goCheckHorses");
        json.put("spectatorID", id);
        
        sendMessage(json, socket, out, o);
        
        JSONObject res = receiveMessage( socket );
        while( res == null ){
            res = receiveMessage( socket );
        }
    }
    
    private int placeABet( int id, int money, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();

        json.put("entidade", "spectator");
        json.put("metodo", "placeABet");
        json.put("money", money);
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
        System.out.println("cliente enviei msg goCollectTheGains");
        JSONObject res = receiveMessage( socket );
        System.out.println("cliente recebi msg goCollectTheGains");
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
        System.out.println("spectator "+id+ " vou enviar msg relaxABit");
        sendMessage(json, socket, out, o);
        System.out.println("spectator "+id+ " enviei msg relaxABit");
        JSONObject res = receiveMessage( socket );
        
        System.out.println("spectator recebi resposta relaxABit");
        while( res == null ){
            res = receiveMessage( socket );
        }
    }

    private boolean haveIWon(int id, Socket socket, OutputStream out, ObjectOutputStream o) throws JSONException, IOException, ClassNotFoundException {
        JSONObject json = new JSONObject();

        json.put("entidade", "spectator");
        json.put("metodo", "haveIWon");
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