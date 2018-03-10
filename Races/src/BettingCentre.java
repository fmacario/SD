import java.util.LinkedList;
import java.util.Queue;

public class BettingCentre implements IBettingCentre_Spectator, IBettingCentre_Broker{
    private Queue<Spectator> fifoSpectators = new LinkedList<Spectator>();
    
    @Override
    public void placeABet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void goCollectTheGains() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void acceptTheBets() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void honourTheBets() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
