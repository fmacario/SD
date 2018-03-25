package Monitores;

import Main.Main;
import Threads.Horse;
import Threads.Spectator;
import Interfaces.IPaddock_Spectator;
import Interfaces.IPaddock_Horse;
import Enum.*;
import Interfaces.IPaddock_Broker;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Paddock implements IPaddock_Horse, IPaddock_Spectator, IPaddock_Broker{
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
    
    private boolean allHorses = false;
    
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

    @Override
    public void goCheckHorses(int spectatorID) {
        rl.lock();
        System.out.println("checkHorses - "+spectatorID);
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
