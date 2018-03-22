package Monitores;

import Main.*;
import Threads.Spectator;
import Interfaces.IControlCentre_Broker;
import Interfaces.IControlCentre_Spectator;
import Enum.*;
import java.util.ArrayList;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ControlCentre implements IControlCentre_Spectator, IControlCentre_Broker{
    private GRI gri;
    private final ReentrantLock rl;
    private final Condition condHorses;
    private final Condition condSpectators;
    private int NO_COMPETITORS;
    private ArrayList<Integer> winnersList = new ArrayList<>();
    
    public ControlCentre( GRI gri){
        this.gri = gri;
        this.NO_COMPETITORS = Main.NO_COMPETITORS;
        rl = new ReentrantLock(true);
        condHorses = rl.newCondition();
        condSpectators = rl.newCondition();
    }
    
    /*@Override
    public void waitForNextRace() {
        rl.lock();
        
        try {
            try{
                //while (  ) {
                    
                //}
                Spectator.state = SpectatorState.WAITING_FOR_A_RACE_TO_START;
            } catch (Exception e) { 
                e.printStackTrace();
            }
        }finally{
            rl.unlock();
        }
    }*/

    @Override
    public void goWatchTheRace(int specId) {
        rl.lock();
        
        try {
            try {
                gri.setSpectatorState(specId, SpectatorState.WATCHING_A_RACE);
                gri.updateStatus();
                if( haveIWon(specId) == false ){
                    condSpectators.await();
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
    }

    @Override
    public boolean haveIWon( int specId ) {
        rl.lock();
        try {
            try {
                
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } finally {
            rl.unlock();
        }
    }

    @Override
    public void relaxABit(int specId) {
        rl.lock();
        try {
            try {
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
    }

    @Override
    public void reportResults( ArrayList<Integer> winnersList ) {
        // temos de acordar o espetador
        
        rl.lock();
        try {
            try {
                this.winnersList = winnersList;
                condSpectators.signalAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
    }

    @Override
    public boolean areThereAnyWinners( ArrayList<Integer> winnersList ) {
        rl.lock();
        try {
            try {
                
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } finally {
            rl.unlock();
        }
    }

    @Override
    public void entertainTheGuests() {
        rl.lock();
        try {
            try {
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
    }

}
