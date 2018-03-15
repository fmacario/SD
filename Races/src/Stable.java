import java.util.*;
import static java.lang.System.*;
import java.util.concurrent.locks.*;
        
public class Stable implements IStable_Broker, IStable_Horse{
    private Map<Integer, Horse> hashHorses = new HashMap<Integer, Horse>();
    private final ReentrantLock r1;
    private final Condition condHorses;
    private int nHorses = 0;
    private final int NO_COMPETITORS;
    private boolean GO = false;
    
    public Stable (int NO_COMPETITORS){
        r1 = new ReentrantLock(true);
        condHorses = r1.newCondition();
        this.NO_COMPETITORS = NO_COMPETITORS;
    }
    
    @Override
    public void proceedToStable(int horseID) {
        out.println("IN proceedToStable");
        r1.lock();
        nHorses++;
        
        //condBroker.signal();
        try{
            while(GO == false){
                condHorses.await();
            }
            nHorses--;
        
            if(nHorses == 0)
                GO = false;
            
        } catch (Exception e) { 
            e.printStackTrace();
        } finally {
            r1.unlock();
            out.println("OUT proceedToStable");
        }
    }
    
    @Override
    public void summonHorsesToPaddock() {
        out.println("IN summonHorsesToPaddock");
        r1.lock();
        
        /*
        try{
            while(nHorses < NO_COMPETITORS){
                condBroker.await();
            }
        } catch (Exception e) { 
            e.printStackTrace();
        }
        */
        
        GO = true;
        condHorses.signalAll();
        r1.unlock();
        out.println("OUT summonHorsesToPaddock");
    }

}
