import java.util.*;
import static java.lang.System.*;
import java.util.concurrent.locks.*;
        
public class Stable implements IStable_Broker, IStable_Horse{
    private Map<Integer, Horse> hashHorses = new HashMap<Integer, Horse>();
    private final ReentrantLock rl;
    private final Condition condHorses;
    private final Condition condBroker;
    private int nHorses = 0;
    private final int NO_COMPETITORS = Main.NO_COMPETITORS;
    private boolean GO = false;
    
    public Stable (){
        rl = new ReentrantLock(true);
        condHorses = rl.newCondition();
        condBroker = rl.newCondition();
    }
    
    @Override
    public void proceedToStable(int horseID) {
        //out.println("IN proceedToStable " + horseID);
        rl.lock();
        //condBroker.signal();
        try{
            try{
                nHorses++;
                
                condBroker.signal();
                
                while(GO == false){
                    condHorses.await();
                }
                nHorses--;

                if(nHorses == 0)
                    GO = false;
                Horse.state = HorseState.AT_THE_STABLE;

            } catch (Exception e) { 
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
            //out.println("OUT proceedToStable " + horseID);
        }
    }
    
    @Override
    public void summonHorsesToPaddock() {
        //out.println("IN summonHorsesToPaddock");
        rl.lock();
        
        try {
            try{
                while(nHorses != NO_COMPETITORS){
                    condBroker.await();
                }
            } catch (Exception e) { 
                e.printStackTrace();
            }

            GO = true;
            Broker.state = BrokerState.ANNOUNCING_NEXT_RACE;
            condHorses.signalAll();
        }finally{
            rl.unlock();
        }
        //out.println("OUT summonHorsesToPaddock");
    }

}
