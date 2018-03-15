public class Spectator extends Thread{
    private final int id;
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
    }

    @Override
    public void run(){
        for (int k = 0; k < NO_RACES; k++) {
            i_controlCentre_spectator.waitForNextRace();
            i_paddock_spectator.goCheckHorses();
            i_bettingCentre_spectator.placeABet();
            i_controlCentre_spectator.goWatchTheRace();
            if ( i_controlCentre_spectator.haveIWon() ) {
                i_bettingCentre_spectator.goCollectTheGains();
            }
        }
        i_controlCentre_spectator.relaxABit();
    }
}