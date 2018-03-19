import java.util.*;

public class RacingTrack implements IRacingTrack_Broker, IRacingTrack_Horse{
    private Map<Integer, Horse> hashHorses = new HashMap<Integer, Horse>();

    @Override
    public void startTheRace() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void proceedToStartLine() {
        try {
            
        } catch (Exception e) {
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
