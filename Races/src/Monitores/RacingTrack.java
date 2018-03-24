package Monitores;

import Main.Main;
import Threads.Horse;
import Interfaces.IRacingTrack_Broker;
import Interfaces.IRacingTrack_Horse;
import Enum.*;
import Threads.Broker;

import java.util.*;
import java.util.concurrent.locks.*;

public class RacingTrack implements IRacingTrack_Broker, IRacingTrack_Horse{
    private Map<Integer, Horse> hashHorses = new HashMap<Integer, Horse>();
    private ArrayList<Integer> winnersList = new ArrayList<>();
            
    private GRI gri;
    private final ReentrantLock rl;
    private final Condition condHorses;
    private final Condition condBroker;
    private final Condition condSpectators;
    
    private int nHorses = 0;
    boolean raceFinished = false;
    boolean raceStart = false;
    private final int NO_COMPETITORS = Main.NO_COMPETITORS;
    private int[] positions = new int[NO_COMPETITORS];
    private int[] iterations = new int[NO_COMPETITORS];
    private int noMinIterations = Integer.MAX_VALUE;
    
    private int horsesFinished=0;
    
    public RacingTrack(GRI gri){
        this.gri = gri;
         
        for(int i=0; i < NO_COMPETITORS; i++){
            positions[i] = 0;
        }
        
        for(int i=0; i < NO_COMPETITORS; i++){
            iterations[i] = 0;
        }
        
        rl = new ReentrantLock();
        condHorses = rl.newCondition();
        condBroker = rl.newCondition();
        condSpectators = rl.newCondition();
    }
    
    @Override
    public ArrayList<Integer> startTheRace() {
        rl.lock();
        
        //System.out.println("startTheRace");
        
        try {
            try {
                
                gri.setBrokerState(BrokerState.SUPERVISING_THE_RACE);
                gri.updateStatus();
                //System.out.println("Broker " + BrokerState.SUPERVISING_THE_RACE);
                
                raceStart = true;
                condHorses.signalAll();
                
                while( raceFinished == false ){
                    condBroker.await();
                }
                
                               
                return winnersList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } finally {
            rl.unlock();
        }
    }

    @Override
    public void proceedToStartLine( int horseID ) {
        rl.lock();
        
        //System.out.println("proceed to start line");
        
        try{
            try {
                nHorses++;
                                
                gri.setHorseState( horseID, HorseState.AT_THE_START_LINE);
                positions[horseID] = 0;
                gri.setTrackPosition(positions);
                gri.updateStatus();
                //System.out.println("Horse "+ horseID +" "+  HorseState.AT_THE_START_LINE);
                
                if(nHorses == NO_COMPETITORS){
                    condSpectators.signalAll();
                }
                
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
    
    @Override
    public void makeAMove( int horseID, int Pnk ) {
        rl.lock();
        
        //System.out.println("makeAMove");
        
        try {
            try {
                gri.setHorseState(horseID, HorseState.RUNNING);
                gri.updateStatus();
                //System.out.println("Horse " + horseID +" "+ HorseState.RUNNING+" "+ positions[horseID]);
                
                condHorses.signalAll();
                
                if( positions[horseID] < Main.TRACK_DISTANCE ){
                    positions[horseID] += (int )(Math.random() * Pnk + 1);
                    iterations[horseID] += 1;
                    //System.out.println("position inc - " + positions[horse.getHorseId()]);
                    
                    gri.setTrackPosition(positions);
                    gri.setIterationNumber(iterations);
                    gri.updateStatus();
                    
                    System.out.println("horsesfinished " + horsesFinished);
                    if(horsesFinished != (NO_COMPETITORS-1)){
                        condHorses.await();
                    }
                    
                    // falta acordar o broker quando o ultimo cavalo chega À meta
                }
                
                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
    }

    @Override
    public boolean hasFinishLineBeenCrossed( int horseID ) {
        rl.lock();
        
        //System.out.println("finish line check" + horseID);
            
        try{
            try {
                if( positions[horseID] >= Main.TRACK_DISTANCE){
                    horsesFinished++;
                    
                    gri.setStandingAtTheEnd( horseID , horsesFinished );
                    
                    if ( iterations[horseID] < noMinIterations)
                        noMinIterations = iterations[horseID];
                                        
                    gri.setHorseState(horseID, HorseState.AT_THE_FINNISH_LINE);
                    gri.updateStatus();
                                                         
                    //System.out.println("FINISHED - " + horseID + " - " +horsesFinished);
                    if(horsesFinished == NO_COMPETITORS){
                        raceFinished = true;
                        horsesFinished = 0;
                        //System.out.println("ALL FINISHED");
                        
                        
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
