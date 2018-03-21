package Monitores;

import Main.Main;
import Threads.Horse;
import Interfaces.IRacingTrack_Broker;
import Interfaces.IRacingTrack_Horse;
import Enum.*;
import Threads.Broker;

import java.util.*;
import java.util.concurrent.locks.*;

public class RacingTrack implements IRacingTrack_Broker, IRacingTrack_Horse{
    private Map<Integer, Horse> hashHorses = new HashMap<Integer, Horse>();

    private GRI gri;
    private final ReentrantLock rl;
    private final Condition condHorses;
    private final Condition condBroker;
    private final Condition condSpectators;
    
    private int nHorses = 0;
    private final int NO_COMPETITORS = Main.NO_COMPETITORS;
    
    public RacingTrack(GRI gri){
        this.gri = gri;
        rl = new ReentrantLock();
        condHorses = rl.newCondition();
        condBroker = rl.newCondition();
        condSpectators = rl.newCondition();
    }
    
    @Override
    public void startTheRace() {
        rl.lock();
        
        Broker broker = ((Broker)Thread.currentThread());
        System.out.println("startTheRace");
        
        try {
            try {
                broker.setBroState(BrokerState.SUPERVISING_THE_RACE);
                gri.setBrokerState(BrokerState.SUPERVISING_THE_RACE);
                gri.updateStatus();
                System.out.println("Broker " + broker.getBroState());
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
    }

    @Override
    public void proceedToStartLine(int horseID) {
        rl.lock();
        //System.out.println("proceedStartLine - "+horseID);
        try{
            try {
                nHorses++;
                
                Horse.state = HorseState.AT_THE_START_LINE;
                gri.setHorseState(horseID, HorseState.AT_THE_START_LINE);
                gri.updateStatus();
                System.out.println("Horse " + horseID + " " + Horse.state);
                
                if(nHorses == NO_COMPETITORS){
                    condSpectators.signalAll();
                }
                
                /*
                while( waiting for broker to start  race ){
                    condHorses.await();
                }
                */
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }finally {
            rl.unlock();
        }
    }

    @Override
    public boolean hasFinishLineBeenCrossed() {
        return false;
    }

    @Override
    public void makeAMove() {

    }

}
