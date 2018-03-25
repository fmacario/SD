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
    private Map<Integer, Integer> hashHorsesAgile = new HashMap<Integer, Integer>();
    
    private GRI gri;
    private final ReentrantLock rl;
    private final Condition condHorses;
    private final Condition condBroker;
    private int nHorses = 0;
    private final int NO_COMPETITORS = Main.NO_COMPETITORS;
    private boolean GO = false;
    private boolean allHorses = false;
    
    public Stable (GRI gri){
        this.gri = gri;
        rl = new ReentrantLock(true);
        condHorses = rl.newCondition();
        condBroker = rl.newCondition();
    }
    
    @Override
    public void proceedToStable(int horseID, int agile) {
        rl.lock();
        try{
            try{
                nHorses++;
                
                hashHorsesAgile.put( horseID, agile );
                
                gri.setHorseState(horseID, HorseState.AT_THE_STABLE);
                gri.updateStatus();
                
                if(nHorses == NO_COMPETITORS){
                    allHorses=true;
                    condBroker.signal();
                }
                
                while(GO == false){
                    condHorses.await();
                }
               
                nHorses--;

                if(nHorses == 0){
                    GO = false;
                    allHorses=false;
                }
                
            } catch (Exception e) { 
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
    }
    
    @Override
    public Map<Integer, Integer> summonHorsesToPaddock( int nRace ) {
        rl.lock();
        try {
            try{
                gri.setBrokerState(BrokerState.ANNOUNCING_NEXT_RACE);
                gri.setRn( nRace );
                gri.updateStatus();
                
                while(!allHorses){
                    condBroker.await();
                }
                
                GO = true;
                
                int totalAgile = 0;
                
                for (Map.Entry<Integer, Integer> entry : hashHorsesAgile.entrySet())
                {
                    totalAgile += entry.getValue();
                }
                
                for (int i = 0; i < NO_COMPETITORS; i++) {
                    int odd = hashHorsesAgile.get(i)*100 / totalAgile;
                    double o = (double)odd/100;
                    gri.setHorseMaxDistance(i, hashHorsesAgile.get(i));
                    gri.setHorseWinningProb(i, o);
                    hashHorsesAgile.put(i, odd);
                }
                                
                condHorses.signalAll();
                
                return hashHorsesAgile;
                
            } catch (Exception e) { 
                e.printStackTrace();
                return null;
            }
        }finally{
            rl.unlock();
        }
    }

}
