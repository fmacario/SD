import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Paddock implements IPaddock_Horse, IPaddock_Spectator{
    private Map<Integer, Horse> hashHorses = new HashMap<Integer, Horse>();
    private Map<Integer, Spectator> hashSpectators = new HashMap<Integer, Spectator>();
    
    private final Condition condSpectators;
    private final Condition condHorses;
    private final Condition condBroker;
    private final ReentrantLock rl;
    
    private int nSpectators = 0;
    private int nHorses = 0;
    private static boolean GO = false;
    private final int NO_SPECTATORS = Main.NO_SPECTATORS;
    private final int NO_COMPETITORS = Main.NO_COMPETITORS;
    
    private boolean allHorsesAtPaddok = false;
    
    public Paddock(){
        rl = new ReentrantLock(true);
        condSpectators = rl.newCondition();
        condHorses = rl.newCondition();
        condBroker = rl.newCondition();
    }
    
    @Override
    public void proceedToPaddock(int horseID) {
        rl.lock();
        try{
            try{
                nHorses++;
                Horse.state = HorseState.AT_THE_PADDOCK;
                System.out.println("Horse " + horseID + " " + Horse.state);
                
                while( GO == false ){
                    //System.out.println("nHorses " + nHorses); // se ficar este print, o programa (aparentemente) funciona
                    condHorses.await();
                }
                
                allHorsesAtPaddok = true;
                
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
        try{
            try{
                nSpectators++;
                Spectator.state = SpectatorState.APPRAISING_THE_HORSES;
                System.out.println("Spectator " + spectatorID + " " + Spectator.state);
                //esperar enquanto o ultimo espetador nao chega ao paddok
                
                while( nSpectators < NO_SPECTATORS - 1 ){
                    condSpectators.await();
                }
                
                GO = true;
                condBroker.signal();
                condHorses.signalAll();
                                
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
                
                while ( !allHorsesAtPaddok ) {
                    
                }
                condSpectators.signalAll();
            } catch (Exception e) { 
                e.printStackTrace();
            }
        }finally{
            rl.unlock();
        }
    }

}
