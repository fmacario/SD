package Threads;

import Interfaces.IStable_Horse;
import Interfaces.IPaddock_Horse;
import Interfaces.IRacingTrack_Horse;

/**
 * Thread Horse
 * @author fm
 */
public class Horse extends Thread{

    private final int id;
    private final int Pnk;
    private final IPaddock_Horse i_paddock_horse;
    private final IRacingTrack_Horse i_racingTrack_horse;
    private final IStable_Horse i_stable_horse;
    private final int NO_RACES;
    private final int TRACK_DISTANCE;
    
    /**
     * @param id Id do cavalo.
     * @param i_paddock_horse Interface IPaddock_Horse;
     * @param i_racingTrack_horse Interface IRacingTrack_Horse.
     * @param i_stable_horse Interface IStable_Horse.
     */
    public Horse(int NO_RACES, int TRACK_DISTANCE, int id, IPaddock_Horse i_paddock_horse, IRacingTrack_Horse i_racingTrack_horse, IStable_Horse i_stable_horse){
        this.NO_RACES = NO_RACES;
        this.TRACK_DISTANCE = TRACK_DISTANCE;
        this.id = id;
        this.Pnk = (int )(Math.random() * (TRACK_DISTANCE/4) + 3); // Pnk
        this.i_paddock_horse = i_paddock_horse;
        this.i_racingTrack_horse = i_racingTrack_horse;
        this.i_stable_horse = i_stable_horse;
    }
    
    /**
     * run()
     */
    @Override
    public void run(){
        for (int k = 0; k < NO_RACES ; k++) {
            i_stable_horse.proceedToStable( id, Pnk );
            i_paddock_horse.proceedToPaddock(id);
            i_racingTrack_horse.proceedToStartLine(id);
            while( !i_racingTrack_horse.hasFinishLineBeenCrossed( id ) ){
                i_racingTrack_horse.makeAMove( id, Pnk );
            }
        }
        i_stable_horse.proceedToStable(id, Pnk);
        //System.out.println("Bye HORSE " + id);
    }
    
}
