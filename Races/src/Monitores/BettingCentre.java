package Monitores;

import Threads.Spectator;
import Threads.Broker;
import Interfaces.IBettingCentre_Broker;
import Interfaces.IBettingCentre_Spectator;
import Enum.*;
import Main.*;
import java.util.HashMap;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.*;

public class BettingCentre implements IBettingCentre_Spectator, IBettingCentre_Broker{
    private Queue<Spectator> fifoSpectators = new LinkedList<Spectator>();
    
    private Map<Integer, Integer> mapSpec_Horse = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> mapSpec_Bet = new HashMap<Integer, Integer>();
    
    private GRI gri;
    private final ReentrantLock rl;
    private final Condition condHorses;
    private final Condition condBroker;
    private final Condition condSpectators;
    private int nSpectators = 0;
    private int NO_SPECTATORS = Main.NO_SPECTATORS;
    private int NO_COMPETITORS = Main.NO_COMPETITORS; 
    private double MAX_BET = Main.MAX_BET;
    private boolean betDone = false;
    private boolean wantToBet = false;
    private boolean[] betSpec;
    
    public BettingCentre(GRI gri){
        this.gri = gri;
        
        betSpec = new boolean[NO_SPECTATORS];
        for(int i=0; i< NO_SPECTATORS; i++){
                betSpec[i] = false;
        }
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
                
                wantToBet = true;
                condBroker.signal();
                
                while (betSpec[spec.getSpecId()] == false){
                    condSpectators.await();
                }
                System.out.println("sai do while " + spec.getSpecId());
                
                fifoSpectators.remove();
                if (fifoSpectators.size() == 0){
                    int index = 0;
                    
                    int bets_value[] = new int[NO_COMPETITORS];
                    int bets_horses[] = new int[NO_COMPETITORS];
                    
                    for (Map.Entry<Integer, Integer> mapEntry : mapSpec_Horse.entrySet()) {
                        bets_horses[index] = mapEntry.getValue();
                        index++;
                    }
                    index = 0;
                    for (Map.Entry<Integer, Integer> mapEntry : mapSpec_Bet.entrySet()) {
                        bets_value[index] = mapEntry.getValue();
                        index++;
                    }
                    
                    gri.setBetSelection( bets_horses );
                    gri.setBetAmount( bets_value );
                    
                    gri.updateStatus();
                }
                
                condBroker.signal();
                //condSpectators.signal();
                
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
        
        int bets = 0;
        
        try{
            try{
                broker.setBroState(BrokerState.WAITING_FOR_BETS);
                gri.setBrokerState(BrokerState.WAITING_FOR_BETS);
                gri.updateStatus();
                System.out.println("Broker " + broker.getBroState());
                
                while (bets != NO_SPECTATORS){
                    //System.out.println("betStatus -- "+ betStatus[fifoSpectators.peek().getSpecId()]);
                    if( wantToBet){ //betStatus[fifoSpectators.peek().getSpecId()] == true ){
                        
                        Spectator s = fifoSpectators.peek();
                        int id = s.getSpecId();
                        System.out.println("WantToBet - " + id);
                        bets++;
                        betSpec[id] = true;
                        
                        int horse = (int)(Math.random() * NO_COMPETITORS); 
                        
                        int bet;
                        
                        if ( s.getMoney() == 0 )
                            bet = 0;
                        else if ( s.getMoney() <= MAX_BET ) {
                            bet = (int)(Math.random() * s.getMoney()); 
                        }else{
                            bet = (int)(Math.random() * MAX_BET); 
                        }
                        
                        s.setMoney( -bet );
                        gri.setMoney(id, s.getMoney());
                        gri.updateStatus();
                                                
                        mapSpec_Horse.put(id, horse);
                        mapSpec_Bet.put(id, bet);
                        
                        System.out.println(mapSpec_Horse);
                        System.out.println(mapSpec_Bet);
                        
                        condSpectators.signal();
                    }
                    
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
    public void goCollectTheGains() {

    }

    @Override
    public void honourTheBets() {

    }

}
