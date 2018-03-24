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
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.*;

public class BettingCentre implements IBettingCentre_Spectator, IBettingCentre_Broker{
    //private Queue<Spectator> fifoSpectators = new LinkedList<Spectator>();
    private Queue<Integer> fifoSpectators = new LinkedList<Integer>();
    
    private Map<Integer, Integer> mapSpec_Money = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> hashHorsesAgile = new HashMap<Integer, Integer>();
    private Map<Integer, List<Integer>> mapSpec_Horse_Bet = new HashMap<Integer, List<Integer>>();
    
    private Map<Integer, Integer> mapSpec_MoneyToReceive = new HashMap<Integer, Integer>();
    private Map<Integer, Boolean> mapSpec_Paid = new HashMap<Integer, Boolean>();
  
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
    private int noSpecWinners = 0;
    
    private int betsHonoured = 0;
    
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
                //System.out.println("sai do while " + spectatorID);
                
                
                int id = fifoSpectators.remove();
                                                
                //gri.setMoney(id, mapSpec_Money.get(id) - mapSpec_Bet.get(id));
                gri.setMoney(id, mapSpec_Money.get(id) - mapSpec_Horse_Bet.get(id).get(1));

                gri.setBetSelection(id, mapSpec_Horse_Bet.get(id).get(0) );
                gri.setBetAmount(id, mapSpec_Horse_Bet.get(id).get(1) );

                gri.updateStatus();
                              
                condBroker.signal();
                nSpectators--;
                if(nSpectators==0){
                    wantToBet=false;
                }
                //condSpectators.signal();
                return mapSpec_Horse_Bet.get(id).get(1);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        } finally {
            rl.unlock();
        }
        
    }
    
    @Override
    public Map<Integer, List<Integer>> acceptTheBets( Map<Integer, Integer> hashHorsesAgile ) {
        rl.lock();
        
        System.out.println("acceptBets");
        this.hashHorsesAgile = hashHorsesAgile;
        System.out.println(hashHorsesAgile);
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
                                                          
                        List<Integer> list = new ArrayList<Integer>();
                        list.add(0, horse);
                        list.add(1, bet);
                        mapSpec_Horse_Bet.put(id, list);
                                                                        
                        condSpectators.signal();
                    }
                    
                    condBroker.await();
                }
                
                return mapSpec_Horse_Bet;
                
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } finally {
            rl.unlock();
        }
    }

    @Override
    public int goCollectTheGains( int spectatorID ) {
        rl.lock();
        System.out.println("goCollectTheGains - "+ spectatorID);
        try {
            try {
                gri.setSpectatorState(spectatorID, SpectatorState.COLLECTING_THE_GAINS);
                gri.updateStatus();
                
                fifoSpectators.add(spectatorID);
                condBroker.signal();
                
                //System.out.println("MAP PAID - " + mapSpec_Paid);
                
                while( mapSpec_Paid.get(spectatorID) == false ){
                    condSpectators.await();
                }
                
                //System.out.println("MAP PAID AFTER - " + mapSpec_Paid);
                
                //System.out.println("I WAS PAID - " + spectatorID);
                fifoSpectators.remove();
                condBroker.signal();
                //gri.setMoney(spectatorID, spectatorID);
                
                gri.addMoney(spectatorID, mapSpec_MoneyToReceive.get(spectatorID));
                gri.updateStatus();
                return mapSpec_MoneyToReceive.get(spectatorID);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        } finally {
            rl.unlock();
        }
    }

    @Override
    public void honourTheBets( ArrayList<Integer> horsesWinnersList, ArrayList<Integer> specsWinnersList ) {
        rl.lock();
        System.out.println("honourTheBets");
        try {
            try {
                gri.setBrokerState(BrokerState.SETTLING_ACCOUNTS);
                gri.updateStatus();
                
                // calcular valor a distribuir para cada vencedor
                //noSpecWinners = winnersList.size();
                System.out.println(horsesWinnersList);
                //mapSpec_Horse_Bet
                //mapSpec_MoneyToReceive
                for ( Integer winner :  specsWinnersList) {
                    int id_cavalo = mapSpec_Horse_Bet.get(winner).get(0);
                    int aposta = mapSpec_Horse_Bet.get(winner).get(1);
                    double odd = (double)hashHorsesAgile.get(id_cavalo) / 100;
                    //System.out.println("APOSTA: " + aposta + ", ODD: " + odd);
                    mapSpec_MoneyToReceive.put(winner, (int)Math.round(aposta * (1/odd)));
                    mapSpec_Paid.put(winner, false);
                }
                
                System.out.println(mapSpec_MoneyToReceive);
                
                 
                while( betsHonoured != mapSpec_Paid.size() ){
                    
                    if( !fifoSpectators.isEmpty() ){
                        mapSpec_Paid.put(fifoSpectators.peek(), true);
                        betsHonoured++;
                        condSpectators.signalAll();
                    }
                    condBroker.await();
                }
                
                System.out.println("ALL PAID");
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
    }

}
