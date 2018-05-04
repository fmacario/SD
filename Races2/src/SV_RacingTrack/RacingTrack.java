package SV_RacingTrack;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.util.*;
import java.util.concurrent.locks.*;
import org.json.JSONObject;

/**
 * Métodos da Racing Track.
 * @author fm
 */
public class RacingTrack implements IRacingTrack_Broker, IRacingTrack_Horse{
    //private GRI gri;
    private final ReentrantLock rl;
    private final Condition condHorses;
    private final Condition condBroker;
    
    private final int NO_COMPETITORS;
    private final int TRACK_DISTANCE;
    
    private Map<Integer, Integer> hashHorses = new HashMap<Integer, Integer>();
    private ArrayList<Integer> winnersList = new ArrayList<>();
    
    private int[] iterations;
    private int[] positions;
    private int nHorses = 0;
    private int horsesFinished = 0;
    private int noMinIterations = Integer.MAX_VALUE;
    private boolean raceFinished = false;
    private boolean raceStart = false;
    
    private String IP_GRI;
    private int PORT_GRI;
        
    public RacingTrack(int NO_COMPETITORS, int TRACK_DISTANCE, String IP_GRI, int PORT_GRI){
        //this.gri = gri;
        this.NO_COMPETITORS = NO_COMPETITORS;
        this.TRACK_DISTANCE = TRACK_DISTANCE;
        this.IP_GRI = IP_GRI;
        this.PORT_GRI = PORT_GRI;
        
        iterations = new int[NO_COMPETITORS];
        positions = new int[NO_COMPETITORS];
         
        for(int i=0; i < NO_COMPETITORS; i++){
            positions[i] = 0;
        }
        
        for(int i=0; i < NO_COMPETITORS; i++){
            iterations[i] = 0;
        }
        
        rl = new ReentrantLock();
        condHorses = rl.newCondition();
        condBroker = rl.newCondition();
    }
    
    /**
     * <b>Broker</b> anuncia o início da corrida.
     * @return ArrayList com o id dos cavalos vencedores.
     */
    @Override
    public ArrayList<Integer> startTheRace() {
        rl.lock();
        try {
            try {
                JSONObject json = new JSONObject();
                
                //gri.setBrokerState(BrokerState.SUPERVISING_THE_RACE);
                json.put("metodo", "setBrokerState");
                json.put("BrokerState", "SUPERVISING_THE_RACE");
                sendMessage(json);
                
                //gri.updateStatus();
                json = new JSONObject();
                json.put("metodo", "updateStatus");
                sendMessage(json);
                
                System.out.println("BrokerState.SUPERVISING_THE_RACE");
                
                raceStart = true;
                condHorses.signalAll();
                
                while( raceFinished == false ){
                    condBroker.await();
                }
                
                raceFinished = false;
                               
                return winnersList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } finally {
            rl.unlock();
        }
    }

    /**
     * <b>Cavalo</b> vai para a linha de partida.
     * @param horseID Id do cavalo.
     */
    @Override
    public void proceedToStartLine( int horseID ) {
        rl.lock();
        try{
            try {
                JSONObject json;
                
                nHorses++;    
                positions[horseID] = 0;
                
                //gri.setHorseState( horseID, HorseState.AT_THE_START_LINE);
                json = new JSONObject();
                json.put("metodo", "setHorseState");
                json.put("id", horseID);
                json.put("HorseState", "AT_THE_START_LINE");
                sendMessage(json);
                
                //gri.setTrackPosition(positions);
                json = new JSONObject();
                json.put("metodo", "setTrackPosition");
                json.put("trackPosition", Arrays.toString(positions));
                sendMessage(json);
                
                //gri.updateStatus();
                json = new JSONObject();
                json.put("metodo", "updateStatus");
                sendMessage(json);
                
                System.out.println("HorseState.AT_THE_START_LINE " +horseID);
                               
                
                while( raceStart == false ){
                    condHorses.await();
                }
                nHorses--;
                if(nHorses==0)
                    raceStart=false;
                
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }finally {
            rl.unlock();
        }
    }
    
    /**
     * <b>Cavalo</b> faz um movimento/avança.
     * @param horseID Id do cavalo.
     * @param Pnk Valor máximo de movimento numa iteração.
     */
    @Override
    public void makeAMove( int horseID, int Pnk ) {
        rl.lock();
        try {
            try {
                JSONObject json;
                
                //gri.setHorseState(horseID, HorseState.RUNNING);
                json = new JSONObject();
                json.put("metodo", "setHorseState");
                json.put("id", horseID);
                json.put("HorseState", "RUNNING");
                sendMessage(json);

                //gri.updateStatus();
                json = new JSONObject();
                json.put("metodo", "updateStatus");
                sendMessage(json);
                
                System.out.println("HorseState.RUNNING " +horseID);
                
                condHorses.signalAll();
                
                if( positions[horseID] < TRACK_DISTANCE ){
                    positions[horseID] += (int )(Math.random() * Pnk + 1);
                    iterations[horseID] += 1;
                    
                    //gri.setTrackPosition(positions);
                    json = new JSONObject();
                    json.put("metodo", "setTrackPosition");
                    json.put("trackPosition", Arrays.toString(positions));
                    sendMessage(json);
                    
                    //gri.setIterationNumber(iterations);
                    json = new JSONObject();
                    json.put("metodo", "setIterationNumber");
                    json.put("iterationNumber", Arrays.toString(iterations));
                    sendMessage(json);
                    
                    //gri.updateStatus();
                    json = new JSONObject();
                    json.put("metodo", "updateStatus");
                    sendMessage(json);
                    
                    if(horsesFinished != (NO_COMPETITORS-1)){
                        condHorses.await();
                    }
                }
                
                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
    }

    /**
     * <b>Cavalo</b> verifica se já passou a linha final.
     * @param horseID Id do cavalo.
     * @return true - se o cavalo passou a linha final.<p>false - se o cavalo não passou a linha final.
     */
    @Override
    public boolean hasFinishLineBeenCrossed( int horseID ) {
        rl.lock();
        try{
            try {
                JSONObject json;
                if( positions[horseID] >= TRACK_DISTANCE){
                    horsesFinished++;
                    
                    //gri.setStandingAtTheEnd( horseID , horsesFinished );
                    json = new JSONObject();
                    json.put("metodo", "setStandingAtTheEnd");
                    json.put("id", horseID);
                    json.put("standingAtTheEnd", horsesFinished);
                    sendMessage(json);
                    
                    if ( iterations[horseID] < noMinIterations)
                        noMinIterations = iterations[horseID];
                                        
                    //gri.setHorseState(horseID, HorseState.AT_THE_FINNISH_LINE);
                    json = new JSONObject();
                    json.put("metodo", "setHorseState");
                    json.put("id", horseID);
                    json.put("HorseState", "AT_THE_FINNISH_LINE");
                    sendMessage(json);

                    //gri.updateStatus();
                    json = new JSONObject();
                    json.put("metodo", "updateStatus");
                    sendMessage(json);
                    
                    System.out.println("HorseState.AT_THE_FINNISH_LINE " +horseID);
                                                         
                    if(horsesFinished == NO_COMPETITORS){
                        raceFinished = true;
                        horsesFinished = 0;
                        
                        /////// VENCEDORES:                        
                        for( int i = 0; i < NO_COMPETITORS; i++ ){
                            if ( iterations[i] == noMinIterations )                                    
                                winnersList.add(i);
                        }
                                            
                        condBroker.signal();
                    }
                    condHorses.signalAll();
                    
                    return true;
                }
                System.out.println("hasFinishLineBeenCrossed? FALSE " + horseID);
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } finally{        
            rl.unlock();
        }
    }

    private void sendMessage(JSONObject json) throws IOException {
        Socket socket = new Socket( IP_GRI, PORT_GRI );
        OutputStream out = socket.getOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(out);
        
        o.writeObject( json.toString() );
        out.flush();
    }
}
