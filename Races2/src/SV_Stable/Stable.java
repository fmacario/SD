package SV_Stable;

//import Enum.*;

import java.util.*;
import java.util.concurrent.locks.*;
        
/**
 * Métodos do Estábulo.
 * @author fm
 */
public class Stable implements IStable_Broker, IStable_Horse{    
    //private GRI gri;
    private final ReentrantLock rl;
    private final Condition condHorses;
    private final Condition condBroker;
    
    private final int NO_COMPETITORS;
    
    private Map<Integer, Integer> hashHorsesAgile = new HashMap<Integer, Integer>();
    
    private int nHorses = 0;
    private boolean GO = false;
    private boolean allHorses = false;
    private boolean end = false;
    
    /**
     * 
     * @param gri General Repository of Information (GRI).
     */
    /*
    public Stable (GRI gri, int NO_COMPETITORS){
        this.gri = gri;
        this.NO_COMPETITORS = NO_COMPETITORS;
        
        rl = new ReentrantLock(true);
        condHorses = rl.newCondition();
        condBroker = rl.newCondition();
    }
    */
    public Stable (int NO_COMPETITORS){
        this.NO_COMPETITORS = NO_COMPETITORS;
        
        rl = new ReentrantLock(true);
        condHorses = rl.newCondition();
        condBroker = rl.newCondition();
    }
    
    /**
     * <b>Cavalo</b> vai para o estábulo.
     * @param horseID Id do cavalo.
     * @param agile Agilidade do cavalo.
     */
    @Override
    public void proceedToStable(int horseID, int agile) {
        rl.lock();
        try{
            try{
                nHorses++;
                
                hashHorsesAgile.put( horseID, agile );
                
                //gri.setHorseState(horseID, HorseState.AT_THE_STABLE);
                //gri.updateStatus();
                System.out.println("Horse " + horseID + " at the stable.");
                
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
                   System.out.println("HORSE " +horseID+ " : saí");
            } catch (Exception e) { 
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
    }
    
    /**
     * <b>Broker</b> anuncia a corrida e acorda os cavalos para irem para o Paddock.
     * @param nRace Número da corrida.
     * @return Mapa com id dos cavalos e agilidade de cada um.
     */
    @Override
    public Map<Integer, Integer> summonHorsesToPaddock( int nRace ) {
        rl.lock();
        try {
            try{
                //gri.setBrokerState(BrokerState.ANNOUNCING_NEXT_RACE);
                //gri.setRn( nRace );
                //gri.updateStatus();
                System.out.println("BrokerState.ANNOUNCING_NEXT_RACE");
                
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
                    //gri.setHorseMaxDistance(i, hashHorsesAgile.get(i));
                    //gri.setHorseWinningProb(i, o);
                    hashHorsesAgile.put(i, odd);
                }
                                
                condHorses.signalAll();
                   System.out.println("BROKER  saí");
                return hashHorsesAgile;
                
            } catch (Exception e) { 
                e.printStackTrace();
                return null;
            }
        }finally{
            rl.unlock();
        }
    }

    /**
     * <b>Broker</b> termina o evento.
     */
    @Override
    public void end() {
        rl.lock();
        try {
            try {
                condHorses.signalAll();
                GO = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
    }

}
