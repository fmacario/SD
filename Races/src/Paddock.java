import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Paddock implements IPaddock_Horse, IPaddock_Spectator{
    private Map<Integer, Horse> hashHorses = new HashMap<Integer, Horse>();
    private Map<Integer, Spectator> hashSpectators = new HashMap<Integer, Spectator>();
    
    private GRI gri;
    private final Condition condSpectators;
    private final Condition condHorses;
    private final Condition condBroker;
    private final ReentrantLock rl;
    
    private int nSpectators = 0;
    private int nHorses = 0;
    private static boolean GO = false;
    private final int NO_SPECTATORS = Main.NO_SPECTATORS;
    private final int NO_COMPETITORS = Main.NO_COMPETITORS;
    
    private boolean lastHorse = false;
    
    public Paddock(GRI gri){
        this.gri = gri;
        rl = new ReentrantLock(true);
        condSpectators = rl.newCondition();
        condHorses = rl.newCondition();
        condBroker = rl.newCondition();
    }
    
    @Override
    public void proceedToPaddock(int horseID) {
        rl.lock();
        //System.out.println("proceedPaddock - "+horseID);
        try{
            try{
                nHorses++;
                Horse.state = HorseState.AT_THE_PADDOCK;
                System.out.println("Horse " + horseID + " " + Horse.state);
                
                if(nHorses == NO_COMPETITORS){
                    condSpectators.signalAll();
                }
                
                while( GO == false ){
                    condHorses.await();
                }                
                nHorses--;
                if(nHorses == 0){
                    lastHorse = true;
                    condSpectators.signalAll();
                }
                
            }catch( Exception e ){
                e.printStackTrace();
            }
        }finally{
            rl.unlock();
        }
    }

    @Override
    public void goCheckHorses(int spectatorID) {
        rl.lock();
        //System.out.println("checkHorses - "+spectatorID);
        try{
            try{
                nSpectators++;
                Spectator.state = SpectatorState.APPRAISING_THE_HORSES;
                System.out.println("Spectator " + spectatorID + " " + Spectator.state);
                
                if(nSpectators == NO_SPECTATORS){
                    GO = true;
                    condHorses.signalAll();
                }
                while( !lastHorse ){
                    condSpectators.await();
                }
                
                condBroker.signal();
                                
            }catch( Exception e ){
                e.printStackTrace();
            }
        }finally{
            rl.unlock();
        }
    }
    
    @Override
    public void waitForNextRace(int spectatorID){
        rl.lock();
        
        try {
            try{
                Spectator.state = SpectatorState.WAITING_FOR_A_RACE_TO_START;
                System.out.println("Spectator " + spectatorID + " " + Spectator.state);
                
                while ( nHorses != NO_COMPETITORS ) {
                    condSpectators.await();
                }
                
            } catch (Exception e) { 
                e.printStackTrace();
            }
        }finally{
            rl.unlock();
        }
    }

}
