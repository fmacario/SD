package Main;

import Monitores.*;
import Threads.*;
import Interfaces.*;

public class Main {
    public static final int NO_RACES = 2;
    public static final int NO_COMPETITORS = 4;
    public static final int NO_SPECTATORS = 4;
    public static final int TRACK_DISTANCE = 50;
    public static final double MAX_BET = 1000;
    
    public static Horse[] horses = new Horse[NO_COMPETITORS];
    public static Spectator[] spectators = new Spectator[NO_SPECTATORS];
	
    public static void main(String[] args) {
            // monitores 
            GRI gri = new GRI();
            Paddock paddock = new Paddock(gri);
            Stable stable = new Stable(gri);
            RacingTrack racingTrack = new RacingTrack(gri);
            ControlCentre controlCentre = new ControlCentre(gri);
            BettingCentre bettingCentre = new BettingCentre(gri);
            //GRI gri = new GRI();
            
            // threads
            Broker broker = new Broker(NO_RACES, (IBettingCentre_Broker)bettingCentre, (IControlCentre_Broker)controlCentre, (IRacingTrack_Broker)racingTrack, (IStable_Broker)stable, (IPaddock_Broker)paddock );
            broker.start();
            
            for (int i = 0; i < NO_COMPETITORS; i++) {
                horses[i] = new Horse(i, (IPaddock_Horse)paddock, (IRacingTrack_Horse)racingTrack, (IStable_Horse)stable );
                horses[i].start();
            }
            
            for (int i = 0; i < NO_SPECTATORS; i++) {
                spectators[i] = new Spectator( i, NO_RACES, (IBettingCentre_Spectator)bettingCentre, (IControlCentre_Spectator)controlCentre, (IPaddock_Spectator)paddock );
                spectators[i].start();
            }
            
	}

}
