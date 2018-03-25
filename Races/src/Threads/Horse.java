package Threads;

import Enum.*;
import Interfaces.IStable_Horse;
import Interfaces.IPaddock_Horse;
import Interfaces.IRacingTrack_Horse;
import Main.*;

public class Horse extends Thread{

    public static volatile HorseState state;
    private final int id;
    private final int Pnk;
    private final IPaddock_Horse i_paddock_horse;
    private final IRacingTrack_Horse i_racingTrack_horse;
    private final IStable_Horse i_stable_horse;
    
    public Horse(int id, IPaddock_Horse i_paddock_horse, IRacingTrack_Horse i_racingTrack_horse, IStable_Horse i_stable_horse){
        this.id = id;
        this.Pnk = (int )(Math.random() * (Main.TRACK_DISTANCE/4) + 3); // Pnk
        System.out.println("horse MAX - "+Pnk);
        this.i_paddock_horse = i_paddock_horse;
        this.i_racingTrack_horse = i_racingTrack_horse;
        this.i_stable_horse = i_stable_horse;
    }
    
    @Override
    public void run(){
        for (int k = 0; k < Main.NO_RACES; k++) {
            i_stable_horse.proceedToStable( id, Pnk );
            i_paddock_horse.proceedToPaddock(id);
            i_racingTrack_horse.proceedToStartLine(id);
            while( !i_racingTrack_horse.hasFinishLineBeenCrossed( id ) ){
                i_racingTrack_horse.makeAMove( id, Pnk );
            }
        }
        i_stable_horse.proceedToStable(id, Pnk);
        System.out.println("HORSE " + id + " ACABEI! --------------//////////////");
    }
    
}
