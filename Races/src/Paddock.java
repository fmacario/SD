import java.util.*;

public class Paddock implements IPaddock_Horse, IPaddock_Spectator{
    private Map<Integer, Horse> hashHorses = new HashMap<Integer, Horse>();
    private Map<Integer, Spectator> hashSpectators = new HashMap<Integer, Spectator>();
    
    @Override
    public void proceedToPaddock() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void goCheckHorses() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
