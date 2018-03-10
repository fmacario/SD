public class Horse extends Thread{
    private final int id;
    private final IPaddock_Horse i_paddock_horse;
    private final IRacingTrack_Horse i_racingTrack_horse;
    private final IStable_Horse i_stable_horse;
    
    public Horse(int id, IPaddock_Horse i_paddock_horse, IRacingTrack_Horse i_racingTrack_horse, IStable_Horse i_stable_horse){
        this.id = id;
        this.i_paddock_horse = i_paddock_horse;
        this.i_racingTrack_horse = i_racingTrack_horse;
        this.i_stable_horse = i_stable_horse;
    }
    
    @Override
    public void run(){
        i_stable_horse.proceedToStable();
        i_paddock_horse.proceedToPaddock();
        i_racingTrack_horse.proceedToStartLine();
        while( !i_racingTrack_horse.hasFinishLineBeenCrossed() ){
            i_racingTrack_horse.makeAMove();
        }
        i_stable_horse.proceedToStable();
    }
}
