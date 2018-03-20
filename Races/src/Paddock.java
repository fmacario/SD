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
    public void proceedToPaddock() {
        rl.lock();
        try{
            try{
                nHorses++;
                Horse.state = HorseState.AT_THE_PADDOCK;
                
                while( nHorses != NO_COMPETITORS ){
                    //System.out.println("nHorses " + nHorses); // se ficar este print, o programa (aparentemente) funciona
                    condSpectators.await();
                }
                
                //System.out.println(nHorses);
                allHorsesAtPaddok = true;
                             
                
                condSpectators.signalAll();
            }catch( Exception e ){
                e.printStackTrace();
            }
        }finally{
            rl.unlock();
        }
    }

    @Override
    public void goCheckHorses() {
        rl.lock();
        try{
            try{
                nSpectators++;
                     
                //esperar enquanto o ultimo espetador nao chega ao paddok
                
                while( nSpectators != NO_SPECTATORS ){
                    condHorses.await();
                }
                
                /*
                while ( !allHorsesAtPaddok ) {
                    condSpectators.await();
                }
                */
                Spectator.state = SpectatorState.APPRAISING_THE_HORSES;
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
    public void waitForNextRace(){
        rl.lock();
        
        try {
            try{
                while ( !allHorsesAtPaddok ) {
                    condSpectators.await();
                    Spectator.state = SpectatorState.WAITING_FOR_A_RACE_TO_START;
                }
                
            } catch (Exception e) { 
                e.printStackTrace();
            }
        }finally{
            rl.unlock();
        }
    }
    
    
    
    // proceedToStartLine
    

}
