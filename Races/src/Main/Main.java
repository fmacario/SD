package Main;

import Monitores.*;
import Threads.*;
import Interfaces.*;
import Enum.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fm
 */
public class Main {

    /**
     * Número de corridas (K).
     */
    public static final int NO_RACES = 4;

    /**
     * Número de competidores (N).
     */
    public static final int NO_COMPETITORS = 4;

    /**
     * Número de espetadores (M).
     */
    public static final int NO_SPECTATORS = 4;

    /**
     * Distância da pista de corrida (D).
     */
    public static final int TRACK_DISTANCE = 50;

    /**
     * Aposta máxima.
     */
    public static final double MAX_BET = 1000;
    
    /**
     * Array de cavalos/competidores.
     */
    public static Horse[] horses = new Horse[NO_COMPETITORS];

    /**
     * Array de espetadores.
     */
    public static Spectator[] spectators = new Spectator[NO_SPECTATORS];
	
    /**
     * Instanciação de monitores e threads; espera o fim das threads.
     * @param args Não usado.
     */
    public static void main(String[] args) {
        // monitores 
        GRI gri = new GRI(NO_COMPETITORS, NO_SPECTATORS, TRACK_DISTANCE);
        Paddock paddock = new Paddock(gri, NO_COMPETITORS, NO_SPECTATORS);
        Stable stable = new Stable(gri, NO_COMPETITORS);
        RacingTrack racingTrack = new RacingTrack(gri, NO_COMPETITORS, TRACK_DISTANCE);
        ControlCentre controlCentre = new ControlCentre(gri);
        BettingCentre bettingCentre = new BettingCentre(gri, NO_SPECTATORS, NO_COMPETITORS, MAX_BET);

        // threads
        Broker broker = new Broker(NO_RACES, (IBettingCentre_Broker)bettingCentre, (IControlCentre_Broker)controlCentre, (IRacingTrack_Broker)racingTrack, (IStable_Broker)stable, (IPaddock_Broker)paddock );
        broker.start();
       
        gri.setBrokerState( BrokerState.OPENING_THE_EVENT );
        gri.updateStatus();
        
        for (int i = 0; i < NO_COMPETITORS; i++) {
            horses[i] = new Horse(NO_RACES, TRACK_DISTANCE, i, (IPaddock_Horse)paddock, (IRacingTrack_Horse)racingTrack, (IStable_Horse)stable );
            horses[i].start();
        }

        for (int i = 0; i < NO_SPECTATORS; i++) {
            spectators[i] = new Spectator( i, NO_RACES, (IBettingCentre_Spectator)bettingCentre, (IControlCentre_Spectator)controlCentre, (IPaddock_Spectator)paddock );
            spectators[i].start();
        }
        
        while (true) {            
            try {
                broker.join();

                for (int i = 0; i < NO_COMPETITORS; i++) {
                    horses[i].join();
                }

                for (int i = 0; i < NO_SPECTATORS; i++) {
                    spectators[i].join();
                }
                
                System.exit(0);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }            
        }
    }
}
