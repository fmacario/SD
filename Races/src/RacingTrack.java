import java.util.*;
import java.util.concurrent.locks.*;

public class RacingTrack implements IRacingTrack_Broker, IRacingTrack_Horse{
    private Map<Integer, Horse> hashHorses = new HashMap<Integer, Horse>();

    private final ReentrantLock rl;
    private final Condition condHorses;
    private final Condition condBroker;
    
    private int nHorses = 0;
    private final int NO_COMPETITORS = Main.NO_COMPETITORS;
    
    public RacingTrack(){
        rl = new ReentrantLock();
        condHorses = rl.newCondition();
        condBroker = rl.newCondition();
    }
    
    @Override
    public void startTheRace() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void proceedToStartLine() {
        rl.lock();
        
        try{
            try {
                nHorses++;
                
            } catch (Exception e) {
            } 
        }finally {
            rl.unlock();
        }
    }

    @Override
    public boolean hasFinishLineBeenCrossed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void makeAMove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
