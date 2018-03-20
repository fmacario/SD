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
    private boolean allSpectatorsAtPaddock = false;
    private boolean allHorsesAtPaddock = false;
    
    private final int NO_SPECTATORS = Main.NO_SPECTATORS;
    private final int NO_COMPETITORS = Main.NO_COMPETITORS;
    
    public Paddock(){
        rl = new ReentrantLock(true);
        condSpectators = rl.newCondition();
        condHorses = rl.newCondition();
        condBroker = rl.newCondition();
    }
    
    @Override
    public void proceedToPaddock( int horseID) {
        rl.lock();
        try{
            try{
                nHorses++;
                                
                System.out.println(nHorses +" "+ NO_COMPETITORS);
                
                while ( nHorses != NO_COMPETITORS ) {
                    condSpectators.await();
                }
                
                Horse.state = HorseState.AT_THE_PADDOCK;
                System.out.println("Horse " + horseID + " " + Horse.state);
                
                allHorsesAtPaddock = true;
                condSpectators.signalAll();
                
                if (!allSpectatorsAtPaddock) {
                    condHorses.await();
                }
                                
            }catch( Exception e ){
                e.printStackTrace();
            }
        }finally{
            rl.unlock();
        }
    }

    @Override
    public void goCheckHorses( int spectatorID) {
        rl.lock();
        try{
            try{
                while ( !allHorsesAtPaddock ) {                    
                    condSpectators.await();
                }
                
                System.out.println("TESTEE");
                
                nSpectators++;
                Spectator.state = SpectatorState.APPRAISING_THE_HORSES;
                System.out.println("Spectator " + spectatorID + " " + Spectator.state);
                     
                //esperar enquanto o ultimo espetador nao chega ao paddok
                while ( nSpectators != NO_SPECTATORS ) {
                    condHorses.await();
                }
                
                allSpectatorsAtPaddock = true;
                condHorses.signalAll();
                condBroker.signal();
                

            }catch( Exception e ){
                e.printStackTrace();
            }
        }finally{
            rl.unlock();
        }
    }
    
    @Override
    public void waitForNextRace( int spectatorID ){
        return;
       /* rl.lock();
        
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
        }*/
    }
    
    
    
    // proceedToStartLine
    

}
