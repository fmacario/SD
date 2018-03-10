public class Main {
    public static final int NO_RACES = 5;
    public static final int NO_COMPETITORS = 4;
    public static final int NO_SPECTATORS = 4;
	
    public static void main(String[] args) {
            // monitores 
            Paddock paddock = new Paddock();
            Stable stable = new Stable();
            RacingTrack racingTrack = new RacingTrack();
            ControlCentre controlCentre = new ControlCentre();
            BettingCentre bettingCentre = new BettingCentre();
            GRI gri = new GRI();
            
            // threads
            Broker broker = new Broker(NO_RACES, (IBettingCentre_Broker)bettingCentre, (IControlCentre_Broker)controlCentre, (IRacingTrack_Broker)racingTrack, (IStable_Broker)stable );
            Horse horse = new Horse(1, (IPaddock_Horse)paddock, (IRacingTrack_Horse)racingTrack, (IStable_Horse)stable );
            Spectator spectator = new Spectator( 1, NO_RACES, (IBettingCentre_Spectator)bettingCentre, (IControlCentre_Spectator)controlCentre, (IPaddock_Spectator)paddock );
            
            broker.start();
            horse.start();
            spectator.start();
	}

}
