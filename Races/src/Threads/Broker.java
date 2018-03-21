package Threads;

import Interfaces.*;
import Enum.*;

public class Broker extends Thread{
    private final int NO_RACES;
    private final IBettingCentre_Broker i_bettingCentre_broker;
    private final IControlCentre_Broker i_controlCentre_broker;
    private final IRacingTrack_Broker i_racingTrack_broker;
    private final IStable_Broker i_stable_broker;
    
    public static volatile BrokerState state;
    
    public Broker(int NO_RACES, IBettingCentre_Broker i_bettingCentre_broker, IControlCentre_Broker i_controlCentre_broker, IRacingTrack_Broker i_racingTrack_broker, IStable_Broker i_stable_broker){
        this.NO_RACES = NO_RACES;
        this.i_bettingCentre_broker = i_bettingCentre_broker;
        this.i_controlCentre_broker = i_controlCentre_broker;
        this.i_racingTrack_broker = i_racingTrack_broker;
        this.i_stable_broker = i_stable_broker;
    }
    
    @Override
    public void run(){
        
        this.state = BrokerState.OPENING_THE_EVENT;
        System.out.println("Broker " + state);
        
        for (int k = 0; k < NO_RACES; k++) {
            i_stable_broker.summonHorsesToPaddock();
            //System.out.println("Broker " + state);
            i_bettingCentre_broker.acceptTheBets();
            //i_racingTrack_broker.startTheRace();
            //i_controlCentre_broker.reportResults();
            //if ( i_controlCentre_broker.areThereAnyWinners() ) {
              //  i_bettingCentre_broker.honourTheBets();
            //}
        }
        //i_controlCentre_broker.entertainTheGuests();
    }
    
    public BrokerState getBroState() {
        return Broker.state;
    }

    public void setBroState(BrokerState state) {
        Broker.state = state;
    }
}