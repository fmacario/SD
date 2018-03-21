package Monitores;

import Threads.Spectator;
import Threads.Broker;
import Interfaces.IBettingCentre_Broker;
import Interfaces.IBettingCentre_Spectator;
import Enum.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.*;

public class BettingCentre implements IBettingCentre_Spectator, IBettingCentre_Broker{
    private Queue<Spectator> fifoSpectators = new LinkedList<Spectator>();
    
    private GRI gri;
    private final ReentrantLock rl;
    private final Condition condHorses;
    private final Condition condBroker;
    private final Condition condSpectators;
    private int nSpectators = 0;
    
    public BettingCentre(GRI gri){
        this.gri = gri;
        rl = new ReentrantLock();
        condHorses = rl.newCondition();
        condBroker = rl.newCondition();
        condSpectators = rl.newCondition();
    }
    
    @Override
    public void placeABet() {
        rl.lock();
        Spectator spec = ((Spectator)Thread.currentThread());
        System.out.println("placeABet - "+spec.getSpecId());
        
        try{
            try {
                nSpectators++;
                fifoSpectators.add(spec);
                
                spec.setSpecState(SpectatorState.PLACING_A_BET);
                gri.setSpectatorState(spec.getSpecId(), SpectatorState.PLACING_A_BET);
                gri.updateStatus();
                System.out.println("Spectator " + spec.getSpecId() + " " + spec.getSpecState());
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
        
    }
    
    @Override
    public void acceptTheBets() {
        rl.lock();
        
        Broker broker = ((Broker)Thread.currentThread());
        System.out.println("acceptBets");
        
        try{
            try{
                broker.setBroState(BrokerState.WAITING_FOR_BETS);
                gri.setBrokerState(BrokerState.WAITING_FOR_BETS);
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
    public void goCollectTheGains() {

    }

    @Override
    public void honourTheBets() {

    }

}
