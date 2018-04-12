package Monitores;

import Interfaces.IRacingTrack_Broker;
import Interfaces.IRacingTrack_Horse;
import Enum.*;

import java.util.*;
import java.util.concurrent.locks.*;

/**
 * Métodos da Racing Track.
 * @author fm
 */
public class RacingTrack implements IRacingTrack_Broker, IRacingTrack_Horse{
    private GRI gri;
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
        
    /**
     * 
     * @param gri General Repository of Information (GRI).
     */
    public RacingTrack(GRI gri, int NO_COMPETITORS, int TRACK_DISTANCE){
        this.gri = gri;
        this.NO_COMPETITORS = NO_COMPETITORS;
        this.TRACK_DISTANCE = TRACK_DISTANCE;
        
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
                
                gri.setBrokerState(BrokerState.SUPERVISING_THE_RACE);
                gri.updateStatus();
                
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
                nHorses++;
                                
                gri.setHorseState( horseID, HorseState.AT_THE_START_LINE);
                positions[horseID] = 0;
                gri.setTrackPosition(positions);
                gri.updateStatus();
                               
                
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
                gri.setHorseState(horseID, HorseState.RUNNING);
                gri.updateStatus();
                
                condHorses.signalAll();
                
                if( positions[horseID] < TRACK_DISTANCE ){
                    positions[horseID] += (int )(Math.random() * Pnk + 1);
                    iterations[horseID] += 1;
                    
                    gri.setTrackPosition(positions);
                    gri.setIterationNumber(iterations);
                    gri.updateStatus();
                    
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
                if( positions[horseID] >= TRACK_DISTANCE){
                    horsesFinished++;
                    
                    gri.setStandingAtTheEnd( horseID , horsesFinished );
                    
                    if ( iterations[horseID] < noMinIterations)
                        noMinIterations = iterations[horseID];
                                        
                    gri.setHorseState(horseID, HorseState.AT_THE_FINNISH_LINE);
                    gri.updateStatus();
                                                         
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
                
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } finally{        
            rl.unlock();
        }
    }

}
