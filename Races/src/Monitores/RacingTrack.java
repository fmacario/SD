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
    boolean raceFinished = false;
    boolean raceStart = false;
    private final int NO_COMPETITORS = Main.NO_COMPETITORS;
    private int[] positions = new int[NO_COMPETITORS];
    
    private int horsesFinished=0;
    
    public RacingTrack(GRI gri){
        this.gri = gri;
         
        for(int i=0; i < NO_COMPETITORS; i++){
            positions[i] = 0;
        }
        
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
                
                raceStart = true;
                condHorses.signalAll();
                
                while( raceFinished == false ){
                    condBroker.await();
                }
                               
                
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
        
        Horse horse = ((Horse)Thread.currentThread());
        System.out.println("proceed to start line");
        
        try{
            try {
                nHorses++;
                
                horse.setHorseState(HorseState.AT_THE_START_LINE);
                gri.setHorseState(horse.getHorseId(), HorseState.AT_THE_START_LINE);
                gri.updateStatus();
                System.out.println("Horse "+ horse.getHorseId() +" "+  horse.getHorseState());
                
                if(nHorses == NO_COMPETITORS){
                    condSpectators.signalAll();
                }
                
                while( raceStart == false ){
                    condHorses.await();
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }finally {
            rl.unlock();
        }
    }
    
    @Override
    public void makeAMove(int Pnk) {
        rl.lock();
        
        Horse horse = ((Horse)Thread.currentThread());
        System.out.println("makeAMove");
        
        try {
            try {
                horse.setHorseState(HorseState.RUNNING);
                gri.setHorseState(horse.getHorseId(), HorseState.RUNNING);
                gri.updateStatus();
                System.out.println("Horse " + horse.getHorseId() +" "+ horse.getHorseState() +" "+ positions[horse.getHorseId()]);
                
                condHorses.signalAll();
                if( positions[horse.getHorseId()] < Main.TRACK_DISTANCE){
                    positions[horse.getHorseId()] += (int )(Math.random() * Pnk + 1);
                    System.out.println("position inc - " + positions[horse.getHorseId()]);
                    
                    condHorses.await();
                }
                
                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
    }

    @Override
    public boolean hasFinishLineBeenCrossed() {
        rl.lock();
        
        Horse horse = ((Horse)Thread.currentThread());
        System.out.println("finish line check" + horse.getHorseId());
            
        try{
            try {
                if( positions[horse.getHorseId()] >= Main.TRACK_DISTANCE){
                    horsesFinished++;
                    if(horsesFinished == NO_COMPETITORS){
                        raceFinished = true;
                        System.out.println("ALL FINISHED");
                        condBroker.signal();
                    }
                    return true;
                }
                
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } finally{        
            rl.unlock();
        }
    }

}
