public class Main {
    public static final int NO_RACES = 5;
    public static final int NO_COMPETITORS = 4;
    public static final int NO_SPECTATORS = 4;
    public static final int TRACK_DISTANCE = 10;
    
    public static Horse[] horses = new Horse[NO_COMPETITORS];
    public static Spectator[] spectators = new Spectator[NO_SPECTATORS];
	
    public static void main(String[] args) {
            // monitores 
            Paddock paddock = new Paddock();
            Stable stable = new Stable();
            RacingTrack racingTrack = new RacingTrack();
            ControlCentre controlCentre = new ControlCentre(NO_COMPETITORS);
            BettingCentre bettingCentre = new BettingCentre();
            GRI gri = new GRI();
            
            // threads
            Broker broker = new Broker(NO_RACES, (IBettingCentre_Broker)bettingCentre, (IControlCentre_Broker)controlCentre, (IRacingTrack_Broker)racingTrack, (IStable_Broker)stable );
            
            for (int i = 0; i < NO_COMPETITORS; i++) {
                horses[i] = new Horse(i, (IPaddock_Horse)paddock, (IRacingTrack_Horse)racingTrack, (IStable_Horse)stable );
                horses[i].start();
            }
            
            for (int i = 0; i < NO_SPECTATORS; i++) {
                spectators[i] = new Spectator( i, NO_RACES, (IBettingCentre_Spectator)bettingCentre, (IControlCentre_Spectator)controlCentre, (IPaddock_Spectator)paddock );
                spectators[i].start();
            }
           
             
            broker.start();
            
	}

}
