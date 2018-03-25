package Threads;

import Interfaces.IPaddock_Spectator;
import Interfaces.IControlCentre_Spectator;
import Interfaces.IBettingCentre_Spectator;

/**
 * Thread Spectator
 * @author fm
 */
public class Spectator extends Thread{

    private final int id;
    private int money;
    private final int NO_RACES;
    private final IBettingCentre_Spectator i_bettingCentre_spectator;
    private final IControlCentre_Spectator i_controlCentre_spectator;
    private final IPaddock_Spectator i_paddock_spectator;
    
    /**
     * @param id Id do espetador.
     * @param NO_RACES Número de corridas.
     * @param i_bettingCentre_spectator Interface IBettingCentre_Spectator.
     * @param i_controlCentre_spectator Interface IControlCentre_Spectator.
     * @param i_paddock_spectator Interface IPaddock_Spectator.
     */
    public Spectator( int id, int NO_RACES, IBettingCentre_Spectator i_bettingCentre_spectator, IControlCentre_Spectator i_controlCentre_spectator, IPaddock_Spectator i_paddock_spectator){
        this.id = id;
        this.NO_RACES = NO_RACES;
        this.i_bettingCentre_spectator = i_bettingCentre_spectator;
        this.i_controlCentre_spectator = i_controlCentre_spectator;
        this.i_paddock_spectator = i_paddock_spectator;
        this.money = 500;
    }

    /**
     * run()
     */
    @Override
    public void run(){
        for (int k = 0; k < NO_RACES; k++) {
            // i_controlCentre_spectator.waitForNextRace();
            i_paddock_spectator.waitForNextRace( id );
            i_paddock_spectator.goCheckHorses( id );
            money -= i_bettingCentre_spectator.placeABet( id, money);
            i_controlCentre_spectator.goWatchTheRace( id );
            if ( i_controlCentre_spectator.haveIWon( id ) ) {
                money += i_bettingCentre_spectator.goCollectTheGains( id );
            }
        }
        i_controlCentre_spectator.relaxABit( id );
        //System.out.println("Bye SPECTATOR " + id);
    }

}