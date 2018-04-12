package Monitores;

import Threads.Horse;
import Threads.Spectator;
import Interfaces.IPaddock_Spectator;
import Interfaces.IPaddock_Horse;
import Enum.*;
import Interfaces.IPaddock_Broker;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Métodos do Paddock.
 * @author fm
 */
public class Paddock implements IPaddock_Horse, IPaddock_Spectator, IPaddock_Broker{    
    private GRI gri;
    private final Condition condSpectators;
    private final Condition condHorses;
    private final Condition condBroker;
    private final ReentrantLock rl;
    
    private final int NO_SPECTATORS;
    private final int NO_COMPETITORS;
    
    private Map<Integer, Horse> hashHorses = new HashMap<Integer, Horse>();
    private Map<Integer, Spectator> hashSpectators = new HashMap<Integer, Spectator>();
    
    private int nSpectators = 0;
    private int nHorses = 0;
    private static boolean GO = false;
    private boolean allHorses = false;
    private boolean lastHorse = false;
    
    /**
     * 
     * @param gri General Repository of Information (GRI).
     */
    public Paddock(GRI gri, int NO_COMPETITORS, int NO_SPECTATORS){
        this.gri = gri;
        this.NO_COMPETITORS = NO_COMPETITORS;
        this.NO_SPECTATORS = NO_SPECTATORS;
               
        rl = new ReentrantLock(true);
        condSpectators = rl.newCondition();
        condHorses = rl.newCondition();
        condBroker = rl.newCondition();
    }
    
    /**
     * <b>Cavalo</b> vai para o Paddock.
     * @param horseID Id do cavalo.
     */
    @Override
    public void proceedToPaddock(int horseID) {
        rl.lock();
        try{
            try{
                nHorses++;
                gri.setHorseState(horseID, HorseState.AT_THE_PADDOCK);
                gri.updateStatus();
                
                if(nHorses == NO_COMPETITORS){
                    allHorses = true;
                    condSpectators.signalAll();
                }
                
                while( GO == false ){
                    condHorses.await();
                }                
                nHorses--;
                if(nHorses == 0){
                    lastHorse = true;
                    allHorses= false;
                    condSpectators.signalAll();
                }
                
            }catch( Exception e ){
                e.printStackTrace();
            }
        }finally{
            rl.unlock();
        }
    }

    /**
     * <b>Espetador</b> vai analisar os cavalos.
     * @param spectatorID Id do espetador.
     */
    @Override
    public void goCheckHorses(int spectatorID) {
        rl.lock();
        try{
            try{
                nSpectators++;
                
                gri.setSpectatorState(spectatorID, SpectatorState.APPRAISING_THE_HORSES);
                gri.updateStatus();
                
                if(nSpectators == NO_SPECTATORS){
                    GO = true;
                    condHorses.signalAll();
                    condBroker.signal();
                }
                while( !lastHorse ){
                    condSpectators.await();
                }
                
                nSpectators--;
                if(nSpectators==0){
                    GO = false;
                    lastHorse=false;
                }
            }catch( Exception e ){
                e.printStackTrace();
            }
        }finally{
            rl.unlock();
        }
    }
    
    /**
     * <b>Espetador</b> espera pela próxima corrida.
     * @param spectatorID Id do espetador.
     */
    @Override
    public void waitForNextRace(int spectatorID){
        rl.lock();
        
        try {
            try{
                gri.setSpectatorState(spectatorID, SpectatorState.WAITING_FOR_A_RACE_TO_START);
                gri.updateStatus();
                
                while ( !allHorses ) {
                    condSpectators.await();
                }
                
            } catch (Exception e) { 
                e.printStackTrace();
            }
        }finally{
            rl.unlock();
        }
    }
    
    /**
     * <b>Broker</b> espera que os espetadores cheguem ao Paddock.
     * @return true - quando todos os espetadores chegam ao Paddock.
     */
    public boolean waitForSpectators(){
        rl.lock();
        
        try {
            try {
                while( GO == false ){
                    condBroker.await();
                }
                return true;
                
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        } finally {
            rl.unlock();
        }
    }

}
