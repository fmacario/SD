package Monitores;

import Threads.Spectator;
import Threads.Broker;
import Interfaces.IBettingCentre_Broker;
import Interfaces.IBettingCentre_Spectator;
import Enum.*;
import Main.*;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.*;

public class BettingCentre implements IBettingCentre_Spectator, IBettingCentre_Broker{
    //private Queue<Spectator> fifoSpectators = new LinkedList<Spectator>();
    private Queue<Integer> fifoSpectators = new LinkedList<Integer>();
    
    private Map<Integer, Integer> mapSpec_Money = new HashMap<Integer, Integer>();
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
    public int placeABet( int spectatorID, int money) {
        rl.lock();
        //Spectator spec = ((Spectator)Thread.currentThread());
        System.out.println("placeABet - "+ spectatorID);
        
        try{
            try {
                nSpectators++;
                fifoSpectators.add( spectatorID );
                mapSpec_Money.put( spectatorID, money );
                
                gri.setSpectatorState( spectatorID, SpectatorState.PLACING_A_BET);
                gri.updateStatus();
                System.out.println("Spectator " + spectatorID + " " + SpectatorState.PLACING_A_BET);
                
                wantToBet = true;
                condBroker.signal();
                
                while ( betSpec[spectatorID] == false ){
                    condSpectators.await();
                }
                System.out.println("sai do while " + spectatorID);
                
                
                int id = fifoSpectators.remove();
                                
                gri.setMoney(id, mapSpec_Money.get(id) - mapSpec_Bet.get(id));
                //gri.updateStatus();

                gri.setBetSelection(id, mapSpec_Horse.get(id) );
                gri.setBetAmount(id, mapSpec_Bet.get(id) );

                gri.updateStatus();
                              
                condBroker.signal();
                //condSpectators.signal();
                return mapSpec_Bet.get(id);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        } finally {
            rl.unlock();
        }
        
    }
    
    @Override
    public void acceptTheBets() {
        rl.lock();
        
        System.out.println("acceptBets");
        
        int bets = 0;
        
        try{
            try{
                gri.setBrokerState(BrokerState.WAITING_FOR_BETS);
                gri.updateStatus();
                System.out.println("Broker " + BrokerState.WAITING_FOR_BETS );
                
                while (bets != NO_SPECTATORS){
                    //System.out.println("betStatus -- "+ betStatus[fifoSpectators.peek().getSpecId()]);
                    if( wantToBet){ //betStatus[fifoSpectators.peek().getSpecId()] == true ){
                        
                        int id = fifoSpectators.peek();
                        
                        System.out.println("WantToBet - " + id);
                        bets++;
                        betSpec[id] = true;
                        
                        int horse = (int)(Math.random() * NO_COMPETITORS); 
                        int bet;
                        
                        if ( mapSpec_Money.get(id) == 0 )
                            bet = 0;
                        else if ( mapSpec_Money.get(id) <= MAX_BET ) {
                            bet = (int)(Math.random() * mapSpec_Money.get(id)); 
                        }else{
                            bet = (int)(Math.random() * MAX_BET); 
                        }
                                                                        
                        mapSpec_Horse.put(id, horse);
                        mapSpec_Bet.put(id, bet);
                                                
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
    public int goCollectTheGains( int spectatorID ) {
        return 0;
    }

    @Override
    public void honourTheBets( ArrayList<Integer> winnersList ) {

    }

}
