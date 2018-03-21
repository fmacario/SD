package Monitores;

import Main.Main;
import Threads.Broker;
import Threads.Horse;
import Interfaces.IStable_Horse;
import Interfaces.IStable_Broker;
import Enum.*;

import java.util.*;
import static java.lang.System.*;
import java.util.concurrent.locks.*;
        
public class Stable implements IStable_Broker, IStable_Horse{
    private Map<Integer, Horse> hashHorses = new HashMap<Integer, Horse>();
    
    private GRI gri;
    private final ReentrantLock rl;
    private final Condition condHorses;
    private final Condition condBroker;
    private int nHorses = 0;
    private final int NO_COMPETITORS = Main.NO_COMPETITORS;
    private boolean GO = false;
    
    public Stable (GRI gri){
        this.gri = gri;
        rl = new ReentrantLock(true);
        condHorses = rl.newCondition();
        condBroker = rl.newCondition();
    }
    
    @Override
    public void proceedToStable(int horseID) {
        rl.lock();
        //System.out.println("proceedStable - "+horseID);
        try{
            try{
                nHorses++;
                Horse.state = HorseState.AT_THE_STABLE;
                System.out.println("Horse " + horseID + " " + Horse.state);
                condBroker.signal();
                
                while(GO == false){
                    condHorses.await();
                }
               
                nHorses--;

                if(nHorses == 0)
                    GO = false;
                
                
            } catch (Exception e) { 
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
    }
    
    @Override
    public void summonHorsesToPaddock() {
        rl.lock();
        //System.out.println("summonHorses");
        try {
            try{
                Broker.state = BrokerState.ANNOUNCING_NEXT_RACE;
                System.out.println("Broker " + Broker.state);
                
                while(nHorses != NO_COMPETITORS){
                    condBroker.await();
                }
                
                GO = true;
                
                condHorses.signalAll();
                
            } catch (Exception e) { 
                e.printStackTrace();
            }
        }finally{
            rl.unlock();
        }
    }

}
