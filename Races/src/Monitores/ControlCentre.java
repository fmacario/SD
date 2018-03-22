package Monitores;

import Main.*;
import Threads.Spectator;
import Interfaces.IControlCentre_Broker;
import Interfaces.IControlCentre_Spectator;
import Enum.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ControlCentre implements IControlCentre_Spectator, IControlCentre_Broker{
    private GRI gri;
    private final ReentrantLock rl;
    private final Condition condHorses;
    private final Condition condSpectators;
    private int NO_COMPETITORS;
    private ArrayList<Integer> horsesWinnersList = new ArrayList<>();
    private ArrayList<Integer> specsWinnersList = new ArrayList<>();
    
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
                System.out.println("VOU VER A CORRIDA " + specId); 
                
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
                for ( Integer specsWinner : specsWinnersList ) {
                    if( specId == specsWinner )
                        return true;
                    
                    return false;
                }
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
    public ArrayList<Integer> reportResults( ArrayList<Integer> winnersList ) {
        // temos de acordar o espetador
        
        rl.lock();
        try {
            try {
                this.horsesWinnersList = winnersList;
                condSpectators.signalAll();
                return specsWinnersList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } finally {
            rl.unlock();
        }
    }

    @Override
    public boolean areThereAnyWinners( Map<Integer, List<Integer>> mapSpec_Horse_Bet ) {
        rl.lock();
        System.out.println("areThereAnyWinners");
        try {
            try {
                
                // para cada cavalo vencedor, verifica se há apostadores que ganharam
                for ( Integer horseWinner : horsesWinnersList ) {
                    //System.out.println(winner);
                    
                    for (Map.Entry<Integer, List<Integer>> entry : mapSpec_Horse_Bet.entrySet())
                    {
                        //System.out.println(entry.getKey() + "/" + entry.getValue());
                        if( horseWinner == entry.getValue().get(0) )
                            //System.out.println("SII: " + entry.getKey()); 
                            specsWinnersList.add(entry.getKey());
                    }
                }
                
                if (specsWinnersList.size() == 0)
                    return false;
                
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } finally {
            System.out.println("saí da areThereAnyWinners");
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
