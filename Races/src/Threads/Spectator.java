package Threads;

import Enum.*;

import Interfaces.IPaddock_Spectator;
import Interfaces.IControlCentre_Spectator;
import Interfaces.IBettingCentre_Spectator;

public class Spectator extends Thread{
    public static volatile SpectatorState state;
    private final int id;
    private int bet;
    private int money;
    private final int NO_RACES;
    private final IBettingCentre_Spectator i_bettingCentre_spectator;
    private final IControlCentre_Spectator i_controlCentre_spectator;
    private final IPaddock_Spectator i_paddock_spectator;
    
    public Spectator( int id, int NO_RACES, IBettingCentre_Spectator i_bettingCentre_spectator, IControlCentre_Spectator i_controlCentre_spectator, IPaddock_Spectator i_paddock_spectator){
        this.id = id;
        this.NO_RACES = NO_RACES;
        this.i_bettingCentre_spectator = i_bettingCentre_spectator;
        this.i_controlCentre_spectator = i_controlCentre_spectator;
        this.i_paddock_spectator = i_paddock_spectator;
        this.money = 500;
    }

    @Override
    public void run(){
        for (int k = 0; k < NO_RACES; k++) {
            //System.out.println("Spectator " + id + " " + state);
            // i_controlCentre_spectator.waitForNextRace();
            i_paddock_spectator.waitForNextRace( id );
            //System.out.println("Spectator " + id + " " + state);
            i_paddock_spectator.goCheckHorses( id );
            //System.out.println("Spectator xxxx " + id + " " + state);
            i_bettingCentre_spectator.placeABet();
            i_controlCentre_spectator.goWatchTheRace();
            //if ( i_controlCentre_spectator.haveIWon() ) {
              //  i_bettingCentre_spectator.goCollectTheGains();
            //}
        }
        //i_controlCentre_spectator.relaxABit();
    }

    public int getSpecId() {
        return id;
    }

    public static SpectatorState getSpecState() {
        return state;
    }

    public static void setSpecState(SpectatorState state) {
        Spectator.state = state;
    }
    
    public int getMoney() {
        return money;
    }

    public void setMoney(int value) {
        this.money += value;
    }
}