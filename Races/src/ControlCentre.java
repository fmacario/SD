
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ControlCentre implements IControlCentre_Spectator, IControlCentre_Broker{
    private final ReentrantLock rl;
    private final Condition condHorses;
    private int NO_COMPETITORS;
    
    public ControlCentre( int NO_COMPETITORS ){
        this.NO_COMPETITORS = NO_COMPETITORS;
        rl = new ReentrantLock(true);
        condHorses = rl.newCondition();
    }
    
    @Override
    public void waitForNextRace() {
        rl.lock();
        
        try {
            try{
                //while (  ) {
                    
                //}
                Spectator.state = SpectatorState.WAITING_FOR_A_RACE_TO_START;
            } catch (Exception e) { 
                e.printStackTrace();
            }
        }finally{
            rl.unlock();
        }
    }

    @Override
    public void goWatchTheRace() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean haveIWon() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void relaxABit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reportResults() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean areThereAnyWinners() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void entertainTheGuests() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
