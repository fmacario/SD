package SV_BettingCentre;

import Enum.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.*;
import org.json.JSONObject;

/**
 * Métodos do Betting Centre.
 * @author fm
 */
public class BettingCentre implements IBettingCentre_Spectator, IBettingCentre_Broker{  
    //private GRI gri;
    private final ReentrantLock rl;
    private final Condition condBroker;
    private final Condition condSpectators;
    
    private final int NO_SPECTATORS;
    private final int NO_COMPETITORS; 
    private final double MAX_BET;
    
    private Queue<Integer> fifoSpectators = new LinkedList<Integer>();
    private Map<Integer, Integer> mapSpec_Money = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> hashHorsesAgile = new HashMap<Integer, Integer>();
    private Map<Integer, List<Integer>> mapSpec_Horse_Bet = new HashMap<Integer, List<Integer>>();
    private Map<Integer, Integer> mapSpec_MoneyToReceive = new HashMap<Integer, Integer>();
    private Map<Integer, Boolean> mapSpec_Paid = new HashMap<Integer, Boolean>();
    
    private int nSpectators = 0;
    private int betsHonoured = 0;
    private boolean betDone = false;
    private boolean wantToBet = false;
    private boolean[] betSpec;    
    private boolean canGO = false;
    
    private String IP_GRI;
    private int PORT_GRI;
    
    /**
     * 
     * @param gri General Repository of Information (GRI).
     */
    public BettingCentre( int NO_SPECTATORS, int NO_COMPETITORS, double MAX_BET, String IP_GRI, int PORT_GRI){
        this.NO_SPECTATORS = NO_SPECTATORS;
        this.NO_COMPETITORS = NO_COMPETITORS; 
        this.MAX_BET = MAX_BET;
        this.IP_GRI = IP_GRI;
        this.PORT_GRI = PORT_GRI;
        
        betSpec = new boolean[NO_SPECTATORS];
        for(int i=0; i< NO_SPECTATORS; i++){
                betSpec[i] = false;
        }
        rl = new ReentrantLock();
        condBroker = rl.newCondition();
        condSpectators = rl.newCondition();
    }
    
    /**
     * <b>Espetador</b> realiza uma aposta.
     * @param spectatorID Id do espetador.
     * @param money Dinheiro do espetador.
     * @return Retorna o valor da aposta realizada.
     */
    @Override
    public int placeABet( int spectatorID, int money) {
        rl.lock();
        
        try{
            try {
                nSpectators++;
                
                fifoSpectators.add( spectatorID );
                mapSpec_Money.put( spectatorID, money );
                
                //gri.setSpectatorState( spectatorID, SpectatorState.PLACING_A_BET);
                //gri.updateStatus();
                System.out.println("Spectator "+ spectatorID + " PLACING_A_BET");
                
                wantToBet = true;
                condBroker.signal();
                
                while ( betSpec[spectatorID] == false ){
                    condSpectators.await();
                }
                
                int id = fifoSpectators.remove();
                                                
                //gri.setMoney(id, mapSpec_Money.get(id) - mapSpec_Horse_Bet.get(id).get(1));

                //gri.setBetSelection(id, mapSpec_Horse_Bet.get(id).get(0) );
                //gri.setBetAmount(id, mapSpec_Horse_Bet.get(id).get(1) );

                //gri.updateStatus();
                --nSpectators;  
                condBroker.signal();
                
                if(nSpectators==0){
                    for(int i=0; i< NO_SPECTATORS; i++){
                        betSpec[i] = false;
                    }
                    wantToBet=false;
                }
                
                return mapSpec_Horse_Bet.get(id).get(1);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        } finally {
            rl.unlock();
        }
        
    }
    
    /**
     * <b>Broker</b> aceita as apostas.
     * @param hashHorsesAgile Mapa com o id dos cavalos e a sua agilidade.
     * @return Retorna um mapa com o id do espetador e uma lista contendo o id do cavalo e a aposta realziada.
     */
    @Override
    public Map<Integer, List<Integer>> acceptTheBets( Map<Integer, Integer> hashHorsesAgile ) {
        rl.lock();
        try{
            try{
                this.hashHorsesAgile = hashHorsesAgile;
                int bets = 0;
                
                //gri.setBrokerState(BrokerState.WAITING_FOR_BETS);
                //gri.updateStatus();
                System.out.println("BrokerState.WAITING_FOR_BETS");
                
                while (bets != NO_SPECTATORS){
                    if( wantToBet ){ 
                        int id = fifoSpectators.peek();
                                                
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

    /**
     * <b>Espetador</b> vai receber os lucros da aposta.
     * @param spectatorID Id do espetador.
     * @return Dinheiro a receber por parte do espetador.
     */
    @Override
    public int goCollectTheGains( int spectatorID ) {
        rl.lock();
        try {
            try {
                //gri.setSpectatorState(spectatorID, SpectatorState.COLLECTING_THE_GAINS);
                //gri.updateStatus();
                System.out.println("Spectator "+ spectatorID + " COLLECTING_THE_GAINS");
                
                fifoSpectators.add(spectatorID);
                condBroker.signal();
                                System.out.println("mapSpec_Paid " + mapSpec_Paid.toString());
                while( !canGO )
                    condSpectators.await();
                System.out.println("mapSpec_Paid : "+ mapSpec_Paid.toString());
                while( mapSpec_Paid.get(spectatorID) == false){
                    condSpectators.await();
                }
                                
                fifoSpectators.remove();
                condBroker.signal();
                
                //gri.addMoney(spectatorID, mapSpec_MoneyToReceive.get(spectatorID));
                //gri.updateStatus();
                
                return mapSpec_MoneyToReceive.get(spectatorID);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        } finally {
            rl.unlock();
        }
    }

    /**
     * <b>Broker</b> liquida as contas.
     * @param horsesWinnersList ArrayList com os id dos cavalos vencedores da corrida.
     * @param specsWinnersList ArrayList com o id os espetadores que ganharam a aposta.
     */
    @Override
    public void honourTheBets( ArrayList<Integer> horsesWinnersList, ArrayList<Integer> specsWinnersList ) {
        rl.lock();
        try {
            try {
                mapSpec_Paid.clear();
                //gri.setBrokerState(BrokerState.SETTLING_ACCOUNTS);
                //gri.updateStatus();
                System.out.println("BrokerState.SETTLING_ACCOUNTS");
                
                // calcular valor a distribuir para cada vencedor
                for ( Integer winner :  specsWinnersList) {
                    int id_cavalo = mapSpec_Horse_Bet.get(winner).get(0);
                    int aposta = mapSpec_Horse_Bet.get(winner).get(1);
                    double odd = (double)hashHorsesAgile.get(id_cavalo) / 100;
                    mapSpec_MoneyToReceive.put(winner, (int)Math.round(aposta * (1/odd)));
                    mapSpec_Paid.put(winner, false);
                    System.out.println("mapSpec_Paid PUTTTT : " + winner);
                }
                
                canGO = true;
                
                while( betsHonoured != mapSpec_Paid.size() ){
                    
                    if( !fifoSpectators.isEmpty() ){
                        mapSpec_Paid.put(fifoSpectators.peek(), true);
                        betsHonoured++;
                        condSpectators.signalAll();
                    }
                    condBroker.await();
                }
                
                for (int i = 0; i < NO_SPECTATORS; i++) {
                    //gri.setBetAmount( i, -1 );
                    //gri.setBetSelection( i , -1 );
                }
                betsHonoured = 0;
                canGO = false;
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            rl.unlock();
        }
    }
    
    private void sendMessage(JSONObject json) throws IOException {
        Socket socket = new Socket( IP_GRI, PORT_GRI );
        OutputStream out = socket.getOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(out);
        
        o.writeObject( json.toString() );
        out.flush();
    }
}
