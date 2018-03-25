package Threads;

import Interfaces.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Thread Broker
 * @author fm
 */
public class Broker extends Thread{
    private final int NO_RACES;
    private final IBettingCentre_Broker i_bettingCentre_broker;
    private final IControlCentre_Broker i_controlCentre_broker;
    private final IRacingTrack_Broker i_racingTrack_broker;
    private final IStable_Broker i_stable_broker;
    private final IPaddock_Broker i_paddock_broker;
    
    private Map<Integer, Integer> hashHorsesAgile = new HashMap<Integer, Integer>();
    
    private Map<Integer, List<Integer>> mapSpec_Horse_Bet = new HashMap<Integer, List<Integer>>();
    private ArrayList<Integer> horsesWinnersList = new ArrayList<>();
    private ArrayList<Integer> specsWinnersList = new ArrayList<>();
        
    /**
     *
     * @param NO_RACES Número de corridas.
     * @param i_bettingCentre_broker Interface IBettingCentre_Broker.
     * @param i_controlCentre_broker Interface IControlCentre_Broker.
     * @param i_racingTrack_broker Interface IRacingTrack_Broker.
     * @param i_stable_broker Interface IStable_Broker.
     * @param i_paddock_broker Interface IPaddock_Broker.
     */
    public Broker(int NO_RACES, IBettingCentre_Broker i_bettingCentre_broker, IControlCentre_Broker i_controlCentre_broker, IRacingTrack_Broker i_racingTrack_broker, IStable_Broker i_stable_broker, IPaddock_Broker i_paddock_broker){
        this.NO_RACES = NO_RACES;
        this.i_bettingCentre_broker = i_bettingCentre_broker;
        this.i_controlCentre_broker = i_controlCentre_broker;
        this.i_racingTrack_broker = i_racingTrack_broker;
        this.i_stable_broker = i_stable_broker;
        this.i_paddock_broker = i_paddock_broker;
    }
    
    /**
     * run()
     */
    @Override
    public void run(){
        
        for (int k = 0; k < NO_RACES; k++) {
            hashHorsesAgile = i_stable_broker.summonHorsesToPaddock( k+1 );
            while( !i_paddock_broker.waitForSpectators()) {
                
            }
            mapSpec_Horse_Bet = i_bettingCentre_broker.acceptTheBets(hashHorsesAgile);
            horsesWinnersList = i_racingTrack_broker.startTheRace();
            specsWinnersList = i_controlCentre_broker.reportResults( horsesWinnersList, mapSpec_Horse_Bet );
            if ( i_controlCentre_broker.areThereAnyWinners( mapSpec_Horse_Bet ) ) {
                i_bettingCentre_broker.honourTheBets( horsesWinnersList, specsWinnersList );
            }
        }
        i_controlCentre_broker.entertainTheGuests();
        i_stable_broker.end();
        //System.out.println("Bye BROKER");
    }
    
}